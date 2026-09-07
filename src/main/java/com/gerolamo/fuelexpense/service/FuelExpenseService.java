package com.gerolamo.fuelexpense.service;

import com.gerolamo.fuelexpense.dto.FuelExpenseRequestDTO;
import com.gerolamo.fuelexpense.dto.FuelExpenseResponseDTO;
import com.gerolamo.fuelexpense.exception.ResourceNotFoundException;
import com.gerolamo.fuelexpense.model.FuelExpense;
import com.gerolamo.fuelexpense.model.User;
import com.gerolamo.fuelexpense.repository.FuelExpenseRepository;
import com.gerolamo.fuelexpense.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FuelExpenseService {

    private final FuelExpenseRepository fuelExpenseRepository;
    private final UserRepository userRepository;

    public FuelExpenseService(FuelExpenseRepository fuelExpenseRepository,
                              UserRepository userRepository) {
        this.fuelExpenseRepository = fuelExpenseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FuelExpenseResponseDTO create(FuelExpenseRequestDTO request) {
        User user = getAuthenticatedUser();
        FuelExpense fuelExpense = new FuelExpense();
        updateEntity(fuelExpense, request);
        fuelExpense.setUser(user);

        FuelExpense savedFuelExpense = fuelExpenseRepository.save(fuelExpense);

        return mapToResponse(savedFuelExpense);
    }

    public List<FuelExpenseResponseDTO> find(Integer year, LocalDate from, LocalDate to) {
        User user = getAuthenticatedUser();
        List<FuelExpense> fuelExpenses;

        if (year != null) {
            LocalDate firstDay = LocalDate.of(year, 1, 1);
            LocalDate lastDay = LocalDate.of(year, 12, 31);
            fuelExpenses = fuelExpenseRepository.findByUserAndDateBetweenOrderByDateDesc(user, firstDay, lastDay);
        } else if (from != null || to != null) {
            validateDateRange(from, to);
            fuelExpenses = fuelExpenseRepository.findByUserAndDateBetweenOrderByDateDesc(user, from, to);
        } else {
            fuelExpenses = fuelExpenseRepository.findByUserOrderByDateDesc(user);
        }

        return fuelExpenses.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public FuelExpenseResponseDTO update(Long id, FuelExpenseRequestDTO request) {
        User user = getAuthenticatedUser();
        FuelExpense fuelExpense = fuelExpenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Lançamento não encontrado"));

        updateEntity(fuelExpense, request);

        FuelExpense savedFuelExpense = fuelExpenseRepository.save(fuelExpense);

        return mapToResponse(savedFuelExpense);
    }

    @Transactional
    public void delete(Long id) {
        User user = getAuthenticatedUser();
        FuelExpense fuelExpense = fuelExpenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Lançamento não encontrado"));

        fuelExpenseRepository.delete(fuelExpense);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Usuário autenticado não encontrado");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado"));
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Informe as datas inicial e final");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Data inicial deve ser anterior ou igual à data final");
        }
    }

    private void updateEntity(FuelExpense fuelExpense, FuelExpenseRequestDTO request) {
        fuelExpense.setDate(request.getDate());
        fuelExpense.setAmount(request.getAmount());
        fuelExpense.setLiters(request.getLiters());
        fuelExpense.setPricePerLiter(request.getPricePerLiter());
        fuelExpense.setOdometer(request.getOdometer());
        fuelExpense.setFuelType(request.getFuelType());
        fuelExpense.setNotes(request.getNotes());
    }

    private FuelExpenseResponseDTO mapToResponse(FuelExpense fuelExpense) {
        FuelExpenseResponseDTO response = new FuelExpenseResponseDTO();
        response.setId(fuelExpense.getId());
        response.setDate(fuelExpense.getDate());
        response.setAmount(fuelExpense.getAmount());
        response.setLiters(fuelExpense.getLiters());
        response.setPricePerLiter(fuelExpense.getPricePerLiter());
        response.setOdometer(fuelExpense.getOdometer());
        response.setFuelType(fuelExpense.getFuelType());
        response.setNotes(fuelExpense.getNotes());
        response.setCreatedAt(fuelExpense.getCreatedAt());
        response.setUpdatedAt(fuelExpense.getUpdatedAt());
        return response;
    }
}
