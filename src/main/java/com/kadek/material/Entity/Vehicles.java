package com.kadek.material.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VEHICLES")
public class Vehicles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TRUCK_NAME")
    private String truckName;
    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private VehicleCategory category;
    @OneToMany(mappedBy = "vehicleId",cascade = CascadeType.ALL)
    private List<Income> incomes;
}
