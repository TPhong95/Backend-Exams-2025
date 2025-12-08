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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public CustomerResponseDto getCustomerDtoById(int id){
        Customer customer = getCustomerById(id);
        return convertToDto(customer);
    }

    //Mangler expection på all metodene i alle klasser
    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    public List<CustomerResponseDto> getAllCustomerDto(){
     List<Customer> customers = getAllCustomers();
     return customers.stream().map(this::convertToDto).toList();
    }

    public void deleteCustomerById(int id){
        Customer customer = getCustomerById(id);
        if(customer.getPurchases() == null || customer.getPurchases().isEmpty()){
            throw new CustomerHasPurchasesException("Customer with id " + id + " has purchases.");
        }
        customerRepo.deleteById(id);
    }

    private CustomerAddressResponseDto convertAddressToDto(CustomerAddress address) {
        return new CustomerAddressResponseDto(
                address.getId(),
                address.getStreetName(),
                address.getPhone(),
                address.getEmail());
    }

    private PurchaseBatchResponseDto convertBatchToDto(PurchaseBatch batch) {
        return new PurchaseBatchResponseDto(
                batch.getProduct().getBreed(). toString(),
                batch.getQuantity(),
                batch.getProduct().getPrice(),
                batch.getTotalPrice()
        );
    }

    private PurchaseResponseDto convertPurchaseToDto(Purchase purchase){
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

    private CustomerResponseDto convertToDto(Customer customer) {
        List<CustomerAddressResponseDto> addresses = customer.getCustomerAddresses()
                .stream()
                .map(this::convertAddressToDto)
                .toList();

        List<PurchaseResponseDto> purchaseHistory = customer.getPurchases()
                .stream()
                .map(this::convertPurchaseToDto)
                .toList();

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
