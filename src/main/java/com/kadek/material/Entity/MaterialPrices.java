package com.kadek.material.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MATERIAL_PRICES")
public class MaterialPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "MATERIAL_NAME")
    private String materialName;
    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private VehicleCategory category;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "DESCRIPTION")
    private String description;
}
