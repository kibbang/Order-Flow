package toy.orderflow.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.orderflow.domain.order.Order;
import toy.orderflow.domain.order.OrderItem;
import toy.orderflow.domain.product.Product;
import toy.orderflow.dto.order.OrderCreateRequest;
import toy.orderflow.dto.order.OrderResponse;
import toy.orderflow.event.OrderEventPublisher;
import toy.orderflow.repository.OrderRepository;
import toy.orderflow.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEventPublisher eventPublisher;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        List<OrderItem> items = request.items().stream()
                .map(itemReq -> {
                    Product product = productRepository.findById(itemReq.productId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "상품을 찾을 수 없습니다. id: " + itemReq.productId()));
                    return OrderItem.create(product, itemReq.quantity());
                })
                .toList();

        Order order = Order.create(request.ordererName(), items);
        orderRepository.save(order);

        eventPublisher.publishOrderCreated(order);

        return OrderResponse.from(order);
    }

    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. id: " + orderId));
        return OrderResponse.from(order);
    }

    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }
}
