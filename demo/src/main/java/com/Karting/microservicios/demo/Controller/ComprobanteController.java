package com.Karting.microservicios.demo.Controller;

import com.Karting.microservicios.demo.Entities.ComprobanteEntity;
import com.Karting.microservicios.demo.Repository.ComprobanteRepository;
import com.Karting.microservicios.demo.Servicios.ComprobanteService;
import com.Karting.microservicios.demo.Servicios.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comprobante")
public class ComprobanteController {
    @Autowired
    private final ComprobanteService comprobanteService;
    private final EmailService emailService;

    public ComprobanteController(ComprobanteService comprobanteService, EmailService emailService) {
        this.comprobanteService = comprobanteService;
        this.emailService = emailService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<?> enviarComprobante(@RequestBody ComprobanteEntity comprobante) {
        emailService.enviarEmail(comprobante.getEmail(),"Buenas tardes","Este es un mail de comprobante");
        return ResponseEntity.ok().build();
    }

}
