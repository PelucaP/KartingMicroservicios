package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="descuentos-frecuencia-service")
public interface DescuentoFrecuenciaClient {
    @GetMapping("/api/descuentofrecuencia/{cantvisitas}")
    Double getDescuentoFrecuencia(@PathVariable("cantvisitas") int cantVisitas);
}
