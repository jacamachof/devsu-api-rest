package com.jacamachof.devsuapirest.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankStatementDto {

    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private List<AccountStatementDto> accounts;
}
