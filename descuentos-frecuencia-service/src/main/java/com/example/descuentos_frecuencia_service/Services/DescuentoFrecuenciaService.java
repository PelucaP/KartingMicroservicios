package com.example.descuentos_frecuencia_service.Services;

import org.springframework.stereotype.Service;

@Service
public class DescuentoFrecuenciaService {

    public double obtenerDescuentoFrecuencia(int cantidadVisitas){
        if (cantidadVisitas < 1){
            return 0;
        }else if (cantidadVisitas >= 2 && cantidadVisitas <= 4){
            return 0.1;
        }else if (cantidadVisitas >= 5 && cantidadVisitas <= 6){
            return 0.2;
        }else if (cantidadVisitas >= 7){
            return 0.3;
        }
        return 0;
    }
}
