package com.scalefocus.shop.service;

import com.scalefocus.shop.enumeration.CategoryAction;
import com.scalefocus.shop.exception.CategoryAlreadyExistsException;
import com.scalefocus.shop.exception.InvalidCategoryNameException;
import com.scalefocus.shop.model.entity.Category;
import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.repository.specification.CategoryRepository;
import com.scalefocus.shop.repository.specification.ProductRepository;
import com.scalefocus.shop.service.sftp.SftpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

/**
 * <b>This service declares all manipulations that can be done to a {@link Category}</b>
 */
@Service
@Transactional
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private SftpClient sftpClient;

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public CategoryService(SftpClient sftpClient, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.sftpClient = sftpClient;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * This method creates the default category 'misc'.
     */
    @Bean
    @DependsOn(value = "tableCreation")
    public Void createDefaultCategory() {
        if (!categoryRepository.categoryExists("misc")) {
            categoryRepository.createCategory(new Category("misc"));
            logger.info("Default category created.");
        }
        return null;
    }

    /**
     * This method returns a category by id.
     *
     * @param categoryId id of the category
     * @return {@link Category} object
     */
    public Category getCategory(Long categoryId) {
        return categoryRepository.getCategory(categoryId);
    }

    /**
     * This method returns all categories in the database.
     *
     * @return list of categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    /**
     * This method is used to get all products from a given Category.
     *
     * @param categoryId the id of the category
     * @return list of products
     */
    public List<Product> getAllProductsFromCategory(String categoryId) {
        return productRepository.getAllProductsByCategory(Long.parseLong(categoryId));
    }

    /**
     * This method is used to add a category to the database.
     *
     * @param category contains category data
     */
    public void createCategory(Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidCategoryNameException("Invalid category name!", category, bindingResult, CategoryAction.CREATE);

        try {
            categoryRepository.createCategory(category);
            logger.info("Category created.");
        } catch (DataIntegrityViolationException e) {
            throw new CategoryAlreadyExistsException("Category already exists!");
        }
    }

    /**
     * This method deletes category and all products associated with it.
     *
     * @param categoryId id of the category
     */
    public void removeCategory(long categoryId) {
        List<Product> products = productRepository.getAllProductsByCategory(categoryId);

        products.forEach(product -> {
            Optional.of(product)
                    .filter(p -> !productRepository.pictureIsBoundToMoreThanOneProduct(p.getPictureName()))
                    .ifPresent(p -> {
                        sftpClient.deleteFile(p.getPictureName());
                        logger.info("Picture deleted from remote server.");
                    });

            productRepository.deleteProduct(product.getId());
            logger.info("Product deleted.");
        });
    }

    /**
     * This method edits the name of a given category if it is valid.
     *
     * @param category      the category to be edited
     * @param bindingResult contains errors from form validation (if any)
     */
    public void editCategory(Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new InvalidCategoryNameException("Invalid category name!", category, bindingResult, CategoryAction.EDIT);

        categoryRepository.editCategory(category);
        logger.info("Category edited.");
    }
}