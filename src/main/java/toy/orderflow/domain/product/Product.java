package toy.orderflow.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    public static Product create(String name, int price, int stockQuantity) {
        Product product = new Product();
        product.name = name;
        product.price = price;
        product.stockQuantity = stockQuantity;
        return product;
    }

    public void deductStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException(
                    "재고가 부족합니다. 현재 재고: %d, 요청 수량: %d".formatted(this.stockQuantity, quantity));
        }
        this.stockQuantity -= quantity;
    }
}
