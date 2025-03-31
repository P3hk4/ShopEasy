package base.Repository;

import base.DTO.ManufacturerDTO;
import base.Entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT new base.DTO.ManufacturerDTO(m.manufacturerId,m.country,m.name) FROM Manufacturers m")
    List<ManufacturerDTO> findAllManufacturerDTO();

    @Query("SELECT new base.DTO.ManufacturerDTO(m.manufacturerId,m.country,m.name) FROM Manufacturers m WHERE m.manufacturerId = :id")
    ManufacturerDTO findManufacturerDTOById(@Param("id") int id);

    @Query("SELECT new base.DTO.ManufacturerDTO(m.manufacturerId,m.country,m.name) FROM Manufacturers m WHERE m.country = :county")
    List<ManufacturerDTO> findAllManufacturerDTOByCountry(@Param("country") String country);
}
