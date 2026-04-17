package com.kadek.material.Dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private Long vehicleId;
    private String truckName;
    private String truckCategory;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal profit;
}
