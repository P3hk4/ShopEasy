package base.ControllerAPI;

import base.DTO.MapperDTO;
import base.DTO.ProductDTO;
import base.Entity.Product;
import base.Service.CategoryService;
import base.Service.ManufacturerService;
import base.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private MapperDTO mapperDTO;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts()
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !products.isEmpty()
                ? new ResponseEntity<>(products, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(name = "id") int id) {
        ProductDTO product = mapperDTO.entityToDTO(productService.getProductByProductId(id));
        return product != null
                ? new ResponseEntity<>(product, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/categories/id/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable(name = "id") int id) {
        List<ProductDTO> products = productService.getAllProductsByCategoryId(id)
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !products.isEmpty()
                ? new ResponseEntity<>(products, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/categories/name/{name}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryName(@PathVariable(name = "name") String name) {
        return getProductsByCategoryId(categoryService.getCategoryByName(name).getCategoryId());
    }


    @GetMapping("/manufacturers/id/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getProductsByManufacturerId(@PathVariable(name = "id") int id) {
        List<ProductDTO> products = productService.getAllProductsByManufacturerId(id)
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !products.isEmpty()
                ? new ResponseEntity<>(products, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/manufacturers/name/{name}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getProductsByManufacturerName(@PathVariable(name = "name") String name) {
        return getProductsByManufacturerId(manufacturerService.getManufacturerByName(name).getManufacturerId());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@RequestBody Product product) {
        productService.saveProduct(product);
        return product;
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") int id) {
        try {
            productService.deleteProduct(productService.getProductByProductId(id));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Категория успено удалена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении категории: " + e.getMessage());
        }
    }

}
