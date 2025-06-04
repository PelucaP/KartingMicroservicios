package com.example.tarifas_especiales_service.Repository;

import com.example.tarifas_especiales_service.Entity.TarifaEspecialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifaEspecialRepository extends JpaRepository<TarifaEspecialEntity, Long> {
    TarifaEspecialEntity findByTipoTarifa(String tipoTarifa);
}
