package com.example.tarifas_especiales_service.Controller;

import com.example.tarifas_especiales_service.Entity.TarifaEspecialEntity;
import com.example.tarifas_especiales_service.Services.TarifaEspecialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tarifaespecial")
public class TarifaEspecialController {
    private final TarifaEspecialService tarifaEspecialService;


    public TarifaEspecialController(TarifaEspecialService tarifaEspecialService) {
        this.tarifaEspecialService = tarifaEspecialService;
    }

    @PostMapping("/creartarifa")
    public ResponseEntity<?> crearTarifa(@RequestBody TarifaEspecialEntity tarifa){
        return ResponseEntity.ok(tarifaEspecialService.crearTarifaEspecial(tarifa.getTipoTarifa(),tarifa.getDescuento()));
    }

    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTarifas(){
        return ResponseEntity.ok(tarifaEspecialService.listarTarifasEspecial());
    }

    @GetMapping("/obtenertarifa/{id}")
    public ResponseEntity<?> obtenerTarifa(@PathVariable Long id){
        try {
            TarifaEspecialEntity tarifa = tarifaEspecialService.buscarTarifaEspecialPorId(id);
            if (tarifa != null) {
                return ResponseEntity.ok(tarifa.getDescuento());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
