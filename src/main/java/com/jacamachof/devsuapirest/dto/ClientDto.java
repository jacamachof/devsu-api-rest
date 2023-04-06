package com.jacamachof.devsuapirest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jacamachof.devsuapirest.validation.GenderValidation;
import com.jacamachof.devsuapirest.validation.StateValidation;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private UUID id;

    @NotBlank(message = "{identification.not-empty}")
    private String identification;

    @NotBlank(message = "{name.not-empty}")
    private String name;

    @NotBlank(message = "{address.not-empty}")
    private String address;

    @NotBlank(message = "{phone-number.not-empty}")
    @Size(min = 9, max = 10, message = "{phone-number.not-valid}")
    private String phoneNumber;

    @NotBlank(message = "{gender.not-empty}")
    @GenderValidation()
    private String gender;

    @NotNull(message = "{birthdate.not-empty}")
    @Past(message = "{birthdate.not-valid")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @ToString.Exclude
    @NotBlank(message = "{password.not-empty}")
    @Size(min = 9, max = 16, message = "{password.inv-length}")
    private String password;

    @StateValidation()
    private String state;
}
