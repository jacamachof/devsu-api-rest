package com.jacamachof.devsuapirest.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementDto {

    private UUID id;
    private String clientId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balanceAvailable;
    private String state;
    private List<MovementDto> movements;
}
