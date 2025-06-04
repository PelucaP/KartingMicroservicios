package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reportes")
public class ReporteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    Long id_reporte;

    @Column(name = "fechainicio")
    private Date fechaInicio;

    @Column(name = "fechafin")
    private Date fechaFin;

    // 1 = 10 minutos y vueltas, 2 = 15 y 15, 3 = 20 y 20.
    @Column(name="tiporeserva")
    private int tipoReserva;

    private double total;

    @Column(name = "cantidadpersonas")
    private int cantidadPersonas;
}
