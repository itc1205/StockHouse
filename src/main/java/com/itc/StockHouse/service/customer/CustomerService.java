package com.itc.StockHouse.service.customer;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;

public interface CustomerService {
    Long createCustomer(CustomerDTO customer);

    CustomerDTO getCustomer(Long id);
}
