package com.Karting.microservicios.demo.Repository;

import com.Karting.microservicios.demo.Entities.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity,Long> {

}
