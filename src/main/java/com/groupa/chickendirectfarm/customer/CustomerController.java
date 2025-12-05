package com.groupa.chickendirectfarm.customer;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressDto;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.exception.notfound.CustomerAddressNotFoundException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
        var result = customerService.save(customer);
        log.info(result.toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id){
        var result = customerService.getCustomerById(id);
        log.info(result.toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<Customer>> getAllCustomers(){
        var result = customerService.getAllCustomers();
        if (result == null){throw new CustomerNotFoundException("Customers not found");}
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
        var result = customerAddressService.getCustomerAddressById(id);
        if (result == null){throw new CustomerNotFoundException("Customer address not found");}
        return ResponseEntity.ok(result);
    }
    @GetMapping("/address")
    public ResponseEntity<List<CustomerAddress>> getAllCustomerAddresses(){
        var result = customerAddressService.getAllCustomerAddresses();
        if (result == null){throw new CustomerAddressNotFoundException("Customers addresses not found");}
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteCustomerAddressById(@PathVariable int id){
        customerAddressService.deleteCustomerAddressById(id);
        return ResponseEntity.ok("Customer Address with id " + id + " was deleted");
    }


}
