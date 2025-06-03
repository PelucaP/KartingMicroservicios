package com.example.demo.Services;

import com.example.demo.Entities.ReservaEntity;
import com.example.demo.Repositories.ReservaRepository;
import com.example.demo.client.DescuentoClient;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final DescuentoClient descuentoClient;


    public ReservaService(ReservaRepository reservaRepository, DescuentoClient descuentoClient) {
        this.reservaRepository = reservaRepository;
        this.descuentoClient = descuentoClient;

    }

    public ReservaEntity getReservaById(Long idReserva) {
        return reservaRepository.findById(idReserva).get();
    }
    public List<ReservaEntity> getAllReservas() {
        return reservaRepository.findAll();
    }

    public ReservaEntity createReserva(ReservaEntity reserva) {
        if (reserva.getFechaInicio() == null || reserva.getFechaFin() == null) {
            throw new IllegalArgumentException("La fecha de inicio y fin de la reserva no pueden ser nulas.");
        }
        if (reserva.getFechaInicio().after(reserva.getFechaFin()) || reserva.getFechaInicio().equals(reserva.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin y no pueden ser iguales.");
        }
        List<ReservaEntity> conflictingReservas = reservaRepository.findConflictingReservas(
                reserva.getFechaInicio(),
                reserva.getFechaFin()
        );
        if (!conflictingReservas.isEmpty()) {
            throw new IllegalStateException("La franja horaria solicitada para la reserva ya está ocupada o se superpone con otra reserva.");
        }

        // Calculate base price
        double basePrice = calculateBasePrice(reserva.getTipoReserva());

        // Get discount using Feign client
        Double discount = descuentoClient.getDescuento(reserva.getCantidadPersonas());
        if (discount == null) {
            discount = 0.0; // Default if service is unavailable
        }

        // Apply discount to calculate final price
        double finalPrice = basePrice * (1 - discount);
        reserva.setTotal(finalPrice);

        return reservaRepository.save(reserva);
    }

    private double calculateBasePrice(ReservaEntity.tipoReserva tipo) {
        switch (tipo) {
            case MINUTOS10:
                return 10000;
            case MINUTOS20:
                return 18000;
            case MINUTOS30:
                return 25000;
            default:
                return 0;
        }
    }

    public byte[] crearComprobante(ReservaEntity reserva) {
        try {
            Document document = new Document();
            ByteArrayOutputStream pdf = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, pdf);
            document.open();
            
            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("COMPROBANTE DE RESERVA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Content font
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            
            // Reservation details
            document.add(new Paragraph("ID de Reserva: " + reserva.getId(), contentFont));
            document.add(new Paragraph("Fecha y Hora: " + reserva.getFechaInicio().toString(), contentFont));
            
            // Number of laps based on enum
            String laps;
            switch (reserva.getTipoReserva()) {
                case MINUTOS10:
                    laps = "10 minutos (5 vueltas)";
                    break;
                case MINUTOS20:
                    laps = "20 minutos (10 vueltas)";
                    break;
                case MINUTOS30:
                    laps = "30 minutos (15 vueltas)";
                    break;
                default:
                    laps = "No especificado";
            }
            document.add(new Paragraph("Duración/Vueltas: " + laps, contentFont));
            
            // Total people
            document.add(new Paragraph("Cantidad de personas: " + reserva.getCantidadPersonas(), contentFont));
            
            // Reservation owner
            document.add(new Paragraph("Reservado por: " + reserva.getDuenoReserva(), contentFont));
            
            // Separator line
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(BaseColor.LIGHT_GRAY);
            document.add(new Chunk(lineSeparator));
            
            // Total section with discount info
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            document.add(new Paragraph("\nResumen de pago:", totalFont));
            
            // Base price
            double basePrice = calculateBasePrice(reserva.getTipoReserva());
            document.add(new Paragraph("Precio base: $" + String.format("%,.0f", basePrice), contentFont));
            
            // Discount calculation
            double discount = 0;
            try {
                discount = descuentoClient.getDescuento(reserva.getCantidadPersonas());
                document.add(new Paragraph("Descuento aplicado: " + String.format("%.0f%%", discount * 100), contentFont));
            } catch (Exception e) {
                document.add(new Paragraph("Descuento aplicado: 0%", contentFont));
            }
            
            // Final price
            Font priceFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Paragraph total = new Paragraph("TOTAL: $" + String.format("%,.0f", reserva.getTotal()), priceFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            
            // Footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph footer = new Paragraph("\nGracias por preferir Karting Microservicios. ¡Disfrute su experiencia!", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            document.close();
            return pdf.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
