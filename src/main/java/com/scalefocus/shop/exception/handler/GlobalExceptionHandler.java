package com.scalefocus.shop.exception.handler;

import com.scalefocus.shop.enumeration.CategoryAction;
import com.scalefocus.shop.enumeration.ProductAction;
import com.scalefocus.shop.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * <b>A dedicated global exception handler class for handling specific exceptions.</b>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public String unauthorized() {
        logger.error("Access denied.");
        return "error-403";
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public String handleConstraintViolation(ProductAlreadyExistsException e,
                                            RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage());
        redirectAttributes.addFlashAttribute("productExists", e.getMessage());

        return "redirect:/products/create";
    }

    @ExceptionHandler(CheckoutEmptyCartException.class)
    @ResponseBody
    public String handleCheckoutEmptyCart(CheckoutEmptyCartException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).build().getStatusCode().toString();
    }

    @ExceptionHandler(InvalidQuantityException.class)
    @ResponseBody
    public String handleInvalidQuantity(InvalidQuantityException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build().getStatusCode().toString();
    }

    @ExceptionHandler(QuantityNotAvailableException.class)
    @ResponseBody
    public String handleQuantityNotAvailable(QuantityNotAvailableException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build().getStatusCode().toString();
    }

    @ExceptionHandler(ProductAlreadyInCartException.class)
    public String handleProductAlreadyInCart(ProductAlreadyInCartException e,
                                             RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage());
        redirectAttributes.addFlashAttribute("productAlreadyInCart", e.getMessage());
        redirectAttributes.addFlashAttribute("productId", e.getProductId());

        return "redirect:/products/browse/" + e.getPageNumber();
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUsernameAlreadyExists(UsernameAlreadyExistsException e,
                                              Model model) {
        logger.error(e.getMessage());
        model.addAttribute("user", e.getUserData());
        model.addAttribute("usernameDuplication", e.getMessage());

        return "register";
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailAlreadyExists(EmailAlreadyExistsException e,
                                           Model model) {
        logger.error(e.getMessage());
        model.addAttribute("user", e.getUserData());
        model.addAttribute("emailDuplication", e.getMessage());

        return "register";
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public String handleIncorrectPassword(IncorrectPasswordException e,
                                          Model model) {
        logger.error(e.getMessage());
        model.addAttribute("incorrectPassword", e.getMessage());

        return "login";
    }

    @ExceptionHandler(AccountNotValidatedException.class)
    public String handleAccountNotValidated(AccountNotValidatedException e,
                                            Model model) {
        logger.error(e.getMessage());
        model.addAttribute("accountNotValidated", e.getMessage());

        return "login";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFound(UsernameNotFoundException e,
                                         Model model) {
        logger.error(e.getMessage());
        model.addAttribute("userNotFound", e.getMessage());

        return "login";
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public String handleCategoryNotFound(CategoryNotFoundException e,
                                         RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage());
        redirectAttributes.addFlashAttribute("invalidCategoryName", e.getMessage());

        return "redirect:/products/edit/{id}";
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public String handleInvalidUserData(InvalidUserDataException e,
                                        Model model) {
        logger.error(e.getMessage());
        model.addAttribute("org.springframework.validation.BindingResult.user", e.getBindingResult());
        model.addAttribute("user", e.getUserData());

        return "register";
    }

    @ExceptionHandler(InvalidProductDataException.class)
    public String handleInvalidProductData(InvalidProductDataException e,
                                           Model model) {
        logger.error(e.getMessage());
        model.addAttribute("org.springframework.validation.BindingResult.product", e.getBindingResult());
        model.addAttribute("product", e.getProduct());

        if (e.getProductAction() == ProductAction.CREATE)
            return "create-product";

        return "edit-product";
    }

    @ExceptionHandler(EmptyCartException.class)
    public String handleEmptyCartException(EmptyCartException e,
                                           RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage());
        redirectAttributes.addFlashAttribute("emptyCart", e.getMessage());
        return "redirect:/cart";
    }

    @ExceptionHandler(ActivationLinkExpiredException.class)
    public String handleExpiredActivationLink(ActivationLinkExpiredException e,
                                              RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage());
        redirectAttributes.addFlashAttribute("username", e.getUsername());

        return "redirect:/user/tokenExpired";
    }

    @ExceptionHandler(EmptyCommentException.class)
    public String handleEmptyComment(EmptyCommentException e,
                                     RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage());
        redirectAttributes.addFlashAttribute("emptyComment", e.getMessage());

        return "redirect:/products/view/" + e.getProductId();
    }

    @ExceptionHandler(ImageNotUploadedException.class)
    public String handleImageNotUploaded(ImageNotUploadedException e,
                                         Model model) {
        logger.error(e.getMessage());
        model.addAttribute("emptyImage", e.getMessage());
        model.addAttribute("product", e.getProduct());

        return "create-product";
    }

    @ExceptionHandler(InvalidCategoryNameException.class)
    public String handleInvalidCategoryName(InvalidCategoryNameException e,
                                            Model model) {
        logger.error(e.getMessage());
        model.addAttribute("org.springframework.validation.BindingResult.category", e.getBindingResult());
        model.addAttribute("category", e.getCategory());

        if (e.getCategoryAction() == CategoryAction.CREATE)
            return "create-category";

        return "edit-category";
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public String handleCategoryAlreadyExists(CategoryAlreadyExistsException e,
                                              RedirectAttributes redirectAttributes) {
        logger.error(e.getMessage());
        redirectAttributes.addFlashAttribute("categoryExists", e.getMessage());

        return "redirect:/category/create";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(ProductNotFoundException e) {
        logger.error(e.getMessage());

        return "redirect:/cart";
    }
}