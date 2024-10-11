package demo.ecommerce.order;

import demo.ecommerce.customer.CustomerClient;
import demo.ecommerce.exception.BusinessException;
import demo.ecommerce.kafka.OrderConfirmation;
import demo.ecommerce.kafka.OrderProducer;
import demo.ecommerce.orderline.OrderLineRequest;
import demo.ecommerce.orderline.OrderLineService;
import demo.ecommerce.payment.PaymentClient;
import demo.ecommerce.payment.PaymentRequest;
import demo.ecommerce.product.ProductClient;
import demo.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order :: Customer not found with ID::" + request.customerId()));

        var purchasedProducts = productClient.purchaseProducts(request.products());

        var order = orderRepository.save(orderMapper.toOrder(request));
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer

        );

        paymentClient.requestOrderPayment(paymentRequest);

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream().map(orderMapper::fromOrder)
                .toList();
    }

    public OrderResponse findOrderById(Integer orderId) {
        return orderRepository.findById(orderId).map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find order :: Order not found with ID::" + orderId));
    }
}
