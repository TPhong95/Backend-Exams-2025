package com.groupa.chickendirectfarm.dtomappers;

import com.groupa.chickendirectfarm.customer.Customer;
import com.groupa.chickendirectfarm.customer.address.CustomerAddress;
import com.groupa.chickendirectfarm.dto.CustomerAddressForCustomerDto;
import com.groupa.chickendirectfarm.dto.CustomerAddressResponseDto;
import com.groupa.chickendirectfarm.dto.CustomerResponseDto;
import com.groupa.chickendirectfarm.dto.PurchaseResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CustomerDtoMapper {
    private final PurchaseDtoMapper purchaseDtoMapper;

    public CustomerDtoMapper(PurchaseDtoMapper purchaseDtoMapper) {
        this.purchaseDtoMapper = purchaseDtoMapper;
    }

    // ==================== CUSTOMER ====================
    public CustomerResponseDto toCustomerDto(Customer customer) {
        log.debug("Converting customer with Id {} to DTO", customer.getId());

        List<CustomerAddressForCustomerDto> addresses = customer.getCustomerAddresses()
                .stream()
                .map(this::toCustomerAddressDtoSimple)
                .toList();

        List<PurchaseResponseDto> purchaseHistory = customer.getPurchases()
                .stream()
                .map(purchaseDtoMapper::toPurchaseDto)
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
    public CustomerAddressForCustomerDto toCustomerAddressDtoSimple(CustomerAddress address) {
        log.debug("Converting address with id {} to DTO", address.getId());

        return new CustomerAddressForCustomerDto(
                address.getId(),
                address.getStreetName(),
                address.getPhone(),
                address.getEmail()
        );
    }

    public CustomerAddressResponseDto toCustomerAddressDtoFull(CustomerAddress address) {
        log.debug("Converting address with id {} to full DTO with purchases", address.getId());

        List<PurchaseResponseDto> purchaseHistory = address.getPurchases()
                .stream()
                .map(purchaseDtoMapper::toPurchaseDto)
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


}
