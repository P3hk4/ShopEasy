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

    @Query(value = "SELECT * FROM Products p ORDER BY p.product_Id OFFSET :offset LIMIT 9", nativeQuery = true)
    List<Product> findNineProductsFrom(@Param("offset") int offset);

    @Query(value = "SELECT * FROM Products p WHERE p.category_Id = :categoryId ORDER BY p.product_Id OFFSET :offset LIMIT 9", nativeQuery = true)
    List<Product> findNineProductsFromWithCategory(@Param("offset") int offset, @Param("categoryId") int categoryId);

    @Query(value = "SELECT CEIL(COUNT(*)::float / 9) FROM products", nativeQuery = true)
    int getTotalProductsPages();

    @Query(value = "SELECT CEIL(COUNT(*)::float / 9) FROM products p WHERE p.category_Id = :categoryId", nativeQuery = true)
    int getTotalProductsPagesWithCategory(@Param("categoryId") int categoryId);
}
