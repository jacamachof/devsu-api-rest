package com.jacamachof.devsuapirest.dto;

import com.jacamachof.devsuapirest.validation.AccountTypeValidation;
import com.jacamachof.devsuapirest.validation.StateValidation;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private UUID id;

    @NotBlank(message = "{client-id.not-empty}")
    private String clientId;

    @NotBlank(message = "{account-number.not-empty}")
    @Size(min = 6, max = 6, message = "{account-number.not-valid}")
    @Pattern(regexp = "^[0-9]*$", message = "{account-number.not-valid}")
    private String accountNumber;

    @NotBlank(message = "{account-type.not-empty}")
    @AccountTypeValidation()
    private String accountType;

    @NotNull(message = "{balance-available.not-empty}")
    @DecimalMax(value = "1000000.00", message = "{balance-available.max-length}")
    @DecimalMin(value = "0000000.00", message = "{balance-available.non-negative}")
    private BigDecimal balanceAvailable;

    @StateValidation()
    private String state;
}
