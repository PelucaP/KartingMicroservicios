package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "descuentopersonas-service")
public interface DescuentoClient {

    @GetMapping("/api/descuento/{personas}")
    Double getDescuento(@PathVariable("personas") int personas);
}