package com.Karting.microservicios.demo.Controller;

import com.Karting.microservicios.demo.Entities.KartEntity;
import com.Karting.microservicios.demo.Servicios.KartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kart")
@RequiredArgsConstructor
public class KartController {

    @Autowired
    private final KartService kartService;

    @GetMapping("/listar")
    public List<KartEntity> listarKart() {
        return kartService.findAll();
    }

}
