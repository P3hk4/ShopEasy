package base.Repository;

import base.DTO.ProductDTO;
import base.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllBy();

    Product findProductByName(String name);

    Product findProductByProductId(int id);

    List<Product> findProductsByCategoryId(int id);

    List<Product> findProductsByManufacturerId(int id);

    List<Product> findProductsByPriceIsBetween(int minPrice, int maxPrice);

    @Query("SELECT p.productId FROM Products p")
    List<Integer> findAllProductIds();

    @Query("SELECT new base.DTO.ProductDTO(p.productId,p.categoryId,p.manufacturerId,p.name,p.description,p.price) FROM Products p")
    List<ProductDTO> findAllProductDTO();

    @Query("SELECT new base.DTO.ProductDTO(p.productId,p.categoryId,p.manufacturerId,p.name,p.description,p.price) FROM Products p WHERE p.productId = :id")
    ProductDTO findProductDTOByProductId(@Param("id") int id);

    @Query("SELECT new base.DTO.ProductDTO(p.productId,p.categoryId,p.manufacturerId,p.name,p.description,p.price) FROM Products p WHERE p.categoryId = :categoryId")
    List<ProductDTO> findAllProductDTOByCategoryId(@Param("categoryId") int id);

    @Query("SELECT new base.DTO.ProductDTO(p.productId,p.categoryId,p.manufacturerId,p.name,p.description,p.price) FROM Products p WHERE p.manufacturerId = :manufacturerId")
    List<ProductDTO> findAllProductDTOByManufacturerId(@Param("manufacturerId") int id);

}
