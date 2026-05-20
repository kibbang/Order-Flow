package toy.orderflow.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import toy.orderflow.config.RabbitMQConfig;
import toy.orderflow.domain.order.Order;
import toy.orderflow.domain.product.Product;
import toy.orderflow.event.OrderCreatedEvent;
import toy.orderflow.repository.OrderRepository;
import toy.orderflow.repository.ProductRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockConsumer {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.STOCK_QUEUE)
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("[재고] 주문 {} 재고 차감 시작", event.orderId());

        for (OrderCreatedEvent.OrderItemInfo item : event.items()) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalStateException(
                            "상품을 찾을 수 없습니다: " + item.productId()));
            product.deductStock(item.quantity());
        }

        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new IllegalStateException(
                        "주문을 찾을 수 없습니다: " + event.orderId()));
        order.confirm();

        log.info("[재고] 주문 {} 재고 차감 완료 → 주문 확정(CONFIRMED)", event.orderId());
    }
}
