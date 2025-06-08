package com.example.rack_service.Client;

import com.example.rack_service.DTO.ReservaRackRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "reservacomprobante-service")
public interface ReservaComprobanteClient {

    @GetMapping("/api/reserva/for-rack")
    List<ReservaRackRequest> getAllReservasForRack();
}
