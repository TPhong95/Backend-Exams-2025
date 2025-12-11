package com.groupa.chickendirectfarm.customer;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.dto.CustomerAddressCreateDto;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.dto.CustomerAddressResponseDto;
import com.groupa.chickendirectfarm.dto.CustomerResponseDto;
import com.groupa.chickendirectfarm.dtomappers.CustomerDtoMapper;
import com.groupa.chickendirectfarm.dtomappers.DtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")

public class CustomerController {
    private final CustomerService customerService;
    private final CustomerAddressService customerAddressService;
    private final CustomerDtoMapper customerDtoMapper;

    public CustomerController(CustomerService customerService, CustomerAddressService customerAddressService, DtoMapper dtoMapper, CustomerDtoMapper customerDtoMapper) {
        this.customerService = customerService;
        this.customerAddressService = customerAddressService;
        this.customerDtoMapper = customerDtoMapper;
    }


    @PostMapping()
    public ResponseEntity<CustomerResponseDto> saveCustomer(@RequestBody Customer customer){
        Customer result = customerService.save(customer);
        return ResponseEntity.ok(customerDtoMapper.toCustomerDto(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable int id){
        var result = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDtoMapper.toCustomerDto(result));
    }

    @GetMapping()
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<CustomerResponseDto> dtos = customers.stream()
                .map(customerDtoMapper::toCustomerDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable int id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer with id " + id + " was deleted");
    }

    @PostMapping("/address")
    public ResponseEntity<CustomerAddressResponseDto> saveCustomerAddress(@RequestBody CustomerAddressCreateDto customerAddressCreateDto){
        CustomerAddress result = customerAddressService.save(customerAddressCreateDto);
        return ResponseEntity.ok(customerDtoMapper.toCustomerAddressDtoFull(result));
    }


    @GetMapping("/address/{id}")
    public ResponseEntity<CustomerAddressResponseDto> getCustomerAddressById(@PathVariable int id){
        CustomerAddress result = customerAddressService.getCustomerAddressById(id);
        return ResponseEntity.ok(customerDtoMapper.toCustomerAddressDtoFull(result));
    }


    @GetMapping("/address")
    public ResponseEntity<List<CustomerAddressResponseDto>> getAllCustomerAddresses(){
        List<CustomerAddress> addresses = customerAddressService.getAllCustomerAddresses();
        if (addresses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CustomerAddressResponseDto> dtos = addresses. stream()
                .map(customerDtoMapper::toCustomerAddressDtoFull)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteCustomerAddressById(@PathVariable int id){
        customerAddressService.deleteCustomerAddressById(id);
        return ResponseEntity.ok("Customer Address with id " + id + " was deleted");
    }


}
