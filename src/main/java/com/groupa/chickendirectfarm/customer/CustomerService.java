package com.groupa.chickendirectfarm.customer;

import com.groupa.chickendirectfarm.exception.conflict.CustomerHasPurchasesException;
import com.groupa.chickendirectfarm.exception.conflict.CustomerAlreadyExistException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class CustomerService {
    private final CustomerRepo customerRepo;
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer save(Customer customer){
        log.info("ENTRY: Creating new Customer with customer Id: {}, Name {}, Phone: {}, Email: {}",
                customer.getId(), customer.getName(), customer.getPrimaryPhone(), customer.getPrimaryEmail());

        if(customerRepo.existsByPrimaryEmail(customer.getPrimaryEmail()) || customerRepo.existsByPrimaryPhone(customer.getPrimaryPhone())){
            log.warn("Customer creation failed, Duplicate phone number or email address exists");
            throw new CustomerAlreadyExistException("A customer with the email " + customer.getPrimaryEmail() + " or phone number: "+ customer.getPrimaryPhone() + " already exists.");
        }

        Customer savedCustomer = customerRepo.save(customer);
        log.info("EXIT: Customer successfully created with ID: {} and name: {}", savedCustomer.getId(), savedCustomer.getName());
        return savedCustomer;
    }

    public Customer getCustomerById(int id){
        log.debug("Retrieving Customer with ID: {}", id);
        return customerRepo.findById(id).orElseThrow(()  -> {
            log.warn("Customer not found with ID: {}", id);
            return new CustomerNotFoundException("Customer with id " + id + " not found");
        });
    }

    public List<Customer> getAllCustomers(){
        log.debug("Retrieving all Customers");
        List<Customer> customers = customerRepo.findAll();
        log.debug("Retrieved {} customers", customers.size());
        return customers;
    }


    public void deleteCustomerById(int id){
        log.info("ENTRY: Deleting Customer with ID: {}", id);

        if (!customerRepo.existsById(id)){
            log.warn("Delete failed, customer with Id: {} not found", id);
            throw new CustomerNotFoundException("Customer with id " + id + " not found");
        }

        Customer customer = getCustomerById(id);

        if(!customer.getPurchases().isEmpty()){
            log.warn("Delete failed, customer with ID {} has purchases", id);
            throw new CustomerHasPurchasesException("Customer with id " + id + " has purchases.");
        }
        customerRepo.deleteById(id);
        log.info("EXIT: Customer successfully deleted with ID: {}, name{}", id, customer.getName());
    }

}
