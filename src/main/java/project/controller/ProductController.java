package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.exception.ProductAlreadyExistsException;
import project.model.DTOS.CategoryDTO;
import project.model.DTOS.ProductDTO;
import project.service.specification.CategoryService;
import project.service.specification.ProductService;

import javax.persistence.NoResultException;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/browse")
    public String browse(Model model) {
        try {
            List<ProductDTO> allProducts = productService.getAllProducts();
            model.addAttribute("productList", allProducts);
        } catch (NullPointerException e) {
            return "error";
        }
        return "browse";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createProductForm(@ModelAttribute ProductDTO productDTO,
                                    Model model) {

        model.addAttribute("product", productDTO);

        return "create-product";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createProduct(@ModelAttribute ProductDTO productDTO,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        try {
            productService.add(productDTO);
        } catch (ProductAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("productExists", "Product already exists!");
            return "redirect:/products/create";

        }
        return "redirect:/products/browse";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/remove/{id}")
    public String removeProduct(@PathVariable("id") long id) {
        try {
            productService.removeProductById(id);
        } catch (NullPointerException e) {
            return "error";
        }
        return "redirect:/products/browse";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") long id,
                                  Model model) {

        ProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);

        return "edit-product";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") long id,
                              @ModelAttribute ProductDTO productDTO,
                              RedirectAttributes redirectAttributes) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryByName(productDTO.getCategoryName());
            productService.editProduct(id, productDTO, categoryDTO.getId());
        } catch (NullPointerException | NoResultException e) {
            redirectAttributes.addFlashAttribute("invalidCategoryName", "Category name does not exist!");

            return "redirect:/products/edit/{id}";
        }
        return "redirect:/products/browse";
    }
}
