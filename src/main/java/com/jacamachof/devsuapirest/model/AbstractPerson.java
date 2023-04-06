package com.jacamachof.devsuapirest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString(callSuper = true)
public abstract class AbstractPerson extends AbstractEntity {

    @EqualsAndHashCode.Include
    @Column(name = "identification", nullable = false, unique = true, length = 50)
    private String identification;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "gender", nullable = false, length = 50)
    private String gender;

    @Column(name = "birthdate", nullable = false, columnDefinition = "date")
    private LocalDate birthdate;
}
