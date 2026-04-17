package com.kadek.material.Repository;

import com.kadek.material.Entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses,Long> {
    Optional<Expenses>findByIncomeId(Long id);
}
