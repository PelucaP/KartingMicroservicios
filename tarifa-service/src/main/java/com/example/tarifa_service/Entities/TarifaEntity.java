package com.example.tarifa_service.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarifaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    Long id;

    // 1 = 10 minutos y vueltas, 2 = 15 y 15, 3 = 20 y 20.
    @Column(name="tipotarifa")
    int tipoTarifa;

    @Column(name="tiempototal")
    int tiempoTotal;

    @Column(name="preciopersona")
    int precioPersona;
}
