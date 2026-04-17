package com.kadek.material.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expenses")
public class Expenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TRUCK_ID")
    private Long truckId;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "EXPENSE_DATE")
    private LocalDate expenseDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "INCOME_ID")
    private Long incomeId;
}
