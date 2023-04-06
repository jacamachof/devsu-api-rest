package com.jacamachof.devsuapirest.mapper;

import com.jacamachof.devsuapirest.dto.ChangePasswordDto;
import com.jacamachof.devsuapirest.dto.ClientDto;
import com.jacamachof.devsuapirest.model.ClientEntity;
import com.jacamachof.devsuapirest.model.GenderEnum;
import com.jacamachof.devsuapirest.model.StateEnum;

import java.util.Objects;

public class ClientMapper {

    private ClientMapper() {
        throw new AssertionError();
    }

    /**
     * Converts Client DTO object to Client entity object
     *
     * @param dto Client DTO object
     * @return entity Client entity
     */
    public static ClientEntity toEntity(ClientDto dto) {
        var entity = new ClientEntity();

        entity.setIdentification(dto.getIdentification());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setBirthdate(dto.getBirthdate());
        entity.setPassword(dto.getPassword());

        if (Objects.nonNull(dto.getId())) {
            entity.setId(dto.getId());
        }

        if (Objects.nonNull(dto.getGender())) {
            entity.setGender(GenderEnum.of(dto.getGender()).getValue());
        }

        if (Objects.nonNull(dto.getState())) {
            entity.setState(StateEnum.of(dto.getState()).getValue());
        }

        return entity;
    }

    /**
     * Converts ChangePassword DTO object to Client entity object
     *
     * @param dto ChangePasswordDto object
     * @return entity Client entity
     */
    public static ClientEntity toChangePassword(ChangePasswordDto dto) {
        var entity = new ClientEntity();

        entity.setIdentification(dto.getIdentification());
        entity.setPassword(dto.getPassword());
        entity.setNewPassword(dto.getNewPassword());

        return entity;
    }

    /**
     * Converts Client entity object to Client DTO object. The mapper must not return client's password
     *
     * @param entity Client entity object
     * @return dto Client dto object
     */
    public static ClientDto toDto(ClientEntity entity) {
        return ClientDto.builder()
                .id(entity.getId())
                .identification(entity.getIdentification())
                .name(entity.getName())
                .address(entity.getAddress())
                .phoneNumber(entity.getPhoneNumber())
                .gender(entity.getGender())
                .birthdate(entity.getBirthdate())
                .state(entity.getState()).build();
    }
}
