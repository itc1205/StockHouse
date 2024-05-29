package com.itc.StockHouse.dto.schema.customer;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class CreateCustomerSchema {
    @NotNull
    String login;
    @NotNull
    String email;
}
