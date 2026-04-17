package com.kadek.material.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INCOME")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",nullable = false)
    private Vehicles vehicleId;
    @Column(name = "MATERIAL_NAME")
    private String materialName;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;
    @Column(name = "TRANSACTION_DATE")
    private LocalDate transactionDate;
}
