package com.groupa.chickendirectfarm.customer.address;

import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.exception.alreadyexist.CustomerAddressAlreadyExistException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerAddressNotFoundException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerNotFoundException;
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
        if (customerAddressRepo.existsByStreetName(newCustomerAddress.getStreetName())){
            throw new CustomerAddressAlreadyExistException("Address already exists");
        }
        return customerAddressRepo.save(newCustomerAddress);
    }

    public CustomerAddress getCustomerAddressById(int id){
        return customerAddressRepo.findById(id).orElseThrow(()  -> new CustomerAddressNotFoundException("Customer address with id " + id + " not found"));
    }

    public List<CustomerAddress> getAllCustomerAddresses(){
        return customerAddressRepo.findAll();
    }

    public void deleteCustomerAddressById(int id){
        if (!customerAddressRepo.existsById(id)){
            throw new CustomerAddressNotFoundException("Customer address with id " + id + " not found");
        }
        customerAddressRepo.deleteById(id);
    }

}
