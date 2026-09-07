package com.gerolamo.fuelexpense.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    private String password;
}
