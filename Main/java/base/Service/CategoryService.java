package base.Service;

import base.Entity.Category;
import base.Entity.Client;
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

    public int getCategoryIdByName(String name){return categoryRepository.findCategoryByName(name).getCategoryId();}

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
        return categoryRepository.findAllBy();
    }



}
