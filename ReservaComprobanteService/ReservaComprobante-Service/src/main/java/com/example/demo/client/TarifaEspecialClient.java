package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="tarifas-especiales-service")
public interface TarifaEspecialClient {
    @GetMapping("api/tarifaespecial/obtenertarifa/{id}")
    double getTarifaEspeciales(@PathVariable("id") int idTarifa);
}
