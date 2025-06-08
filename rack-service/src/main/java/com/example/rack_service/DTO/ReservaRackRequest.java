package com.example.rack_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRackRequest {
    private Date fechaInicio;
    private Date fechaFin;
    private String nombre;
}
