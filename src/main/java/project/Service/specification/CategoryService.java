package project.service.specification;

import project.model.DTOS.CategoryDTO;
import project.model.entities.Category;

import java.util.List;

public interface CategoryService {

    void create(CategoryDTO category);

    CategoryDTO getCategoryByName(String name);

    List<CategoryDTO> getAllCategories();
}