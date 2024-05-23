package com.itc.StockHouse.controller;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;
import com.itc.StockHouse.dto.schema.customer.CreateCustomerSchema;
import com.itc.StockHouse.exceptions.customer.CustomerAlreadyExistsException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;
import com.itc.StockHouse.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{id}")
    CustomerDTO getCustomerById(Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping("/")
    Long createCustomer(CreateCustomerSchema customer) {
        return customerService.createCustomer(
                CustomerDTO.builder()
                        .email(customer.getEmail())
                        .login(customer.getLogin())
                        .build()
        );
    }


}
