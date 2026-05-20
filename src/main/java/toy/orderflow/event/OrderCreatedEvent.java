package toy.orderflow.event;

import java.time.LocalDateTime;
import java.util.List;
import toy.orderflow.domain.order.Order;

public record OrderCreatedEvent(
        Long orderId,
        String ordererName,
        int totalPrice,
        List<OrderItemInfo> items,
        LocalDateTime orderedAt
) {
    public record OrderItemInfo(Long productId, String productName, int quantity) {
    }

    public static OrderCreatedEvent from(Order order) {
        List<OrderItemInfo> items = order.getOrderItems().stream()
                .map(item -> new OrderItemInfo(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity()
                ))
                .toList();

        return new OrderCreatedEvent(
                order.getId(),
                order.getOrdererName(),
                order.getTotalPrice(),
                items,
                order.getOrderedAt()
        );
    }
}
