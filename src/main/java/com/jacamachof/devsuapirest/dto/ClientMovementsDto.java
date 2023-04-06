package com.jacamachof.devsuapirest.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientMovementsDto {

    private Integer pages;
    private List<MovementDto> movements;
}
