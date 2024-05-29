package com.itc.StockHouse.dto.domain.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    Long id;
    String login;
    String email;
    Boolean isActive;
}
