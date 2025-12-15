package com.groupa.chickendirectfarm.unittests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupa.chickendirectfarm.customer.CustomerController;
import com.groupa.chickendirectfarm.customer.CustomerService;
import com.groupa.chickendirectfarm.customer.address.CustomerAddressService;
import com.groupa.chickendirectfarm.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CustomerController.class)
public class CustomerControllerUnitTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private CustomerAddressService customerAddressService;

    @MockitoBean
    private DtoMapper dtoMapper;


}


