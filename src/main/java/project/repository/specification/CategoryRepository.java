package project.repository.specification;

import project.model.entities.Category;

import java.util.List;


public interface CategoryRepository {

    void persist(Category category);

    Category getCategory(String name);

    List<Category> getAllCategories();

    boolean doesExist(String categoryName);
}