package com.Karting.microservicios.demo.Servicios;

import com.Karting.microservicios.demo.Entities.ReservaEntity;
import com.Karting.microservicios.demo.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservaService {
    @Autowired
    private final ReservaRepository reservaRepository;


    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public ReservaEntity getReservaById(Long idReserva) {
        return reservaRepository.findById(idReserva).get();
    }
    public List<ReservaEntity> getAllReservas() {
        return reservaRepository.findAll();
    }

    public ReservaEntity createReserva(ReservaEntity reserva) {
        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha de inicio y fin de la reserva no pueden ser nulas.");
        }
        if (reserva.getFechaInicio().after(reserva.getFechaFin()) || reserva.getFechaInicio().equals(reserva.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin y no pueden ser iguales.");
        }
        List<ReservaEntity> conflictingReservas = reservaRepository.findConflictingReservas(
                reserva.getFechaInicio(),
                reserva.getFechaFin()
        );
        if (!conflictingReservas.isEmpty()) {
            // A clash is found
            throw new IllegalStateException("La franja horaria solicitada para la reserva ya está ocupada o se superpone con otra reserva.");
        }
        // No conflicts, proceed to save
        return reservaRepository.save(reserva);
    }

    public List<ReservaEntity> reservasBetweenDate(Date inicio, Date fin) {
        return reservaRepository.findByfechaInicioBetween(inicio, fin);
    }

}
