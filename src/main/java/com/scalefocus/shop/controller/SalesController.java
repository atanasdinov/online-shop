package com.scalefocus.shop.controller;

import com.scalefocus.shop.model.entity.Cart;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * <b>Sales controller used to handle all logic that is sales related.</b>
 */
@Controller
@RequestMapping("/sales")
public class SalesController {

    private SaleService saleService;

    @Autowired
    public SalesController(SaleService saleService) {
        this.saleService = saleService;
    }

    /**
     * Returns the "purchase history" page to see all completed purchases.
     *
     * @param model model attributes.
     * @return the logical name of the view.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/purchase")
    public String getPurchaseHistory(Model model) {
        model.addAttribute("sales", saleService.getAllSales());

        return "purchase-history";
    }

    /**
     * This method is used to get the data for a given purchase (i.e. a cart),
     * convert it to a ProductMessageDTO and send it via JmsTemplate over ActiveMQ
     * queue to "ScaleChat" project.
     *
     * @param purchasedProducts a {@link Cart} object containing all data for a given purchase
     * @param requestedQuantity the quantity that is entered by a user for a given product
     * @param principal         the current logged-in {@link User}
     * @return the logical name of the view
     */
    @PreAuthorize("hasAnyRole('USER','MODERATOR')")
    @PostMapping("/purchase")
    @ResponseBody
    public String purchase(@ModelAttribute Cart purchasedProducts,
                           @RequestParam String[] requestedQuantity,
                           Principal principal) {
        saleService.addSale(principal.getName(), purchasedProducts, requestedQuantity);

        return ResponseEntity.status(HttpStatus.OK).build().getStatusCode().toString();
    }

}
