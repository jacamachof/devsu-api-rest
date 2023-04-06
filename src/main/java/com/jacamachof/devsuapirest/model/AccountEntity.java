package com.jacamachof.devsuapirest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString(callSuper = true)
public class AccountEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, columnDefinition = "uuid")
    private ClientEntity client;

    @EqualsAndHashCode.Include
    @Column(name = "account_number", nullable = false, unique = true, length = 50)
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;

    @Column(name = "balance_available", nullable = false)
    private BigDecimal balanceAvailable;

    @Column(name = "state", nullable = false, length = 50)
    private String state = StateEnum.ACTIVE.getValue();
}
