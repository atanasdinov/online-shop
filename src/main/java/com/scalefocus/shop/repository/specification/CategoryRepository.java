package com.scalefocus.shop.repository.specification;

import com.scalefocus.shop.model.entity.Category;

import java.util.List;


/**
 * <b>This interface declares manipulations over the {@link Category} data in the database.</b>
 */
public interface CategoryRepository {

    /**
     * Create a {@link Category}.
     *
     * @param category contain category data.
     */
    void createCategory(Category category);

    /**
     * Get a {@link Category} by specific criteria.
     *
     * @param name the criteria to be found.
     * @return {@link Category}
     */
    Category getCategory(String name);

    /**
     * Get a {@link Category} by specific criteria.
     *
     * @param id the criteria to be found.
     * @return {@link Category}
     */
    Category getCategory(Long id);

    /**
     * Check if such category exist by specific criteria.
     *
     * @param name the criteria to be found.
     * @return whether is found.
     */
    boolean categoryExists(String name);

    /**
     * This method is used to get all categories from the database.
     *
     * @return list of categories
     */
    List<Category> getAllCategories();

    /**
     * This method deletes category from the database.
     *
     * @param categoryId id of the category
     */
    void deleteCategory(Long categoryId);

    /**
     * This method is used to edit category.
     *
     * @param category to be edited
     */
    void editCategory(Category category);
}