package base.Repository;

import base.Entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    OrderProduct findOrderProductByOrderProductId(int id);

    List<OrderProduct> findOrderProductsByOrderId(int id);

    List<OrderProduct> findOrderProductsByProductId(int id);

    @Query("SELECT op FROM Order_Product op WHERE op.orderId IN :orderIds")
    List<OrderProduct> findOrderProductsByOrderIds(@Param("orderIds") List<Integer> orderIds);
}
