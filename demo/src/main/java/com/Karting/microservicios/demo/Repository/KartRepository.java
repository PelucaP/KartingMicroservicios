package com.Karting.microservicios.demo.Repository;

import com.Karting.microservicios.demo.Entities.KartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KartRepository extends JpaRepository<KartEntity, Long> {


}
