package com.groupa.chickendirectfarm.customer;

import com.groupa.chickendirectfarm.exception.CustomerHasPurchasesException;
import com.groupa.chickendirectfarm.exception.alreadyexist.CustomerAlreadyExistException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer save(Customer customer){
        if(customerRepo.existsByName(customer.getName())){
            throw new CustomerAlreadyExistException("A customer with name " + customer.getName() + " already exists.");
        }
        return customerRepo.save(customer);
    }

    public Customer getCustomerById(int id){
        return customerRepo.findById(id).orElseThrow(()  -> new CustomerNotFoundException("Customer with id " + id + " not found"));
    }

    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    public void deleteCustomerById(int id){
        Customer customer = getCustomerById(id);
        if(customer.getPurchases() == null || customer.getPurchases().isEmpty()){
            throw new CustomerHasPurchasesException("Customer with id " + id + " has purchases.");
        }
        customerRepo.deleteById(id);
    }
}
