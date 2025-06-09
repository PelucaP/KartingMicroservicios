package com.example.tarifas_especiales_service.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TarifaEspecialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,unique = true)
    Long id;
    String tipoTarifa;
    double descuento;   
}
