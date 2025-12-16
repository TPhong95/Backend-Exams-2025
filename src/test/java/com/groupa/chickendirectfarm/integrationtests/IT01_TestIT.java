package com.groupa.chickendirectfarm.integrationtests;

import com.groupa.chickendirectfarm.customer.CustomerRepo;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressRepo;
import com.groupa.chickendirectfarm.product.Breed;
import com.groupa.chickendirectfarm.product.Product;
import com.groupa.chickendirectfarm.product.ProductRepo;
import com.groupa.chickendirectfarm.purchase.PurchaseRepo;
import com.groupa.chickendirectfarm.testdata.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;



public class IT01_TestIT extends BaseIntegrationTest {

    @Autowired
    TestData testData;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private PurchaseRepo purchaseRepo;
    @Autowired
    private CustomerAddressRepo customerAddressRepo;

    @Test
    void shouldMakeFakeData(){
        testData.createTestData();

        assertThat(productRepo.findAll()).extracting(Product::getBreed).containsExactlyInAnyOrder(Breed.BROWN, Breed.GOLDEN, Breed.BLACK, Breed.WHITE);
        assertThat(customerRepo.findAll().size()).isEqualTo(21);
        assertThat(purchaseRepo.findAll().size()).isEqualTo(51);
        assertThat(customerAddressRepo.findAll().size()).isEqualTo(101);
    }
}
