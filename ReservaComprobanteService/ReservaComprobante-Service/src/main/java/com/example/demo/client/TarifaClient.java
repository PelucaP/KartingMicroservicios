package com.example.demo.client;

import com.example.demo.DTO.TarifaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tarifa-service")
public interface TarifaClient {

    @GetMapping("/api/tarifa/{tipoTarifa}")
    TarifaResponse getTarifa(@PathVariable("tipoTarifa") int tipoTarifa);
}
