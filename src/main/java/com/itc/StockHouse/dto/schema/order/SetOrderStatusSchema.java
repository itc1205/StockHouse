package com.itc.StockHouse.dto.schema.order;

import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetOrderStatusSchema {
    @NotNull
    private OrderStatusDTO status;
}
