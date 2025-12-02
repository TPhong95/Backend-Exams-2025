package com.groupa.chickendirectfarm.customer;
import com.groupa.chickendirectfarm.customerAddress.CustomerAddress;
import com.groupa.chickendirectfarm.customerAddress.CustomerAddressDto;
import com.groupa.chickendirectfarm.customerAddress.CustomerAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")

public class CustomerController {
    private final CustomerService customerService;
    private final CustomerAddressService customerAddressService;
    public CustomerController(CustomerService customerService, CustomerAddressService customerAddressService) {
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
    }

    @PostMapping()
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok(customerService.save(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable int id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer with id " + id + " was deleted");
    }

    @PostMapping("/address")
    public ResponseEntity<CustomerAddress> saveCustomerAddress(@RequestBody CustomerAddressDto customerAddressDto){
        return ResponseEntity.ok(customerAddressService.save(customerAddressDto));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<CustomerAddress> getCustomerAddressById(@PathVariable int id){
        return ResponseEntity.ok(customerAddressService.getCustomerAddressById(id));
    }
    @GetMapping("/address")
    public ResponseEntity<List<CustomerAddress>> getAllCustomerAddresses(){
        return ResponseEntity.ok(customerAddressService.getAllCustomerAddresses());
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteCustomerAddressById(@PathVariable int id){
        customerAddressService.deleteCustomerAddressById(id);
        return ResponseEntity.ok("Customer Address with id " + id + " was deleted");
    }


}
