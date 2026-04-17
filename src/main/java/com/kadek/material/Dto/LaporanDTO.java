package com.kadek.material.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaporanDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal profit;
}
