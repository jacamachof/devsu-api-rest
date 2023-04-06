package com.jacamachof.devsuapirest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jacamachof.devsuapirest.validation.MovementTypeValidation;
import com.jacamachof.devsuapirest.validation.StateValidation;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovementDto {

    private UUID id;

    @NotBlank(message = "{account-id.not-empty}")
    private String accountId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime movementDate;

    @NotBlank(message = "{movement-type.not-empty}")
    @MovementTypeValidation()
    private String movementType;

    @NotNull(message = "{movement-value.not-empty}")
    @DecimalMax(value = "1000000.00", message = "{movement-value.max-length}")
    @DecimalMin(value = "0000001.00", message = "{movement-value.min-value}")
    private BigDecimal movementValue;

    private BigDecimal initialBalance;

    private BigDecimal balanceAvailable;

    @StateValidation()
    private String state;
}
