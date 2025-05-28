package com.Karting.microservicios.demo.Repository;

import com.Karting.microservicios.demo.Entities.ComprobanteEntity;
import com.Karting.microservicios.demo.Entities.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity,Long> {
    public List<ReservaEntity> findByfechaInicioBetween(Date fechaInicio, Date fechaFin);

    public List<ReservaEntity> findByTipoReservaAndFechaInicioBetween(ReservaEntity.tipoReserva tipoReserva, Date fechaInicio, Date fechaFin);

}
