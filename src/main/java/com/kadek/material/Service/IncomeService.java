package com.kadek.material.Service;

import com.kadek.material.Dto.*;
import com.kadek.material.Entity.*;
import com.kadek.material.Repository.ExpensesRepository;
import com.kadek.material.Repository.IncomeRepository;
import com.kadek.material.Repository.MaterialPricesRepository;
import com.kadek.material.Repository.VehiclesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private VehiclesRepository vehiclesRepository;
    @Autowired
    private MaterialPricesRepository materialPricesRepository;
    @Autowired
    private ExpensesRepository expensesRepository;

    public IncomeDTO createTransaksi(IncomeDTO incomeDTO){
        Optional<Vehicles> findIdVehicle = vehiclesRepository.findById(incomeDTO.getVehicleId());
        if (findIdVehicle.isEmpty()){
            throw new IllegalArgumentException("Id Vehicles is empty");
        }

        VehicleCategory categoryVehicle = findIdVehicle.get().getCategory();
        Optional<MaterialPrices> priceOptional = materialPricesRepository.findByCategoryAndMaterialName(categoryVehicle,incomeDTO.getMaterialName());

        if (priceOptional.isEmpty()) {
            priceOptional = materialPricesRepository.findByCategoryAndMaterialName(VehicleCategory.unit, incomeDTO.getMaterialName());
        }

        MaterialPrices materialPrices = priceOptional.orElseThrow(() ->
                new RuntimeException("Harga untuk material " + incomeDTO.getMaterialName() + " tidak ditemukan di sistem")
        );

        Income income = new Income();
        income.setVehicleId(findIdVehicle.get());
        income.setMaterialName(incomeDTO.getMaterialName());
        BigDecimal amount = materialPrices.getPrice();
        income.setAmount(amount);
        income.setQuantity(incomeDTO.getQuantity());
        BigDecimal totalPrice;
        totalPrice = amount
                .multiply(incomeDTO.getQuantity())
                .setScale(2, RoundingMode.HALF_UP);
        income.setTotalPrice(totalPrice);
        income.setTransactionDate(incomeDTO.getTransactionDate());

        Income entity = incomeRepository.save(income);

        System.out.println("Scale setelah save: " + entity.getTotalPrice().scale()); // cek ini
        System.out.println("Value setelah save: " + entity.getTotalPrice()); // cek ini


        if (incomeDTO.getExpensesAmount() != null && incomeDTO.getExpensesAmount().compareTo(BigDecimal.ZERO) > 0) {
            Expenses expense = new Expenses();
            expense.setIncomeId(entity.getId());
            expense.setTruckId(findIdVehicle.get().getId());
            expense.setAmount(incomeDTO.getExpensesAmount());
            expense.setDescription(incomeDTO.getExpensesDescription());
            expense.setExpenseDate(incomeDTO.getTransactionDate());

            expensesRepository.save(expense);
        }
        return convertToResponseDTO(entity,incomeDTO);
    }

    public IncomeDTO updateTransaksi(Long id, IncomeDTO incomeDTO){
        Income oldIncome = incomeRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Data transaksi with id " + id + " is not found"));

        Long finalVehicleId = (incomeDTO.getVehicleId() != null) ? incomeDTO.getVehicleId() : oldIncome.getVehicleId().getId();

        Optional<Vehicles> findIdVehicle = vehiclesRepository.findById(finalVehicleId);

        String finalMaterial = (incomeDTO.getMaterialName() != null) ? incomeDTO.getMaterialName() : oldIncome.getMaterialName();
        BigDecimal finalQuantity = (incomeDTO.getQuantity() != null) ? incomeDTO.getQuantity() : oldIncome.getQuantity();

        LocalDate finalDate = (incomeDTO.getTransactionDate() != null)
                ? incomeDTO.getTransactionDate()
                : oldIncome.getTransactionDate();

        VehicleCategory categoryVehicle = findIdVehicle.get().getCategory();
        Optional<MaterialPrices> priceOptional = materialPricesRepository.findByCategoryAndMaterialName(categoryVehicle,finalMaterial);

        if (priceOptional.isEmpty()) {
            priceOptional = materialPricesRepository.findByCategoryAndMaterialName(VehicleCategory.unit, finalMaterial);
        }

        MaterialPrices materialPrices = priceOptional.orElseThrow(() ->
                new RuntimeException("Harga untuk material " + finalMaterial + " tidak ditemukan di sistem")
        );

        oldIncome.setVehicleId(findIdVehicle.get());
        oldIncome.setMaterialName(finalMaterial);
        BigDecimal amount = materialPrices.getPrice();
        oldIncome.setAmount(amount);
        oldIncome.setQuantity(finalQuantity);
        BigDecimal totalPrice = amount.multiply(finalQuantity);
        oldIncome.setTotalPrice(totalPrice);
        oldIncome.setTransactionDate(finalDate);

        incomeRepository.save(oldIncome);


        Optional<Expenses> oldExpensesOpt = expensesRepository.findByIncomeId(oldIncome.getId());


        String finalDesc = Optional.ofNullable(incomeDTO.getExpensesDescription())
                .orElseGet(() -> oldExpensesOpt.map(Expenses::getDescription).orElse(null));


        boolean inputAmountExist = incomeDTO.getExpensesAmount() != null
                && incomeDTO.getExpensesAmount().compareTo(BigDecimal.ZERO) > 0;
        if (inputAmountExist){
            //1.jika ADA timpa
            if (oldExpensesOpt.isPresent()){
                Expenses oldExpenses = oldExpensesOpt.get();
                oldExpenses.setAmount(incomeDTO.getExpensesAmount());
                oldExpenses.setDescription(finalDesc);
                oldExpenses.setExpenseDate(finalDate);
                expensesRepository.save(oldExpenses);
            }
            //2.jika gak ADA create
            else {
                Expenses newExpenses = new Expenses();
                newExpenses.setIncomeId(oldIncome.getId());
                Optional.ofNullable(incomeDTO.getExpensesAmount()).ifPresent(newExpenses::setAmount);
                Optional.ofNullable(finalDesc).ifPresent(newExpenses::setDescription);
                Optional.ofNullable(finalDate).ifPresent(newExpenses::setExpenseDate);
                Optional.ofNullable(findIdVehicle.get().getId()).ifPresent(newExpenses::setTruckId);
                expensesRepository.save(newExpenses);
            }
        }
        return convertToResponseDTO(oldIncome,incomeDTO);
    }

    public List<IncomeDTO> getAllTransaction(){
        return incomeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(income -> convertToResponseDTO(income,new IncomeDTO()))
                .collect(Collectors.toList());
    }

    public List<VehiclesDTO> getAllVehicle() {
        List<Vehicles> vehicles = vehiclesRepository.findAll();

        return vehicles.stream()
                .map(vehicle -> {
                    VehiclesDTO dto = new VehiclesDTO();
                    dto.setId(vehicle.getId());
                    dto.setTruckName(vehicle.getTruckName());
                    dto.setCategory(vehicle.getCategory());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public IncomeDTO getTransactionById(Long id){
        Optional<Income>findTransaction = incomeRepository.findById(id);
        Income income = findTransaction.get();
        return convertToResponseDTO(income,new IncomeDTO());
    }

    public List<DashboardDTO> getDashboardTransaction(){
        return incomeRepository.getDashboardTransactionPerVehicle();
    }



    public List<MaterialPriceDto> getMaterialPrice(Long id){
        Optional<Vehicles> findIdVehicle = vehiclesRepository.findById(id);
        if (findIdVehicle.isEmpty()){
            throw new IllegalArgumentException("Id Vehicles is empty");
        }

        VehicleCategory categoryVehicle = findIdVehicle.get().getCategory();
        List<MaterialPrices> prices = materialPricesRepository.findByCategory(categoryVehicle);

        if (prices.isEmpty()) {
            prices = materialPricesRepository.findByCategory(VehicleCategory.unit);
        }

        // Mapping List ke DTO
        return prices.stream()
                .map(price -> convertMaterialPriceDto(price))
                .collect(Collectors.toList());

    }

    public LaporanDTO getLaporanTransaction(){

        return incomeRepository.getGrandTotalReport();
    }

    public void deleteTransaction(Long id){
        if (!incomeRepository.existsById(id)){
            throw new IllegalArgumentException("Id " + id + " Tidak Ada ");
        }
        incomeRepository.deleteById(id);
    }

    private MaterialPriceDto convertMaterialPriceDto(MaterialPrices entity){
        MaterialPriceDto response = new MaterialPriceDto();
        response.setMaterialName(entity.getMaterialName());
        response.setAmount(entity.getPrice());
        return response;
    }

    private IncomeDTO convertToResponseDTO(Income entity,IncomeDTO requestDTO) {
        IncomeDTO response = new IncomeDTO();
        response.setId(entity.getId());
        response.setVehicleId(entity.getVehicleId().getId());
        response.setTruckName(entity.getVehicleId().getTruckName());
        response.setMaterialName(entity.getMaterialName());
        response.setAmount(entity.getAmount());
        response.setQuantity(entity.getQuantity());
        response.setTotalPrice(entity.getTotalPrice());
        response.setTransactionDate(entity.getTransactionDate());
        Optional<Expenses> oldExpensesOpt = expensesRepository.findByIncomeId(entity.getId());

        if (oldExpensesOpt.isPresent()){
            Expenses oldExpenses = oldExpensesOpt.get();

            String finalDesc = (requestDTO.getExpensesDescription() != null) ? requestDTO.getExpensesDescription() : oldExpenses.getDescription();
            BigDecimal finalExpenseAmount = (requestDTO.getExpensesAmount() != null) ? requestDTO.getExpensesAmount() : oldExpenses.getAmount();
            response.setExpensesAmount(finalExpenseAmount);
            response.setExpensesDescription(finalDesc);
        }

        return response;
    }
}
