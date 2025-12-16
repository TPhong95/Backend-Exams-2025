package com.groupa.chickendirectfarm.customer;

import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerViewController {
    private final CustomerAddressService customerAddressService;
    private final CustomerService customerService;

    public CustomerViewController(CustomerService customerService, CustomerAddressService customerAddressService) {
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
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

    @GetMapping("/address")
    public String listOfAddresses(Model model){
        List<CustomerAddress> addresses = customerAddressService.getAllCustomerAddresses();
        model.addAttribute("addresses", addresses);
        return "address/list";
    }

    @GetMapping("/address/{id}")
    public String viewAddressDetails(Model model,  @PathVariable int id){
        CustomerAddress address = customerAddressService.getCustomerAddressById(id);
        model.addAttribute("address", address);
        return "address/view";
    }


}
