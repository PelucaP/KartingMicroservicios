package com.example.demo.Repositories;

import com.example.demo.Entities.ReservaEntity;
import com.example.demo.Services.ReservaService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity,Long> {

    @Query("SELECT r FROM ReservaEntity r WHERE r.fechaInicio < :fechaFin AND r.fechaFin > :fechaInicio")
    List<ReservaEntity> findConflictingReservas(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    List<ReservaEntity> findByTipoReservaAndFechaInicioBetween(int tipoReserva, Date fechaInicio, Date fechaFin);

    public List<ReservaEntity> findByfechaInicioBetween(Date fechaInicio, Date fechaFin);
}
