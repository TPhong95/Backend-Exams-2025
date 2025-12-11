package com.groupa.chickendirectfarm.dtomappers;

import com.groupa.chickendirectfarm.dto.ProductEventResponseDto;
import com.groupa.chickendirectfarm.dto.ProductResponseDto;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.event.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductDtoMapper {

    // ==================== PRODUCT ====================

    public ProductResponseDto toProductDto(Product product) {
        log.debug("Converting Product with Id {} to DTO", product.getId());

        List<ProductEventResponseDto> productEvents = product.getProductEvents()
                .stream()
                .map(this::toProductEventDto)
                .toList();

        log.debug("Product DTO conversion completed, {} events converted",
                productEvents.size());

        return new ProductResponseDto(
                product.getId(),
                product.getBreed(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                productEvents
        );
    }

    // ==================== PRODUCTEVENTS ====================

    public ProductEventResponseDto toProductEventDto(ProductEvent event) {
        log.debug("Converting event with id {} to simple DTO", event.getId());

        return new ProductEventResponseDto(
                event.getId(),
                event.getStockStatus(),
                event.getPreviousQuantity(),
                event.getIncomingQuantity(),
                event.getNewQuantity(),
                event.getProductEventAction(),
                event.getTimestamp()
        );
    }
}
