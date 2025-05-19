package com.Karting.microservicios.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="karts")
public class KartEntity {
    public enum Estado{
        ACTIVO,
        EN_MANTENCION,
        NO_DISPONIBLE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable= false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
