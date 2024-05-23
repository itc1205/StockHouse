package com.itc.StockHouse.service.customer;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;
import com.itc.StockHouse.exceptions.customer.CustomerAlreadyExistsException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;

public interface CustomerService {
    Long createCustomer(CustomerDTO customer) throws CustomerAlreadyExistsException;

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
}
