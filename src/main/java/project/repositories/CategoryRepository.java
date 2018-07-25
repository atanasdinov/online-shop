package project.repositories;


import project.model.entities.Category;

import java.util.List;

public interface CategoryRepository {
    void persist(Category category);
    Category get(String name);
    List<Category> all();
    boolean doExist(String categoryName);
}