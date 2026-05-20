package toy.orderflow.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ordererName;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderedAt;

    public static Order create(String ordererName, List<OrderItem> items) {
        Order order = new Order();
        order.ordererName = ordererName;
        order.status = OrderStatus.PENDING;
        order.orderedAt = LocalDateTime.now();
        items.forEach(order::addOrderItem);
        order.totalPrice = items.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return order;
    }

    private void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.assignOrder(this);
    }

    public void confirm() {
        this.status = OrderStatus.CONFIRMED;
    }

    public void fail() {
        this.status = OrderStatus.FAILED;
    }
}
