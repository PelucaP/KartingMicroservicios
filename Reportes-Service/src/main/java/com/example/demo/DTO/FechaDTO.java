package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.processing.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FechaDTO {
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaInicio;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date fechaFin;
}
