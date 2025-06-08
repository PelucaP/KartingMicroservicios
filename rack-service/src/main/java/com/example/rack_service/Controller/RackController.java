package com.example.rack_service.Controller;

import com.example.rack_service.DTO.ReservaRackRequest;
import com.example.rack_service.Services.RackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rack")
public class RackController {

    private final RackService rackService;

    @Autowired
    public RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaRackRequest>> getCurrentReservas() {
        List<ReservaRackRequest> reservas = rackService.getReservasForDisplay();
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservas);
    }
}
