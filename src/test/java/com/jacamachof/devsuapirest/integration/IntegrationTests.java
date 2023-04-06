package com.jacamachof.devsuapirest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacamachof.devsuapirest.dto.AccountDto;
import com.jacamachof.devsuapirest.dto.BankStatementDto;
import com.jacamachof.devsuapirest.dto.ClientDto;
import com.jacamachof.devsuapirest.dto.MovementDto;
import com.jacamachof.devsuapirest.model.AccountType;
import com.jacamachof.devsuapirest.model.GenderEnum;
import com.jacamachof.devsuapirest.model.MovementType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTests {

    private MockMvc mockMvc;

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Order(1)
    void createClientTest() throws Exception {
        var client = ClientDto.builder()
                .identification("V26078218")
                .name("Joaquin Camacho")
                .address("Caracas, Venezuela")
                .phoneNumber("4142874557")
                .gender(GenderEnum.MALE.getValue())
                .birthdate(LocalDate.now().minusYears(20))
                .password("1234567890").build();

        mockMvc.perform(post("/clientes/crear")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(client))).andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void createAccount() throws Exception {
        var checking = AccountDto.builder()
                .clientId("V26078218")
                .accountNumber("123456")
                .accountType(AccountType.CHECKING.getValue())
                .balanceAvailable(BigDecimal.valueOf(500)).build();

        var saving = AccountDto.builder()
                .clientId("V26078218")
                .accountNumber("789012")
                .accountType(AccountType.SAVING.getValue())
                .balanceAvailable(BigDecimal.valueOf(500)).build();

        mockMvc.perform(post("/cuentas/crear")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(checking))).andExpect(status().isOk());

        mockMvc.perform(post("/cuentas/crear")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(saving))).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void createMovements() throws Exception {
        var movements = List.of(
                MovementDto.builder().accountId("123456")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(5))
                        .build(),
                MovementDto.builder().accountId("123456")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(10))
                        .build(),
                MovementDto.builder().accountId("123456")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(15))
                        .build(),
                MovementDto.builder().accountId("123456")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(20))
                        .build(),
                MovementDto.builder().accountId("123456")
                        .movementType(MovementType.CREDIT.getValue())
                        .movementValue(BigDecimal.valueOf(50))
                        .build(),
                MovementDto.builder().accountId("789012")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(5))
                        .build(),
                MovementDto.builder().accountId("789012")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(10))
                        .build(),
                MovementDto.builder().accountId("789012")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(15))
                        .build(),
                MovementDto.builder().accountId("789012")
                        .movementType(MovementType.DEBIT.getValue())
                        .movementValue(BigDecimal.valueOf(20))
                        .build()
        );

        for (var movement : movements) {
            mockMvc.perform(post("/movimientos/crear")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(MAPPER.writeValueAsString(movement))).andExpect(status().isOk());
        }
    }

    @Test
    @Order(4)
    void getBankStatement() throws Exception {
        var result = mockMvc.perform(get("/movimientos/reportes")
                .param("id", "V26078218")
                .param("startDate", LocalDate.now().minusDays(1).toString())
                .param("endDate", LocalDate.now().plusDays(1).toString())).andReturn();

        var content = MAPPER.readValue(result.getResponse().getContentAsString(), BankStatementDto.class);

        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.CEILING), content.getTotalDebit());
        assertEquals(BigDecimal.valueOf(50).setScale(2, RoundingMode.CEILING), content.getTotalCredit());
        assertEquals(2, content.getAccounts().size());
        assertEquals(5, content.getAccounts().get(0).getMovements().size());
        assertEquals(4, content.getAccounts().get(1).getMovements().size());
    }
}
