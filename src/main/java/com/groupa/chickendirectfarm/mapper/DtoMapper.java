package com.groupa.chickendirectfarm.mapper;


import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.dto.*;
import com.groupa.chickendirectfarm.purchase.Purchase;
import com.groupa.chickendirectfarm.purchase.batch.PurchaseBatch;
import jakarta.persistence.OrderBy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class DtoMapper {

    // ==================== CUSTOMER ====================
    public CustomerResponseDto toCustomerDto(Customer customer) {
        log.debug("Converting customer with Id {} to DTO", customer.getId());

        List<CustomerAddressForCustomerDto> addresses = customer.getCustomerAddresses()
                .stream()
                .map(this::toCustomerAddressDtoSimple)
                .toList();

        List<PurchaseResponseDto> purchaseHistory = customer.getPurchases()
                .stream()
                .map(this::toPurchaseDto)
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

    // ==================== CUSTOMER ADDRESS ====================

    // Simple version - uden purchases (brukes i Customer DTO for å unngå circular ref)
    public CustomerAddressForCustomerDto toCustomerAddressDtoSimple(CustomerAddress address) {
        log.debug("Converting address with id {} to simple DTO", address.getId());

        return new CustomerAddressForCustomerDto(
                address.getId(),
                address.getStreetName(),
                address.getPhone(),
                address.getEmail()
        );
    }

    // Full version - med purchases (brukes når address hentes direkte)
    public CustomerAddressResponseDto toCustomerAddressDtoFull(CustomerAddress address) {
        log.debug("Converting address with id {} to full DTO with purchases", address.getId());

        List<PurchaseResponseDto> purchaseHistory = address. getPurchases()
                .stream()
                .map(this:: toPurchaseDto)
                .toList();

        return new CustomerAddressResponseDto(
                address.getId(),
                address.getStreetName(),
                address.getPhone(),
                address.getEmail(),
                address.getCustomer().getId(),
                address.getCustomer().getName(),
                purchaseHistory
        );
    }

    // ==================== PURCHASE BATCH ====================

    public PurchaseBatchResponseDto toPurchaseBatchDto(PurchaseBatch batch) {
        log.debug("Converting batch with id {} to DTO", batch.getId());

        return new PurchaseBatchResponseDto(
                batch.getProduct().getBreed().toString(),
                batch.getQuantity(),
                batch.getProduct().getPrice(),
                batch.getBatchPrice()
        );
    }

    // ==================== PURCHASE ====================

    public PurchaseResponseDto toPurchaseDto(Purchase purchase) {
        log.debug("Converting purchase with id {} to DTO", purchase.getId());

        List<PurchaseBatchResponseDto> batches = purchase. getPurchaseBatches()
                .stream()
                .map(this::toPurchaseBatchDto)
                .toList();

        String shippedStatus;
        if (purchase.getPurchaseEvents().isEmpty()) {
            shippedStatus = "UNKNOWN";
        } else {
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
                purchase.getShippingCharge(),
                purchase. getTotalPrice(),
                shippedStatus,
                purchase.getCustomerAddress().getStreetName(),
                orderDate
        );
    }

    public PurchaseDetailsResponseDto toPurchaseDetailsDto(Purchase purchase) {
        log.debug("Converting purchase with id {} to detailed DTO", purchase.getId());

        List<PurchaseBatchResponseDto> batches = purchase.getPurchaseBatches()
                .stream()
                .map(this::toPurchaseBatchDto)
                .toList();

        List<PurchaseStatusHistoryDto> statusHistory = purchase.getPurchaseEvents()
                .stream()
                .map(event -> new PurchaseStatusHistoryDto(
                        event.getShippedStatus().toString(),
                        event.getTimestamp()
                ))
                .toList();

        String currentStatus;
        if (purchase.getPurchaseEvents().isEmpty()) {
            currentStatus = "UNKNOWN";
        } else {
            currentStatus = purchase.getPurchaseEvents().getFirst().getShippedStatus(). toString();
        }

        LocalDateTime orderDate;
        if (purchase.getPurchaseEvents().isEmpty()) {
            orderDate = null;
        } else {
            orderDate = purchase.getPurchaseEvents().getLast().getTimestamp();
        }

        log.debug("Purchase DTO conversion completed, {} batches, {} status events converted", batches.size(), statusHistory.size());

        return new PurchaseDetailsResponseDto(
                purchase. getId(),
                orderDate,
                currentStatus,
                purchase. getCustomer().getName(),
                purchase.getCustomer().getPrimaryPhone(),
                purchase.getCustomer().getPrimaryEmail(),
                toCustomerAddressDtoSimple(purchase.getCustomerAddress()),
                batches,
                statusHistory,
                purchase.getTotalQuantity(),
                purchase.getShippingCharge(),
                purchase.getTotalPrice()
        );
    }

}
