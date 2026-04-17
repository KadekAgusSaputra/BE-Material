package com.kadek.material.Dto;

import com.kadek.material.Entity.Income;
import com.kadek.material.Entity.VehicleCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclesDTO {
    private Long id;
    private String truckName;
    private VehicleCategory category;
}
