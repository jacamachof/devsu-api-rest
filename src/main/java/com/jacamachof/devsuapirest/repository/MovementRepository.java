package com.jacamachof.devsuapirest.repository;

import com.jacamachof.devsuapirest.model.MovementEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovementRepository extends CrudRepository<MovementEntity, UUID> {

    @Query("SELECT SUM(m.movementValue) FROM MovementEntity m JOIN m.account a " +
            "WHERE a.client.id = :id AND m.movementType = :type AND m.movementDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTodayMovements(UUID id, String type, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM MovementEntity m JOIN m.account a " +
            "WHERE a.client.identification = :identification ORDER BY m.movementDate DESC")
    Page<MovementEntity> findByClient(String identification, Pageable pageable);

    @Query("SELECT a, m FROM MovementEntity m JOIN m.account a " +
            "WHERE a.client.identification = :identification AND " +
            "m.movementDate BETWEEN :startDate AND :endDate GROUP BY a.id, m.id ORDER BY a.id ASC, m.movementDate DESC")
    List<Tuple> findByClientGroupByAccount(String identification, LocalDateTime startDate, LocalDateTime endDate);
}
