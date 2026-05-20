package toy.orderflow.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toy.orderflow.domain.product.Product;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long productId;

    private String productName;

    private int price;

    private int quantity;

    public static OrderItem create(Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.productId = product.getId();
        item.productName = product.getName();
        item.price = product.getPrice();
        item.quantity = quantity;
        return item;
    }

    void assignOrder(Order order) {
        this.order = order;
    }

    public int getTotalPrice() {
        return price * quantity;
    }
}
