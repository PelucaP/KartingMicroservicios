package com.example.demo.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reserva")
public class ReservaEntity {
    public enum tipoReserva {
        MINUTOS10,
        MINUTOS20,
        MINUTOS30,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "fechainicio")
    private Date fechaInicio;

    @Column(name = "fechafin")
    private Date fechaFin;

    private String email;
    @Column(name = "duenoreserva")
    private String duenoReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "tiporeserva")
    private tipoReserva tipoReserva;
    private double total;
    @Column(name = "cantidadpersonas")
    private int cantidadPersonas;
}