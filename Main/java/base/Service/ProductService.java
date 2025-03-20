package base.Service;

import base.Entity.Order;
import base.Entity.Product;
import base.Repository.ProductRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllBy(){
        return productRepository.findAllBy();
    }

    public Product getProductByName(String name){
        return productRepository.findProductByName(name);
    }

    public Product getProductByProductId(int id){
        return productRepository.findProductByProductId(id);
    }

    public List<Product> getProductsByCategoryId(int id){
        return  productRepository.findProductsByCategoryId(id);
    }

    public List<Product> getProductsByManufacturerId(int id){
        return productRepository.findProductsByManufacturerId(id);
    }

    public List<Product> getProductsByCategoryIdAndAndManufacturerId(int categoryId, int manufacturerId){
        return productRepository.findProductsByCategoryIdAndAndManufacturerId(categoryId,manufacturerId);
    }

    public List<Product> getProductsByPriceIsBetween(int minPrice, int maxPrice){
        return productRepository.findProductsByPriceIsBetween(minPrice,maxPrice);
    }

    public List<Integer> getAllProductIds(){
        return productRepository.findAllProductIds();
    }

    public void saveProduct(Product product){
        try {
            productRepository.save(product);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }

    }

    public void saveAllProducts(List<Product> products){
        try {
            productRepository.saveAll(products);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void deleteProduct(Product product){
        productRepository.delete(product);
    }

}
