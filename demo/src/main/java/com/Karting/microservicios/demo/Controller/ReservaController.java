package com.Karting.microservicios.demo.Controller;

import com.Karting.microservicios.demo.Entities.ReservaEntity;
import com.Karting.microservicios.demo.Repository.ReservaRepository;
import com.Karting.microservicios.demo.Servicios.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> createReserva(@RequestBody ReservaEntity reserva) {
        ReservaEntity nuevaReserva = reservaService.createReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarReservas() {
         List<ReservaEntity> reservas = reservaService.getAllReservas();
         if(reservas.isEmpty()) {
             return ResponseEntity.noContent().build();
         }else{
             return ResponseEntity.ok(reservas);
         }
    }

}
