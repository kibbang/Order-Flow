package toy.orderflow.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import toy.orderflow.config.RabbitMQConfig;
import toy.orderflow.event.OrderCreatedEvent;
import toy.orderflow.repository.OrderRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeadLetterConsumer {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    @Transactional
    public void handleFailedMessage(OrderCreatedEvent event) {
        log.error("[DLQ] 주문 처리 실패 - orderId: {}", event.orderId());

        orderRepository.findById(event.orderId()).ifPresent(order -> {
            order.fail();
            log.info("[DLQ] 주문 {} 실패 처리 완료 → FAILED", event.orderId());
        });
    }
}
