package com.groupa.chickendirectfarm.customer;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.dto.CustomerAddressCreateDto;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.CustomerResponseDto;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<CustomerResponseDto> saveCustomer(@RequestBody Customer customer){
        Customer saved = customerService.save(customer);
        return ResponseEntity.ok(customerService.convertToDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable int id){
        var result = customerService.getCustomerDtoById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers(){
        var result = customerService.getAllCustomers();
        if (result.isEmpty()) {return ResponseEntity.noContent().build();}
        return ResponseEntity.ok(customerService.getAllCustomerDto());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable int id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer with id " + id + " was deleted");
    }

    @PostMapping("/address")
    public ResponseEntity<CustomerAddress> saveCustomerAddress(@RequestBody CustomerAddressCreateDto customerAddressCreateDto){
        return ResponseEntity.ok(customerAddressService.save(customerAddressCreateDto));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<CustomerAddress> getCustomerAddressById(@PathVariable int id){
        var result = customerAddressService.getCustomerAddressById(id);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/address")
    public ResponseEntity<List<CustomerAddress>> getAllCustomerAddresses(){
        var result = customerAddressService.getAllCustomerAddresses();
        if (result.isEmpty()) {return ResponseEntity.notFound().build();}
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteCustomerAddressById(@PathVariable int id){
        customerAddressService.deleteCustomerAddressById(id);
        return ResponseEntity.ok("Customer Address with id " + id + " was deleted");
    }


}
