package com.groupa.chickendirectfarm.customer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer save(Customer customer){
        return customerRepo.save(customer);
    }

    public Customer getCustomerById(int id){
        return customerRepo.findById(id).orElseThrow();
    }

    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    public void deleteCustomerById(int id){
        customerRepo.deleteById(id);
    }
}
