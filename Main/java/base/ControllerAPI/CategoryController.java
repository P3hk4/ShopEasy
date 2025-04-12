package base.ControllerAPI;

import base.DTO.CategoryDTO;
import base.DTO.MapperDTO;
import base.Entity.Category;
import base.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MapperDTO mapperDTO;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories()
                .stream().map(mapperDTO::entityToDTO).collect(Collectors.toList());
        return !categories.isEmpty()
                ? new ResponseEntity<>(categories, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable(name = "id") int id) {
        CategoryDTO category = mapperDTO.entityToDTO(categoryService.getCategoryById(id));
        return category != null
                ? new ResponseEntity<>(category, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable(name = "name") String name) {
        CategoryDTO category = mapperDTO.entityToDTO(categoryService.getCategoryByName(name));
        return category != null
                ? new ResponseEntity<>(category, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/all")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Integer>> getCategoryIds() {
        List<Integer> ids =  categoryService.getAllCategoriesId();
        return ids != null
                ? new ResponseEntity<>(ids, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/all")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<String>> getCategoryNames() {
        List<String> names =  categoryService.getAllCategoriesNames();
        return names != null
                ? new ResponseEntity<>(names, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category save(@RequestBody Category category) {
        categoryService.saveCategory(category);
        return category;
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") int id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Категория успено удалена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении категории: " + e.getMessage());
        }
    }
}
