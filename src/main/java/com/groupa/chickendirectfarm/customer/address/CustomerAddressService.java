package com.groupa.chickendirectfarm.customer.address;

import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.dto.customerdtos.CustomerAddressCreateDto;
import com.groupa.chickendirectfarm.exception.notfound.CustomerAddressNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerAddressService {
   private final CustomerAddressRepo customerAddressRepo;
    private final CustomerService customerService;

    public CustomerAddressService(CustomerAddressRepo customerAddressRepo, CustomerService customerService) {
        this.customerAddressRepo = customerAddressRepo;
        this.customerService = customerService;
    }

    public CustomerAddress save(CustomerAddressCreateDto customerAddressCreateDto){
        log.info("ENTRY: Saving new customer address to customer Id: {}", customerAddressCreateDto.customerId());
        log.debug("Address details, street: {}, phone: {}, email: {}",
                customerAddressCreateDto.streetName(),
                customerAddressCreateDto.phone(),
                customerAddressCreateDto.email());

        var customer = customerService.getCustomerById(customerAddressCreateDto.customerId());
        log.debug("Customer with Id: {}, name: {} retrieved for new customer address", customer.getId(), customer.getName());

        var newCustomerAddress = new CustomerAddress(customerAddressCreateDto.streetName(), customerAddressCreateDto.phone(), customerAddressCreateDto.email(), customer);

        CustomerAddress savedCustomerAddress = customerAddressRepo.save(newCustomerAddress);
        log.info("EXIT: New address added to customer with Id: {}", savedCustomerAddress.getCustomer().getId());

        return savedCustomerAddress;
    }

    public CustomerAddress getCustomerAddressById(int id){
        log.debug("Retrieving address with Id: {}", id);
        return customerAddressRepo.findById(id).orElseThrow(()  -> {
            log.warn("Address with Id: {} not found", id);
            return new CustomerAddressNotFoundException("Customer address with id " + id + " not found");
        });
    }

    public List<CustomerAddress> getAllCustomerAddresses(){
        log.debug("Retrieving all customer addresses...");
        List<CustomerAddress> customerAddresses = customerAddressRepo.findAll();
        log.debug("Retrieved {} addresses", customerAddresses);
        return customerAddresses;
    }

    public void deleteCustomerAddressById(int id){
        log.info("ENTRY: Deleting customer address with Id: {}", id);


        if (!customerAddressRepo.existsById(id)){
            log.warn("Delete failed, customer address with Id: {} not found", id);
            throw new CustomerAddressNotFoundException("Customer address with id " + id + " not found");
        }
        CustomerAddress customerAddress = getCustomerAddressById(id);

        customerAddressRepo.deleteById(id);
        log.info("EXIT: Customer address successfully deleted with ID: {}, streetname: {}", id, customerAddress.getStreetName() );
    }

    public Page<CustomerAddress> getAllCustomerAddressesPaged (int page){
        Pageable pageable = PageRequest.of(page, 10);
        return customerAddressRepo.findAll(pageable);
    }

    public Page<CustomerAddress> getAllCustomerAddressesWithPurchases (int page){
        Pageable pageable = PageRequest.of(page, 10);
        return customerAddressRepo.findAddressesWithPurchases(pageable);
    }

}
