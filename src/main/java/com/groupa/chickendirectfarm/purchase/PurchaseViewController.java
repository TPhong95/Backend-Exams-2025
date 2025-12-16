package com.groupa.chickendirectfarm.purchase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseViewController {

    private final PurchaseService purchaseService;

    public PurchaseViewController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping()
    public String listOfPurchases(Model model) {
        List<Purchase> purchases = purchaseService.getAllPurchases();
        model.addAttribute("purchases", purchases);
        return "purchase/list";
    }

    @GetMapping("/{id}")
    public String viewPurchaseDetails(Model model, @PathVariable int id) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        model.addAttribute("purchase", purchase);
        return "purchase/view";
    }

    @GetMapping("/receipt/{id}")
    public String viewPurchaseReceipt(Model model, @PathVariable int id) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        model.addAttribute("purchase", purchase);
        return "purchase/receipt";
    }

}
