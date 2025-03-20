package base.Repository;

import base.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllBy();

    Product findProductByName(String name);

    Product findProductByProductId(int id);

    List<Product> findProductsByCategoryId(int id);

    List<Product> findProductsByManufacturerId(int id);

    List<Product> findProductsByCategoryIdAndAndManufacturerId(int categoryId, int manufacturerId);

    List<Product> findProductsByPriceIsBetween(int minPrice, int maxPrice);

    @Query("SELECT p.productId FROM Products p")
    List<Integer> findAllProductIds();
}
