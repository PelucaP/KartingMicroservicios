package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaVueltasResponse {
    private Long id;
    private Date fechaInicio;
    private Date fechaFin;
    private int tipoReserva;    // 1=30min, 2=45min, 3=60min
    private double total;       // precio final
    private int cantidadPersonas;
    private int frecuenciaVisitas;
}