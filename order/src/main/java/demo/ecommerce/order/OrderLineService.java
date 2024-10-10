package demo.ecommerce.order;

import demo.ecommerce.orderline.OrderLineMapper;
import demo.ecommerce.orderline.OrderLineRepository;
import demo.ecommerce.orderline.OrderLineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        var order = mapper.toOrderLine(orderLineRequest);
        return repository.save(order).getId();
    }
}
