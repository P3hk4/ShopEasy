package base.Repository;

import base.Entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    Manufacturer findManufacturerByManufacturerId(int id);

    Manufacturer findManufacturerByName(String name);

    List<Manufacturer> findManufacturersByCountry(String country);

    List<Manufacturer> findAllManufacturersBy();

    @Query("SELECT m.manufacturerId FROM Manufacturers m")
    List<Integer> findAllManufacturerIds();
}
