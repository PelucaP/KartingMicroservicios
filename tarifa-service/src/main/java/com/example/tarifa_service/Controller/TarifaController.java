package com.example.tarifa_service.Controller;

import com.example.tarifa_service.Entities.TarifaEntity;
import com.example.tarifa_service.Services.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tarifa")
public class TarifaController {
    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @PostMapping("/{tipoTarifa}")
    public ResponseEntity<?> createTarifa(@PathVariable int tipoTarifa) {
        TarifaEntity tarifa = tarifaService.crearTarifa(tipoTarifa);
        if (tarifa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifa);
    }
}
