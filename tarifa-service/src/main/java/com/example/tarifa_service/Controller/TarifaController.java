package com.example.tarifa_service.Controller;

import com.example.tarifa_service.Entities.TarifaEntity;
import com.example.tarifa_service.Services.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tarifa")
public class TarifaController {
    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping("/{tipoTarifa}")
    public ResponseEntity<?> createTarifa(@PathVariable int tipoTarifa) {
        TarifaEntity tarifa = tarifaService.crearTarifa(tipoTarifa);
        if (tarifa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifa);
    }
}
