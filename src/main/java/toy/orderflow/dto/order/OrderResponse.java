package toy.orderflow.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import toy.orderflow.domain.order.Order;

public record OrderResponse(
        Long id,
        String ordererName,
        String status,
        int totalPrice,
        List<OrderItemResponse> items,
        LocalDateTime orderedAt
) {
    public record OrderItemResponse(
            Long productId,
            String productName,
            int price,
            int quantity
    ) {
    }

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrdererName(),
                order.getStatus().name(),
                order.getTotalPrice(),
                items,
                order.getOrderedAt()
        );
    }
}
