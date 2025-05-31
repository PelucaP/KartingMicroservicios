package com.example.descuentopersonas_service.controller;

import com.example.descuentopersonas_service.service.DescuentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/descuento/")
public class DescuentoController {
    private final DescuentoService descuentoService;

    public DescuentoController(DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }
    @GetMapping("/{personas}")
    public ResponseEntity<?> getDescuento(@PathVariable("personas") int personas) {
        double descuento = descuentoService.descuento(personas);
        return ResponseEntity.ok().body(descuento);
    }
}
