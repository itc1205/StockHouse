package com.itc.StockHouse.dto.domain.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInfoDTO {
    Long id;
    String accountNumber;
    String email;
    String inn;
}
