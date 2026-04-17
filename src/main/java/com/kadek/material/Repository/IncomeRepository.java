package com.kadek.material.Repository;

import com.kadek.material.Dto.DashboardDTO;
import com.kadek.material.Dto.LaporanDTO;
import com.kadek.material.Entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income,Long> {


    //PAKAI NATIVE KARENA TERLALU RIBET UNTUK PAKAI JPQL DAN KEMUNGKINAN ERROR
    @Query(value = "SELECT v.id, v.truck_name, v.category, " +
            "COALESCE(SUM(i.total_price), 0) as total_income, " +
            "COALESCE((SELECT SUM(e.amount) FROM expenses e JOIN income inc ON e.income_id = inc.id WHERE inc.vehicle_id = v.id), 0) as total_expense, " +
            "COALESCE(SUM(i.total_price), 0) - COALESCE((SELECT SUM(e.amount) FROM expenses e JOIN income inc ON e.income_id = inc.id WHERE inc.vehicle_id = v.id), 0) as net " +
            "FROM vehicles v LEFT JOIN income i ON i.vehicle_id = v.id " +
            "GROUP BY v.id, v.truck_name, v.category",
            nativeQuery = true)
    List<DashboardDTO> getDashboardTransactionPerVehicle();

    //PAKAI JPQL KARENA GAK TERLALU RIBET
    @Query("SELECT new com.kadek.material.Dto.LaporanDTO(" +
            "SUM(i.totalPrice), " +
            "(SELECT COALESCE(SUM(e.amount), 0) FROM Expenses e), " +
            "(SUM(i.totalPrice) - (SELECT COALESCE(SUM(e.amount), 0) FROM Expenses e))) " +
            "FROM Income i")
    LaporanDTO getGrandTotalReport();

}

