package com.groupa.chickendirectfarm.customer;

import com.groupa.chickendirectfarm.dto.CustomerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerViewController {
    private CustomerService customerService;

    public CustomerViewController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    public String listOfCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    @GetMapping("/{id}")
    public String viewCustomerDetails(Model model, @PathVariable int id){
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        return "customer/view";
    }
}
