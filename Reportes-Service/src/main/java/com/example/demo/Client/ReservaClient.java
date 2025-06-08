package com.example.demo.Client;

import com.example.demo.DTO.ReservaVueltasResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name="reservacomprobante-service")
public interface ReservaClient {
    @GetMapping("/api/reserva/between")
    List<ReservaVueltasResponse> getReservasBetweenDates(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date inicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fin);
}
