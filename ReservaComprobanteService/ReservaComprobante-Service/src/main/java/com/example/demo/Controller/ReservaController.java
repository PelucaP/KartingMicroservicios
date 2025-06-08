package com.example.demo.Controller;


import com.example.demo.DTO.ReservaRequest;
import com.example.demo.DTO.ReservaVueltasResponse;
import com.example.demo.Entities.ReservaEntity;
import com.example.demo.Services.EmailService;
import com.example.demo.Services.ReservaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reserva")
public class ReservaController {
    private final ReservaService reservaService;
    private final EmailService emailService;

    public ReservaController(ReservaService reservaService, EmailService emailService) {
        this.reservaService = reservaService;
        this.emailService = emailService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> createReserva(@RequestBody ReservaRequest reserva) {
        ReservaEntity nuevaReserva = reservaService.createReserva(reserva);
        emailService.enviarEmail(reserva.getEmail(),"Buenas tardes","Estimado " + reserva.getDuenoReserva() + " Aquí esta su comprobante",nuevaReserva,reserva);
        return ResponseEntity.ok().build();
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
    @GetMapping("/between")
    public ResponseEntity<List<ReservaVueltasResponse>> getReservasBetweenDates(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date inicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fin) {

        List<ReservaEntity> reservas = reservaService.getReservasBetweenDates(inicio, fin);
        List<ReservaVueltasResponse> response = reservas.stream()
                .map(r -> new ReservaVueltasResponse(
                        r.getId(),
                        r.getFechaInicio(),
                        r.getFechaFin(),
                        r.getTipoReserva(),
                        r.getTotal(),
                        r.getCantidadPersonas(),
                        r.getFrecuenciaVisitas()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/creartest")
    public ResponseEntity<?> enviarComprobante(@RequestBody ReservaRequest reserva) {
        ReservaEntity nuevaReserva = reservaService.createReserva(reserva);
        return ResponseEntity.ok().build();
    }

}
