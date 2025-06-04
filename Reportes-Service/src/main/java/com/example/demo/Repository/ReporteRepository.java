package com.example.demo.Repository;

import com.example.demo.Entity.ReporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteEntity,Long> {
    List<ReporteEntity> findByTipoReservaAndFechaInicioBetween(int tipoReserva, Date fechaInicio, Date fechaFin);

    public List<ReporteEntity> findByfechaInicioBetween(Date fechaInicio, Date fechaFin);
}
