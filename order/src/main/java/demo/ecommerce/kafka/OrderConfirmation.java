package demo.ecommerce.kafka;

import demo.ecommerce.customer.CustomerResponse;
import demo.ecommerce.order.PaymentMethod;
import demo.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
