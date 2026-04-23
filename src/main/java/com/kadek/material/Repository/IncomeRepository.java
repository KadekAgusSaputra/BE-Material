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


    @Query(value = "SELECT v.id, v.truck_name, v.category, " +
            "COALESCE(SUM(i.sell_price * i.quantity), 0) as total_income, " +
            "COALESCE(SUM(i.buy_price * i.quantity), 0) + " +
            "  COALESCE((SELECT SUM(e.amount) FROM expenses e JOIN income inc ON e.income_id = inc.id WHERE inc.vehicle_id = v.id), 0) as total_expense, " +
            "COALESCE(SUM((i.sell_price - i.buy_price) * i.quantity), 0) - " +
            "  COALESCE((SELECT SUM(e.amount) FROM expenses e JOIN income inc ON e.income_id = inc.id WHERE inc.vehicle_id = v.id), 0) as net " +
            "FROM vehicles v LEFT JOIN income i ON i.vehicle_id = v.id " +
            "GROUP BY v.id, v.truck_name, v.category",
            nativeQuery = true)
    List<DashboardDTO> getDashboardTransactionPerVehicle();

    @Query("SELECT new com.kadek.material.Dto.LaporanDTO(" +
            "SUM(i.sellPrice * i.quantity), " +
            "SUM(i.buyPrice * i.quantity) + (SELECT COALESCE(SUM(e.amount), 0) FROM Expenses e), " +
            "SUM((i.sellPrice - i.buyPrice) * i.quantity) - (SELECT COALESCE(SUM(e.amount), 0) FROM Expenses e)) " +
            "FROM Income i")
    LaporanDTO getGrandTotalReport();

}

