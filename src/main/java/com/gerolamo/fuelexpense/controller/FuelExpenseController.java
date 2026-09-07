package com.gerolamo.fuelexpense.controller;

import com.gerolamo.fuelexpense.dto.FuelExpenseRequestDTO;
import com.gerolamo.fuelexpense.dto.FuelExpenseResponseDTO;
import com.gerolamo.fuelexpense.service.FuelExpenseService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class FuelExpenseController {

    private final FuelExpenseService fuelExpenseService;

    public FuelExpenseController(FuelExpenseService fuelExpenseService) {
        this.fuelExpenseService = fuelExpenseService;
    }

    @PostMapping
    public ResponseEntity<FuelExpenseResponseDTO> create(@Valid @RequestBody FuelExpenseRequestDTO request) {
        FuelExpenseResponseDTO response = fuelExpenseService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FuelExpenseResponseDTO>> find(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<FuelExpenseResponseDTO> response = fuelExpenseService.find(year, from, to);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelExpenseResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FuelExpenseRequestDTO request) {

        FuelExpenseResponseDTO response = fuelExpenseService.update(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fuelExpenseService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
