package com.Karting.microservicios.demo.Repository;

import com.Karting.microservicios.demo.Entities.ComprobanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteRepository extends JpaRepository<ComprobanteEntity,Long> {
}
