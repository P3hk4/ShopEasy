package base.Repository;

import base.DTO.CategoryDTO;
import base.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

     Category findCategoryByCategoryId(int id);

     Category findCategoryByName(String name);

     List<Category> findAllCategoryBy();

     @Query("SELECT c.categoryId FROM Categories c")
     List<Integer> findAllCategoryIds();

     @Query("SELECT c.name FROM Categories c")
     List<String> findAllCategoryNames();

     @Query("SELECT new base.DTO.CategoryDTO(c.categoryId,c.name) FROM Categories c")
     List<CategoryDTO> findAllCategoryDTO();

     @Query("SELECT new base.DTO.CategoryDTO(c.categoryId,c.name) FROM Categories c WHERE c.categoryId = :id")
     CategoryDTO findCategoryDTOByCategoryId(@Param("id") int id);

     @Query("SELECT new base.DTO.CategoryDTO(c.categoryId,c.name) FROM Categories c WHERE c.name = :name")
     CategoryDTO findCategoryDTOByName(@Param("name") String name);
}
