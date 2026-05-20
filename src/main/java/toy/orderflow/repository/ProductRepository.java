package toy.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.orderflow.domain.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
