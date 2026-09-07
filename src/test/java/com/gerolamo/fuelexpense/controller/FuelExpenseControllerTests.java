package com.gerolamo.fuelexpense.controller;

import com.gerolamo.fuelexpense.model.FuelExpense;
import com.gerolamo.fuelexpense.model.FuelType;
import com.gerolamo.fuelexpense.model.User;
import com.gerolamo.fuelexpense.repository.FuelExpenseRepository;
import com.gerolamo.fuelexpense.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FuelExpenseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FuelExpenseRepository fuelExpenseRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        fuelExpenseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldCreateFuelExpense() throws Exception {
        saveUser("user@example.com");

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "date": "2026-01-15",
                                  "amount": 250.00,
                                  "liters": 40.50,
                                  "pricePerLiter": 6.173,
                                  "odometer": 12500,
                                  "fuelType": "GASOLINE",
                                  "notes": "Posto da esquina"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.date").value("2026-01-15"))
                .andExpect(jsonPath("$.amount").value(250.00))
                .andExpect(jsonPath("$.liters").value(40.50))
                .andExpect(jsonPath("$.pricePerLiter").value(6.173))
                .andExpect(jsonPath("$.odometer").value(12500))
                .andExpect(jsonPath("$.fuelType").value("GASOLINE"))
                .andExpect(jsonPath("$.notes").value("Posto da esquina"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldListFuelExpensesByYearOrderedByDateDesc() throws Exception {
        User user = saveUser("user@example.com");
        User otherUser = saveUser("other@example.com");
        saveFuelExpense(user, LocalDate.of(2026, 1, 10), "GASOLINE", 10000);
        saveFuelExpense(user, LocalDate.of(2026, 2, 10), "ETHANOL", 11000);
        saveFuelExpense(user, LocalDate.of(2025, 12, 10), "DIESEL", 9000);
        saveFuelExpense(otherUser, LocalDate.of(2026, 3, 10), "FLEX", 13000);

        mockMvc.perform(get("/expenses")
                        .param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date").value("2026-02-10"))
                .andExpect(jsonPath("$[1].date").value("2026-01-10"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldListFuelExpensesByDateRangeOrderedByDateDesc() throws Exception {
        User user = saveUser("user@example.com");
        User otherUser = saveUser("other@example.com");
        saveFuelExpense(user, LocalDate.of(2026, 1, 10), "GASOLINE", 10000);
        saveFuelExpense(user, LocalDate.of(2026, 3, 10), "ETHANOL", 12000);
        saveFuelExpense(user, LocalDate.of(2026, 5, 10), "DIESEL", 14000);
        saveFuelExpense(otherUser, LocalDate.of(2026, 6, 10), "FLEX", 15000);

        mockMvc.perform(get("/expenses")
                        .param("from", "2026-02-01")
                        .param("to", "2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].date").value("2026-05-10"))
                .andExpect(jsonPath("$[1].date").value("2026-03-10"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldDeleteFuelExpense() throws Exception {
        User user = saveUser("user@example.com");
        FuelExpense fuelExpense = saveFuelExpense(user, LocalDate.of(2026, 1, 10), "GASOLINE", 10000);

        mockMvc.perform(delete("/expenses/{id}", fuelExpense.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/expenses")
                        .param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldReturnNotFoundWhenDeletingUnknownFuelExpense() throws Exception {
        saveUser("user@example.com");

        mockMvc.perform(delete("/expenses/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void shouldReturnNotFoundWhenDeletingAnotherUsersFuelExpense() throws Exception {
        saveUser("user@example.com");
        User otherUser = saveUser("other@example.com");
        FuelExpense fuelExpense = saveFuelExpense(otherUser, LocalDate.of(2026, 1, 10), "GASOLINE", 10000);

        mockMvc.perform(delete("/expenses/{id}", fuelExpense.getId()))
                .andExpect(status().isNotFound());
    }

    private User saveUser(String email) {
        User user = new User();
        user.setName("Test User");
        user.setEmail(email);
        user.setPassword("encoded-password");
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private FuelExpense saveFuelExpense(User user, LocalDate date, String fuelType, Integer odometer) {
        FuelExpense fuelExpense = new FuelExpense();
        fuelExpense.setDate(date);
        fuelExpense.setAmount(new BigDecimal("200.00"));
        fuelExpense.setLiters(new BigDecimal("40.000"));
        fuelExpense.setPricePerLiter(new BigDecimal("5.000"));
        fuelExpense.setOdometer(odometer);
        fuelExpense.setFuelType(FuelType.valueOf(fuelType));
        fuelExpense.setUser(user);

        return fuelExpenseRepository.save(fuelExpense);
    }
}
