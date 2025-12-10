package com.groupa.chickendirectfarm.customer;

import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.dto.CustomerAddressResponseDto;
import com.groupa.chickendirectfarm.dto.CustomerResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseBatchResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseResponseDto;
import com.groupa.chickendirectfarm.exception.conflict.CustomerHasPurchasesException;
import com.groupa.chickendirectfarm.exception.conflict.CustomerAlreadyExistException;
import com.groupa.chickendirectfarm.exception.notfound.CustomerNotFoundException;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public CustomerResponseDto getCustomerDtoById(int id){
        log.debug("Retrieving Customer DTO with ID: {}", id);
        Customer customer = getCustomerById(id);
        log.debug("Converting customer with Id {} to DTO", id);
        return convertToDto(customer);
    }

    public List<Customer> getAllCustomers(){
        log.debug("Retrieving all Customers");
        List<Customer> customers = customerRepo.findAll();
        log.debug("Retrieved {} customers", customers.size());
        return customers;
    }

    public List<CustomerResponseDto> getAllCustomerDto(){
        log.debug("Retrieving all Customers");
     List<Customer> customers = getAllCustomers();
     List<CustomerResponseDto> dtos = customers.stream().map(this::convertToDto).toList();
     log.debug("Converting {} customers to DTO", dtos.size());
     return dtos;
    }

    public void deleteCustomerById(int id){
        log.info("ENTRY: Deleting Customer with ID: {}", id);

        Customer customer = getCustomerById(id);

        if(customer.getPurchases() == null || customer.getPurchases().isEmpty()){
            log.warn("Delete failed, customer with ID {} has purchases", id);
            throw new CustomerHasPurchasesException("Customer with id " + id + " has purchases.");
        }
        customerRepo.deleteById(id);
        log.info("EXIT: Customer successfully deleted with ID: {}, name{}", id, customer.getName());
    }

    private CustomerAddressResponseDto convertAddressToDto(CustomerAddress address) {
        log.debug("Converting address with id {} to DTO", address.getId());
        return new CustomerAddressResponseDto(
                address.getId(),
                address.getStreetName(),
                address.getPhone(),
                address.getEmail());
    }

    private PurchaseBatchResponseDto convertBatchToDto(PurchaseBatch batch) {
        log.debug("Converting batch with id {} to DTO", batch.getId());
        return new PurchaseBatchResponseDto(
                batch.getProduct().getBreed(). toString(),
                batch.getQuantity(),
                batch.getProduct().getPrice(),
                batch.getBatchPrice()
        );
    }

    private PurchaseResponseDto convertPurchaseToDto(Purchase purchase){
        log.debug("Converting purchase with id {} to DTO", purchase.getId());
        List<PurchaseBatchResponseDto> batches = purchase.getPurchaseBatches()
                .stream()
                .map(this::convertBatchToDto)
                .toList();

        String shippedStatus;
        if (purchase.getPurchaseEvents().isEmpty()) {
            shippedStatus = "UNKNOWN";
        } else{
            shippedStatus = purchase.getPurchaseEvents().getFirst().getShippedStatus().toString();
        }

        LocalDateTime orderDate;
        if (purchase.getPurchaseEvents().isEmpty()) {
            orderDate = null;
        } else {
            orderDate = purchase.getPurchaseEvents().getLast().getTimestamp();
        }

        return new PurchaseResponseDto(
                purchase.getId(),
                batches,
                purchase.getTotalQuantity(),
                purchase. getTotalPrice(),
                purchase. getShippingCharge(),
                shippedStatus,
                purchase.getCustomerAddress().getStreetName(),
                orderDate
        );
    }

    public CustomerResponseDto convertToDto(Customer customer) {
        log.debug("Converting customer with Id {} to detailed DTO", customer.getId());

        List<CustomerAddressResponseDto> addresses = customer.getCustomerAddresses()
                .stream()
                .map(this::convertAddressToDto)
                .toList();

        List<PurchaseResponseDto> purchaseHistory = customer.getPurchases()
                .stream()
                .map(this::convertPurchaseToDto)
                .toList();

        log.debug("Customer DTO conversion completed, {} addresses, {} purchases converted",
                addresses.size(), purchaseHistory.size());

        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getPrimaryPhone(),
                customer.getPrimaryEmail(),
                addresses,
                purchaseHistory
        );

    }

}
