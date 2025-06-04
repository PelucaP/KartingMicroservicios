package com.example.tarifas_especiales_service.Services;

import com.example.tarifas_especiales_service.Entity.TarifaEspecialEntity;
import com.example.tarifas_especiales_service.Repository.TarifaEspecialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaEspecialService {
    private final TarifaEspecialRepository tarifaEspecialRepository;

    public TarifaEspecialService(TarifaEspecialRepository tarifaEspecialRepository) {
        this.tarifaEspecialRepository = tarifaEspecialRepository;
    }

    public TarifaEspecialEntity crearTarifaEspecial(String nombreTarifa, double descuento) {
        TarifaEspecialEntity tarifa = new TarifaEspecialEntity();
        tarifa.setTipoTarifa(nombreTarifa);
        tarifa.setDescuento(descuento);
        return tarifaEspecialRepository.save(tarifa);
    }

    public TarifaEspecialEntity buscarTarifaEspecialPorId(Long id) {
        return tarifaEspecialRepository.findById(id).get();
    }
    public List<TarifaEspecialEntity> listarTarifasEspecial() {
        return tarifaEspecialRepository.findAll();
    }

    public TarifaEspecialEntity obtenerTarifaPorNombre(String tipoTarifa) {
        return tarifaEspecialRepository.findByTipoTarifa(tipoTarifa);
    }
}
