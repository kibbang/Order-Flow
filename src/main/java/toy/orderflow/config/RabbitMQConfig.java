package toy.orderflow.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String DLQ_EXCHANGE = "order.dlq.exchange";

    public static final String STOCK_QUEUE = "stock.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String DLQ = "order.dlq";

    public static final String ORDER_CREATED_KEY = "order.created";

    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    DirectExchange dlqExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    Queue stockQueue() {
        return QueueBuilder.durable(STOCK_QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .build();
    }

    @Bean
    Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }

    @Bean
    Queue dlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    Binding stockBinding(Queue stockQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(stockQueue).to(orderExchange).with(ORDER_CREATED_KEY);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(notificationQueue).to(orderExchange).with("order.#");
    }

    @Bean
    Binding dlqBinding(Queue dlq, DirectExchange dlqExchange) {
        return BindingBuilder.bind(dlq).to(dlqExchange).with("dlq");
    }

    @Bean
    JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
