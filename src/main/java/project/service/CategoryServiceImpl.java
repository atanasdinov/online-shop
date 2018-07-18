package project.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.model.DTOS.CategoryDTO;
import project.model.entities.Category;
import project.repositories.CategoryRepository;
import project.utils.ModelParser;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    private ModelParser modelParser;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(ModelParser modelParser, CategoryRepository categoryRepository) {
        this.modelParser = modelParser;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void create(CategoryDTO category) {
        Category entity = this.modelParser.convert(category, Category.class);
        categoryRepository.persist(entity);
    }

    @Override
    public CategoryDTO get(String name) {
        Category category = categoryRepository.get(name);
        return this.modelParser.convert(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> all() {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        List<Category> all = this.categoryRepository.all();
        for (Category category : all) {
            categoryDTOS.add(this.modelParser.convert(category,CategoryDTO.class));
        }
        return categoryDTOS;
    }
}