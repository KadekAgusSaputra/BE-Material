package com.kadek.material.Repository;

import com.kadek.material.Entity.MaterialPrices;
import com.kadek.material.Entity.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialPricesRepository extends JpaRepository<MaterialPrices,Long> {
    Optional<MaterialPrices> findByCategoryAndMaterialName(VehicleCategory category, String materialName);
    List<MaterialPrices> findByCategory(VehicleCategory category);
}
