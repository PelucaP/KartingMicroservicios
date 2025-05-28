package com.Karting.microservicios.demo.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name="comprobante")
public class ComprobanteEntity {
    public enum TipoReserva{
        MINUTOS10,
        MINUTOS20,
        MINUTOS30,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable= false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tiporeserva")
    private TipoReserva tipoReserva;

    @Column(name="totalvisita")
    private double totalVisita;

    private String email;

}
