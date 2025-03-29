package base.Service;

import base.DTO.CategoryDTO;
import base.Entity.Category;
import base.Repository.CategoryRepository;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(int id){
        return categoryRepository.findCategoryByCategoryId(id);
    }

    public Category getCategoryByName(String name){return categoryRepository.findCategoryByName(name);}

    public CategoryDTO getCategoryDTOById(int id){return categoryRepository.findCategoryDTOByCategoryId(id);}

    public CategoryDTO getCategoryDTOByName(String name){return categoryRepository.findCategoryDTOByName(name);}

    public void saveCategory(Category category){
        try {
            categoryRepository.save(category);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void saveAllCategories(ArrayList<Category> categories){
        try {
            categoryRepository.saveAll(categories);
        } catch (StaleObjectStateException e) {
            System.out.println("Конфликт при сохранении");
        }
    }

    public void deleteCategoryById(int id){
        categoryRepository.delete(getCategoryById(id));
    }

    public List<Integer> getAllCategoriesId(){
        return categoryRepository.findAllCategoryIds();
    }

    public List<String> getAllCategoriesNames(){
        return categoryRepository.findAllCategoryNames();
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAllCategoryBy();
    }

    public List<CategoryDTO> getAllCategoriesDTO(){return categoryRepository.findAllCategoryDTO();}
}
