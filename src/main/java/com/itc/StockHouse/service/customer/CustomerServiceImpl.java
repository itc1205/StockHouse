package com.itc.StockHouse.service.customer;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;
import com.itc.StockHouse.exceptions.customer.CustomerAlreadyExistsException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;
import com.itc.StockHouse.model.CustomerEntity;
import com.itc.StockHouse.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public Long createCustomer(CustomerDTO customer) throws CustomerAlreadyExistsException {
        if (repository.findByLogin(customer.getLogin()).isPresent()) {
            throw new CustomerAlreadyExistsException();
        }

        CustomerEntity newCustomer = CustomerEntity.builder()
                .email(customer.getEmail())
                .login(customer.getLogin())
                .isActive(true)
                .build();

        return repository.save(newCustomer).getId();
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        CustomerEntity customer = repository.findById(id).orElseThrow(CustomerNotFoundException::new);
        return CustomerDTO.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .login(customer.getLogin())
                .isActive(customer.getIsActive())
                .build();
    }
}
