package com.Karting.microservicios.demo.Repository;

import com.Karting.microservicios.demo.Entities.ComprobanteEntity;
import com.Karting.microservicios.demo.Entities.ReservaEntity;
import com.Karting.microservicios.demo.Servicios.ReservaService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ComprobanteRepository extends JpaRepository<ComprobanteEntity,Long> {


}
