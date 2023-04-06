package com.jacamachof.devsuapirest.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    @NotBlank(message = "{identification.not-empty}")
    private String identification;

    @NotBlank(message = "{password.not-empty}")
    @Size(min = 9, max = 16, message = "{password.inv-length}")
    private String password;

    @NotBlank(message = "{new-password.not-empty}")
    @Size(min = 9, max = 16, message = "{new-password.inv-length}")
    private String newPassword;
}
