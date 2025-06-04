package com.example.tarifa_service.Services;

import com.example.tarifa_service.Controller.TarifaController;
import com.example.tarifa_service.Entities.TarifaEntity;
import com.example.tarifa_service.Repository.TarifaRepository;
import org.springframework.stereotype.Service;

@Service
public class TarifaService {
    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public TarifaEntity guardarTarifa(TarifaEntity tarifaEntity) {
        return tarifaRepository.save(tarifaEntity);
    }

    public TarifaEntity crearTarifa(int tarifa) {
        TarifaEntity tarifaEntity = new TarifaEntity();
        tarifaEntity.setTipoTarifa(tarifa);
        if(tarifa==1){
            tarifaEntity.setTiempoTotal(30);
            tarifaEntity.setPrecioPersona(15000);
        }else if(tarifa==2){
            tarifaEntity.setTiempoTotal(35);
            tarifaEntity.setPrecioPersona(20000);
        }else if (tarifa==3){
            tarifaEntity.setTiempoTotal(60);
            tarifaEntity.setPrecioPersona(25000);
        }
        else {
            return null;
        }
        return tarifaRepository.save(tarifaEntity);
    }

}
