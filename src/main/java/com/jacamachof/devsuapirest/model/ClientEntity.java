package com.jacamachof.devsuapirest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "clients")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString(callSuper = true)
public class ClientEntity extends AbstractPerson {

    @ToString.Exclude
    @Column(name = "password", nullable = false, length = 512)
    private String password;

    @Column(name = "state", nullable = false, length = 50)
    private String state = StateEnum.ACTIVE.getValue();

    @ToString.Exclude
    @Transient
    private String newPassword;
}
