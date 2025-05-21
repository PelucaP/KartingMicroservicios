package com.Karting.microservicios.demo.Servicios;

import com.Karting.microservicios.demo.Entities.ReservaEntity;
import com.Karting.microservicios.demo.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return reservaRepository.save(reserva);
    }

    

}
