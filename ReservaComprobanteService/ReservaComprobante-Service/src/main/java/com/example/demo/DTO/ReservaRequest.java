package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequest {
    private int cantidadPersonas;
    private String duenoReserva;
    private int tipoReserva;
    private Date fechaReserva;
    private int cantidadVisitas;
    private String email;
    private int tarifaEspecial;
    private int cantidadFrecuencia;
}
