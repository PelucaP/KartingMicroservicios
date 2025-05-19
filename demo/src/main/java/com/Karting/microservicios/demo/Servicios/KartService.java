package com.Karting.microservicios.demo.Servicios;

import com.Karting.microservicios.demo.Entities.KartEntity;
import com.Karting.microservicios.demo.Repository.KartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KartService {
    KartRepository kartRepository;

    KartService(KartRepository kartRepository) {
        this.kartRepository = kartRepository;
    }

    public List<KartEntity> findAll() {
        return kartRepository.findAll();
    }
}
