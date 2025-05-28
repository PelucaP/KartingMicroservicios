package com.Karting.microservicios.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FechaEntreReservaRequest {
    private Date fechaInicio;
    private Date fechaFin;
}
