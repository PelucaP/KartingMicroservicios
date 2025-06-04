package com.example.demo.Controller;

import com.example.demo.DTO.FechaDTO;
import com.example.demo.Services.ReportesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/reporte")
public class ReporteController {
    private final ReportesService reportesService;

    public ReporteController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @PostMapping("/vueltas")
    public ResponseEntity<byte[]> descargarReporteVueltas(
            @RequestBody FechaDTO fechas) {
        try {
            byte[] pdfBytes = reportesService.generarReporteVueltas(fechas.getFechaInicio(), fechas.getFechaFin());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Suggests a filename for the download.
            headers.setContentDispositionFormData("attachment", "reporte_ingresos_vueltas.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Consider more specific error handling or logging
            // For simplicity, returning an internal server error.
            // You might want to return a more informative error response.
            System.err.println("Error al generar el reporte PDF: " + e.getMessage()); // Basic logging
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/cantidadgente")
    public ResponseEntity<byte[]> descargarReporteIngresosPorAsistencia(
            @RequestBody FechaDTO fechas) {
        try {
            byte[] pdfBytes = reportesService.generarReporteIngresosPorAsistencia(fechas.getFechaInicio(), fechas.getFechaFin());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_ingresos_asistencia.pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.err.println("Error al generar el reporte PDF de ingresos por asistencia: " + e.getMessage());
            e.printStackTrace();
            // Consider a more specific error response to the client
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
