package com.example.demo.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class TarifaResponse {
    private int tipoTarifa;
    private int tiempoTotal;
    private double precioPersona;
}
