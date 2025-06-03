package com.example.descuentopersonas_service.service;

import org.springframework.stereotype.Service;

@Service
public class DescuentoService {

    public double descuento(int cantPersona) {
        if (cantPersona > 0 && cantPersona <= 2) {
            return 0;
        } else if (cantPersona >= 3 && cantPersona <= 5) {
            return 0.1;
        } else if (cantPersona >= 6 && cantPersona <= 10) {
            return 0.2;
        } else if (cantPersona >= 11 && cantPersona <= 15) {
            return 0.3;
        }
        return 0;
    }
}
