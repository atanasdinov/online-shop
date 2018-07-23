package project.service;

import project.model.DTOS.CategoryDTO;
import project.model.entities.Category;

import java.util.List;

public interface CategoryService {
    void create(CategoryDTO category);
    CategoryDTO get(String name);
    List<CategoryDTO> all();
}