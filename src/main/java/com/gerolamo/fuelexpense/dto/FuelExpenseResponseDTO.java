package com.gerolamo.fuelexpense.dto;

import com.gerolamo.fuelexpense.model.FuelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelExpenseResponseDTO {
    private Long id;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal liters;
    private BigDecimal pricePerLiter;
    private Integer odometer;
    private FuelType fuelType;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
