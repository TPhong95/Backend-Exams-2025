package com.groupa.chickendirectfarm.customerAddress;

import com.groupa.chickendirectfarm.customer.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAddressService {
   private final CustomerAddressRepo customerAddressRepo;
    private final CustomerService customerService;

    public CustomerAddressService(CustomerAddressRepo customerAddressRepo, CustomerService customerService) {
        this.customerAddressRepo = customerAddressRepo;
        this.customerService = customerService;
    }

    public CustomerAddress save(CustomerAddressDto customerAddressDto){
        var customerId = customerService.getCustomerById(customerAddressDto.customerId());
        var newCustomerAddress = new CustomerAddress(customerAddressDto.streetName(), customerAddressDto.phone(), customerAddressDto.email(), customerId);
        return customerAddressRepo.save(newCustomerAddress);
    }

    public CustomerAddress getCustomerAddressById(int id){
        return customerAddressRepo.findById(id).orElse(null);
    }

    public List<CustomerAddress> getAllCustomerAddresses(){
        return customerAddressRepo.findAll();
    }

    public void deleteCustomerAddressById(int id){
        customerAddressRepo.deleteById(id);
    }

}
