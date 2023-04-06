package com.jacamachof.devsuapirest.controller;

import com.jacamachof.devsuapirest.dto.BankStatementDto;
import com.jacamachof.devsuapirest.dto.ClientMovementsDto;
import com.jacamachof.devsuapirest.dto.MovementDto;
import com.jacamachof.devsuapirest.exception.BusinessException;
import com.jacamachof.devsuapirest.exception.ExceptionFactory;
import com.jacamachof.devsuapirest.mapper.MovementMapper;
import com.jacamachof.devsuapirest.service.MovementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/movimientos")
public class MovementController {

    private MovementService movementService;

    private ExceptionFactory exceptionFactory;

    @Autowired
    public void setMovementService(MovementService movementService) {
        this.movementService = movementService;
    }

    @Autowired
    public void setExceptionFactory(ExceptionFactory exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    @GetMapping("/reportes")
    public BankStatementDto getBankStatementByClientAndRange(
            @RequestParam @NotBlank String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BankStatementDto response;

        log.info("Generating bank statement for client {}, with start date {} and end date {}",
                id, startDate, endDate);

        try {
            response = MovementMapper.toBankStatementDto(
                    movementService.getBankStatementByClientAndRange(id, startDate, endDate));
        } catch (BusinessException e) {
            log.warn("Could not generate bank statement for client {}, with start date {} and end date {}"
                    , id, startDate, endDate);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to generate bank statement for client {}, " +
                            "with start date {} and end date {}, with message {}",
                    e.getClass().getSimpleName(), id, startDate, endDate, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();

        }

        log.info("Generated bank statement for client {}, with start date {} and end date {}",
                id, startDate, endDate);

        return response;
    }

    @GetMapping("/obtener")
    public MovementDto getMovement(@RequestParam UUID id) {
        MovementDto response;

        log.info("Getting movement {} information", id);

        try {
            response = MovementMapper.toDto(movementService.getMovement(id));
        } catch (BusinessException e) {
            log.warn("Movement {} not found. {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to get movement {} with message {}",
                    e.getClass().getSimpleName(), id, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Movement {} information found {}", id, response);

        return response;
    }

    @GetMapping("/obtener/cliente")
    public ClientMovementsDto getMovementsByClientId(
            @RequestParam @NotBlank String id,
            @RequestParam @PositiveOrZero Integer page,
            @RequestParam @Positive Integer size) {
        ClientMovementsDto response;

        log.info("Getting {} client's latest movements with page {} and size {}", id, page, size);

        try {
            response = MovementMapper.toClientMovementDto(
                    movementService.getMovementsByClientIdentification(id, page, size));
        } catch (BusinessException e) {
            log.warn("Could not get {} client's latest movements with page {} and size {}. {}",
                    id, page, size, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to get {} client's latest movements" +
                    " with page {} and size {} with message {}", e.getClass().getSimpleName(), id, page, size, e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Got {} client's latest movements with page {} and size {}, {}", id, page, size, response);

        return response;
    }

    @PostMapping("/crear")
    public MovementDto createMovement(@Valid @RequestBody MovementDto dto) {
        MovementDto response;

        log.info("Creating movement using account {} with the following information {}", dto.getAccountId(), dto);

        try {
            response = MovementMapper.toDto(movementService.createMovement(MovementMapper.toEntity(dto)));
        } catch (BusinessException e) {
            log.warn("Movement with account {} not created. {}", dto.getAccountId(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception {} happened when trying to create movement with account {} with message {}",
                    e.getClass().getSimpleName(), dto.getAccountId(), e.getMessage(), e);
            throw exceptionFactory.getUnexpectedException();
        }

        log.info("Movement with account {} created with the following information {}", dto.getAccountId(), response);

        return response;
    }
}
