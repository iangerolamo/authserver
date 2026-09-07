package com.gerolamo.fuelexpense.dto;

import com.gerolamo.fuelexpense.model.FuelType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelExpenseRequestDTO {

    @NotNull(message = "Data é obrigatória")
    private LocalDate date;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal amount;

    @NotNull(message = "Litros é obrigatório")
    @Positive(message = "Litros deve ser maior que zero")
    private BigDecimal liters;

    @NotNull(message = "Preço por litro é obrigatório")
    @Positive(message = "Preço por litro deve ser maior que zero")
    private BigDecimal pricePerLiter;

    @NotNull(message = "Odômetro é obrigatório")
    @PositiveOrZero(message = "Odômetro deve ser maior ou igual a zero")
    private Integer odometer;

    @NotNull(message = "Tipo de combustível é obrigatório")
    private FuelType fuelType;

    @Size(max = 500, message = "Observações deve ter no máximo 500 caracteres")
    private String notes;
}
