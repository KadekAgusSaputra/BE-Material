package com.kadek.material.Controller;

import com.kadek.material.Dto.*;
import com.kadek.material.Service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/material")
public class IncomeController {

    @Autowired
    IncomeService incomeService;

    @PostMapping("transaction")
    public ResponseEntity<IncomeDTO> createTransaction(@RequestBody IncomeDTO incomeDTO) {
        try {
            IncomeDTO incomeDTO1 = incomeService.createTransaksi(incomeDTO);
            return ResponseEntity.status(201).body(incomeDTO1);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(409).body(null);
        }
    }
    @PutMapping("transaction/{id}")
    public ResponseEntity<IncomeDTO> updateTransaction(@PathVariable Long id,@RequestBody IncomeDTO incomeDTO) {
        try {
            IncomeDTO incomeDTO1 = incomeService.updateTransaksi(id,incomeDTO);
            return ResponseEntity.status(201).body(incomeDTO1);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(409).body(null);
        }
    }

    @GetMapping("transaction")
    public ResponseEntity<List<IncomeDTO>> getAllTransaction(){
        List<IncomeDTO> transaction = incomeService.getAllTransaction();
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("transaction/{id}")
    public ResponseEntity<IncomeDTO> getTransaction(@PathVariable Long id){
        IncomeDTO transaction = incomeService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("transaction/price/{id}")
    public ResponseEntity<List<MaterialPriceDto>> getMaterialPrice(@PathVariable Long id){
        List<MaterialPriceDto> price = incomeService.getMaterialPrice(id);
        return ResponseEntity.ok(price);
    }

    @GetMapping("transaction/dashboard")
    public ResponseEntity<List<DashboardDTO>> getDashboardTransaction(){
        List<DashboardDTO> transaction = incomeService.getDashboardTransaction();
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("transaction/vehicle")
    public ResponseEntity<List<VehiclesDTO>> getVehicle(){
        List<VehiclesDTO> vehicle = incomeService.getAllVehicle();
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("transaction/laporan")
    public ResponseEntity<LaporanDTO> getLaporanTransaction(){
        LaporanDTO transaction = incomeService.getLaporanTransaction();
        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        try {
            incomeService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
