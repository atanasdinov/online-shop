package com.scalefocus.shop.exception;

import com.scalefocus.shop.enumeration.CategoryAction;
import com.scalefocus.shop.model.entity.Category;
import org.springframework.validation.BindingResult;

/**
 * <b>This exception is used to indicate errors in "edit category" form's data.</b>
 */
public class InvalidCategoryNameException extends RuntimeException{
    private Category category;
    private BindingResult bindingResult;
    private CategoryAction categoryAction;

    public InvalidCategoryNameException(String message, Category category, BindingResult bindingResult, CategoryAction categoryAction) {
        super(message);
        this.category = category;
        this.bindingResult = bindingResult;
        this.categoryAction = categoryAction;
    }

    public Category getCategory() {
        return category;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public CategoryAction getCategoryAction() {
        return categoryAction;
    }
}
