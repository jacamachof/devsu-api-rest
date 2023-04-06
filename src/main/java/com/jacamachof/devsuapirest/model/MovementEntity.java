package com.jacamachof.devsuapirest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movements")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString(callSuper = true)
public class MovementEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, columnDefinition = "uuid")
    private AccountEntity account;

    @Column(name = "movement_date", nullable = false, columnDefinition = "timestamp")
    private LocalDateTime movementDate = LocalDateTime.now();

    @Column(name = "movement_type", nullable = false, length = 50)
    private String movementType;

    @Column(name = "movement_value", nullable = false)
    private BigDecimal movementValue;

    @Column(name = "initial_balance", nullable = false)
    private BigDecimal initialBalance;

    @Column(name = "balance_available", nullable = false)
    private BigDecimal balanceAvailable;

    @Column(name = "state", nullable = false, length = 50)
    private String state = StateEnum.ACTIVE.getValue();
}
