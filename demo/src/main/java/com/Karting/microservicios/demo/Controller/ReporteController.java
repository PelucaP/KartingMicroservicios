package com.Karting.microservicios.demo.Controller;

import com.Karting.microservicios.demo.Servicios.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/reportes") // Using /api prefix as a common practice
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/vueltas")
    public ResponseEntity<byte[]> descargarReporteVueltas(
            @RequestParam("inicio") @DateTimeFormat(pattern="yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern="yyyy-MM-dd") Date fin) {
        try {
            byte[] pdfBytes = reporteService.generarReporteVueltas(inicio, fin);

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

    @GetMapping("/ingresos-por-asistencia")
    public ResponseEntity<byte[]> descargarReporteIngresosPorAsistencia(
            @RequestParam("inicio") @DateTimeFormat(pattern="yyyy-MM-dd") Date inicio,
            @RequestParam("fin") @DateTimeFormat(pattern="yyyy-MM-dd") Date fin) {
        try {
            byte[] pdfBytes = reporteService.generarReporteIngresosPorAsistencia(inicio, fin);

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