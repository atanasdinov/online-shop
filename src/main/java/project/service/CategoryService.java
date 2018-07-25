package project.Service;

import project.model.DTOS.CategoryDTO;

import java.util.List;

public interface CategoryService {
    void create(CategoryDTO category);

    CategoryDTO get(String name);

    List<CategoryDTO> all();
}