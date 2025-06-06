package com.example.tarifa_service.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TarifaResponse {
    private int tipoTarifa;
    private int tiempoTotal;
    private int precioPersona;
}
