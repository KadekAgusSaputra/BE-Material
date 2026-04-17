package com.kadek.material.Dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDTO {
    private Long id;
    private Long vehicleId;
    private String truckName;
    private String materialName;
    private BigDecimal amount;
    private BigDecimal quantity;
    private BigDecimal totalPrice;
    private LocalDate transactionDate;
    private BigDecimal expensesAmount;
    private String expensesDescription;

}
