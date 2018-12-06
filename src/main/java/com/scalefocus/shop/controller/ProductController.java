package com.scalefocus.shop.controller;

import com.scalefocus.shop.model.entity.Product;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


/**
 * <b>Product controller used to handle all logic that is products related.</b>
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Returns a logical view of the ScaleShop's "browse" page..
     *
     * @param model      model attributes
     * @param pageNumber current page number
     * @return the logical name of the view
     */
    @GetMapping("/browse/{pageNumber}")
    public String browse(@PathVariable("pageNumber") int pageNumber,
                         Model model) {
        model.addAttribute("pagedProducts", productService.getPagedProducts(pageNumber));

        return "browse";
    }

    /**
     * Returns a logical view of the ScaleShop's "create product" form/page.
     *
     * @param model model attributes
     * @return the logical name of the view
     */
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @GetMapping("/create")
    public String createProductForm(Model model) {
        if (!model.containsAttribute("product"))
            model.addAttribute("product", new Product());

        return "create-product";
    }

    /**
     * Submits a filled form with data for the {@link Product}.
     *
     * @param product product's data
     * @return the logical name of the view
     */
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult) {
        productService.addProduct(product, bindingResult);

        return "redirect:/products/browse/1";
    }

    /**
     * Removes a {@link Product} from the database by a given id.
     *
     * @param id the product's id
     * @return the logical name of the view
     */
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @PostMapping("/remove/{id}")
    public String removeProduct(@PathVariable("id") long id) {
        productService.removeProduct(id);

        return "redirect:/products/browse/1";
    }

    /**
     * Returns a logical view of the ScaleShop's "edit product" form/page..
     *
     * @param model model attributes
     * @param id    product's id
     * @return the logical name of the view
     */
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") long id,
                                  Model model) {
        model.addAttribute("product", productService.getProduct(id));

        return "edit-product";
    }

    /**
     * Submits a filled "edit product" form.
     *
     * @param productData product's data/properties.
     * @return the logical name of the view.
     */
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @PostMapping("/edit/{id}")
    public String editProduct(@Valid @ModelAttribute Product productData,
                              BindingResult bindingResult) {
        productService.editProduct(productData, bindingResult);

        return "redirect:/products/browse/1";
    }

    /**
     * Submits a filled form with a comment for a given {@link Product}.
     *
     * @param message   the comment message.
     * @param productId the product's id.
     * @param principal the current logged-in {@link User}.
     * @return the logical name of the view.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/comment/send")
    public String sendComment(@RequestParam("message") String message,
                              @RequestParam("productId") String productId,
                              Principal principal) {
        productService.addComment(principal.getName(), message, productId);

        return "redirect:/products/view/" + productId;
    }

    /**
     * Submits a delete request for a given product's comment.
     *
     * @param commentId the comment's id.
     * @param productId the product's id.
     * @return the logical name of the view.
     */
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") long commentId,
                                @RequestParam("productId") String productId) {
        productService.deleteComment(commentId, Long.parseLong(productId));

        return "redirect:/products/view/" + productId;
    }

    /**
     * Returns the given {@link Product} page with information and comments.
     *
     * @param id    the product's id.
     * @param model model attributes.
     * @return the logical name of the view.
     */
    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable("id") long id,
                              Model model) {
        model.addAttribute("product", productService.getProduct(id));

        return "product";
    }

    /**
     * Search for a given product by name
     *
     * @param keyword keyword to search for.
     * @param model   model attributes.
     * @return the logical name of the view.
     */
    @PostMapping("/search")
    public String search(@RequestParam("keyword") String keyword,
                         Model model) {
        model.addAttribute("foundProducts", productService.getFoundProducts(keyword));

        return "search";
    }
}
