package base.Service;

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

    public List<Product> getAllProducts(){
        return productRepository.findAllBy();
    }
    public List<Product> getAllProductsByCategoryId(int id){
        return  productRepository.findProductsByCategoryId(id);
    }
    public List<Product> getAllProductsByManufacturerId(int id){
        return productRepository.findProductsByManufacturerId(id);
    }
    public List<Product> getAllProductsByPriceIsBetween(int minPrice, int maxPrice){
        return productRepository.findProductsByPriceIsBetween(minPrice,maxPrice);
    }
    public List<Integer> getAllProductIds(){
        return productRepository.findAllProductIds();
    }
    public List<Product> getNineProductsFrom(int page){
        return productRepository.findNineProductsFrom((page-1)*9);
    }
    public List<Product> getNineProductsFromWithCategory(int page, int categoryId){
        return productRepository.findNineProductsFromWithCategory((page-1)*9, categoryId);
    }

    public Product getProductByName(String name){
        return productRepository.findProductByName(name);
    }
    public Product getProductByProductId(int id){
        return productRepository.findProductByProductId(id);
    }
    public Integer getTotalProductsPages(){
        return productRepository.getTotalProductsPages();
    }
    public Integer getTotalProductsPagesWithCategoryById(int categoryId){
        return productRepository.getTotalProductsPagesWithCategory(categoryId);
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
