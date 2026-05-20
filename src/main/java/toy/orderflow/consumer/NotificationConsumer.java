package toy.orderflow.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import toy.orderflow.config.RabbitMQConfig;
import toy.orderflow.event.OrderCreatedEvent;

@Component
@Slf4j
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleOrderEvent(OrderCreatedEvent event) {
        log.info("[알림] 주문 알림 발송 - orderId: {}, 주문자: {}, 총액: {}원",
                event.orderId(), event.ordererName(), event.totalPrice());
    }
}
