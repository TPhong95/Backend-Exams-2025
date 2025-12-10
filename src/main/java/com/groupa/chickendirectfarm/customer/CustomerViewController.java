package com.groupa.chickendirectfarm.customer;

import org.springframework.ui.Model;

public class CustomerViewController {
    private CustomerService customerService;

    public CustomerViewController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public String listOfCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "customer/list";
    }
}
