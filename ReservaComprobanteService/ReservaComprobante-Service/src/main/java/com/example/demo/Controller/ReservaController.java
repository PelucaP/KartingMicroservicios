package com.example.demo.Controller;


import com.example.demo.Entities.ReservaEntity;
import com.example.demo.Services.EmailService;
import com.example.demo.Services.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    @PostMapping("/enviar")
    public ResponseEntity<?> enviarComprobante(@RequestBody ReservaEntity reserva) {
        emailService.enviarEmail(reserva.getEmail(),"Buenas tardes","Estimado " + reserva.getDuenoReserva() + " Aquí esta su comprobante");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/gfg")
    public ResponseEntity<String> getAnonymous() {
        return ResponseEntity.ok("Welcome to GeeksforGeeks");
    }
    /*
    @GetMapping("/dsctopersonas")
    public ResponseEntity<?> obtenerDsctoPersonas() {
        RestTemplate restTemplate = new RestTemplate();

    }
     */

}
