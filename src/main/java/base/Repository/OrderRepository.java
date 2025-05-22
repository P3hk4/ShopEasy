package base.Repository;

import base.Entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderByOrderId(int id);

    List<Order> findOrdersByClientId(int clientId);

    List<Order> findOrdersByShippingId(int shippingId);

    List<Order> findOrdersByDate(Date date);

    List<Order> findAllBy();

    @Query("SELECT o FROM Orders o WHERE o.totalCost = 0")
    List<Order> findOrdersWithZeroTotalCost();

    @Query("SELECT o.orderId FROM Orders o WHERE o.totalCost = 0")
    List<Integer> findOrderIdsWithZeroTotalCost();
}
