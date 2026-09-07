package com.gerolamo.fuelexpense.repository;

import com.gerolamo.fuelexpense.model.FuelExpense;
import com.gerolamo.fuelexpense.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FuelExpenseRepository extends JpaRepository<FuelExpense, Long> {
    List<FuelExpense> findByUserOrderByDateDesc(User user);

    List<FuelExpense> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate from, LocalDate to);

    Optional<FuelExpense> findByIdAndUser(Long id, User user);
}
