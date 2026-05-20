package toy.orderflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.orderflow.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
