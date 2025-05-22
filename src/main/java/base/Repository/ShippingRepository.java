package base.Repository;

import base.Entity.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    Shipping findShippingByShippingId(int id);

    List<Shipping> findShippingsBy();

    List<Shipping> findShippingsByClientId(int id);

    List<Shipping> findShippingsByAddress(String address);

    List<Shipping> findShippingsByCity(String city);

    List<Shipping> findShippingsByClientIdAndAddress(int id, String address);

    @Query("SELECT s.shippingId FROM Shipping s")
    List<Integer> findAllShippingIds();
}
