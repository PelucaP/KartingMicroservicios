package com.example.demo.Repositories;

import com.example.demo.Entities.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity,Long> {

    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaInicio < :fechaFin AND r.fechaFin > :fechaInicio")
    List<ReservaEntity> findConflictingReservas(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

}
