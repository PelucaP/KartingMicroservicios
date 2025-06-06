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

    // 1 = 10 vueltas, 2= 15, 3 = 20
    @Column(name = "tiporeserva")
    private int tipoReserva;

    private double total;

    @Column(name = "cantidadpersonas")
    private int cantidadPersonas;

    @Column(name="frecuenciavisitas")
    private int frecuenciaVisitas;

}