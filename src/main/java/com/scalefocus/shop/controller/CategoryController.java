package com.scalefocus.shop.controller;

import com.scalefocus.shop.model.entity.Category;
import com.scalefocus.shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <b>Category controller used to handle all logic that is category-relevant.</b>
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Show a list of all categories.
     *
     * @param model model attributes
     * @return list of categories
     */
    @GetMapping(value = "/all")
    public String showCategoryList(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());

        return "category-list";
    }

    /**
     * Show 'create category' form.
     *
     * @param model model attributes
     * @return 'create category' view
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        if (!model.containsAttribute("category"))
            model.addAttribute("category", new Category());

        return "create-category";
    }

    /**
     * Create category.
     *
     * @param category      the category to be created
     * @param bindingResult contains errors from form validation (if any)
     * @return list of the categories
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult) {
        categoryService.createCategory(category, bindingResult);

        return "redirect:/category/all";
    }

    /**
     * Show products per selected category.
     *
     * @param categoryId id of the category
     * @param model      model attributes
     * @return list of the categories
     */
    @PostMapping(value = "/showProducts")
    public String showProductsPerCategory(@RequestParam("categoryId") String categoryId,
                                          Model model) {
        model.addAttribute("products", categoryService.getAllProductsFromCategory(categoryId));

        return "products-per-category";
    }

    /**
     * Remove selected category.
     *
     * @param id of the category
     * @return list of the categories
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping(value = "/remove/{id}")
    public String removeCategory(@PathVariable("id") long id) {
        categoryService.removeCategory(id);

        return "redirect:/category/all";
    }

    /**
     * Show 'edit category' form.
     *
     * @param id    of the category
     * @param model model attributes
     * @return 'edit-category' view
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping(value = "/edit/{id}")
    public String editCategoryForm(@PathVariable("id") long id,
                                   Model model) {
        model.addAttribute("category", categoryService.getCategory(id));

        return "edit-category";
    }

    /**
     * Edit category.
     *
     * @param category      the selected category
     * @param bindingResult contains errors from form validation (if any)
     * @return list of the categories
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping(value = "/edit/{id}")
    public String editCategory(@Valid @ModelAttribute Category category,
                               BindingResult bindingResult) {
        categoryService.editCategory(category, bindingResult);

        return "redirect:/category/all";
    }
}
