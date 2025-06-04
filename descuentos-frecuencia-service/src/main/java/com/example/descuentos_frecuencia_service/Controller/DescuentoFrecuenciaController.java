package com.example.descuentos_frecuencia_service.Controller;

import com.example.descuentos_frecuencia_service.Services.DescuentoFrecuenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/descuentofrecuencia")
public class DescuentoFrecuenciaController {
    private final DescuentoFrecuenciaService descuentoFrecuenciaService;

    public DescuentoFrecuenciaController(DescuentoFrecuenciaService descuentoFrecuenciaService) {
        this.descuentoFrecuenciaService = descuentoFrecuenciaService;
    }
    @GetMapping("/{cantvisitas}")
    public ResponseEntity<?> descuentoFrecuencia(@PathVariable int cantvisitas) {
        return ResponseEntity.ok(descuentoFrecuenciaService.obtenerDescuentoFrecuencia(cantvisitas));
    }
}
