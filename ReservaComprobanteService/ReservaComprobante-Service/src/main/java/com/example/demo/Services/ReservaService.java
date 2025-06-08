package com.example.demo.Services;

import com.example.demo.DTO.ReservaRackRequest;
import com.example.demo.DTO.ReservaRequest;
import com.example.demo.DTO.ReservaVueltasResponse;
import com.example.demo.DTO.TarifaResponse;
import com.example.demo.Entities.ReservaEntity;
import com.example.demo.Repositories.ReservaRepository;
import com.example.demo.client.DescuentoFrecuenciaClient;
import com.example.demo.client.DescuentoPersonasClient;
import com.example.demo.client.TarifaClient;
import com.example.demo.client.TarifaEspecialClient;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final DescuentoPersonasClient descuentoClient;
    private final DescuentoFrecuenciaClient descuentoFrecuenciaClient;
    private final TarifaClient tarifaClient;
    private final TarifaEspecialClient tarifaEspecialClient;


    public ReservaService(ReservaRepository reservaRepository, DescuentoPersonasClient descuentoClient,
                          DescuentoFrecuenciaClient descuentoFrecuenciaClient, TarifaClient tarifaClient, TarifaEspecialClient tarifaEspecialClient) {
        this.reservaRepository = reservaRepository;
        this.descuentoClient = descuentoClient;
        this.descuentoFrecuenciaClient = descuentoFrecuenciaClient;
        this.tarifaEspecialClient = tarifaEspecialClient;
        this.tarifaClient = tarifaClient;
    }

    public ReservaEntity getReservaById(Long idReserva) {
        return reservaRepository.findById(idReserva).get();
    }

    public List<ReservaEntity> getAllReservas() {
        return reservaRepository.findAll();
    }
    public List<ReservaRackRequest> getAllReservasForRack() {
        return reservaRepository.findAll().stream()
                .map(reserva -> new ReservaRackRequest(
                        reserva.getFechaInicio(),
                        reserva.getFechaFin(),
                        reserva.getDuenoReserva()))
                .collect(Collectors.toList());
    }

    public ReservaEntity createReserva(ReservaRequest reservaRequest) {

        ReservaEntity reserva = new ReservaEntity();

        double tarifaEspecial = 0.0;
        try {
            tarifaEspecial = tarifaEspecialClient.getTarifaEspeciales(reservaRequest.getTarifaEspecial());
        } catch (Exception e) {
            tarifaEspecial = 0.0;
        }
        //Obtener tarifa con microservicio 1
        TarifaResponse tarifa = new TarifaResponse();
        try{
            if(tarifaEspecial != 0){
                tarifa = tarifaClient.getTarifa(reservaRequest.getTipoReserva());
                if(tarifaEspecial > 1){
                    tarifa.setPrecioPersona(tarifa.getPrecioPersona() * tarifaEspecial);
                }else{
                    tarifa.setPrecioPersona(tarifa.getPrecioPersona() * (1- tarifaEspecial));
                }
            }else{
                tarifa = tarifaClient.getTarifa(reservaRequest.getTipoReserva());
            }
        } catch (Exception e) {
            tarifa.setTipoTarifa(1);
            tarifa.setPrecioPersona(10000);
            tarifa.setTiempoTotal(30);
            System.out.println("Error calling discount service: " + e.getMessage());
        }

        reserva.setDuenoReserva(reservaRequest.getDuenoReserva());
        reserva.setCantidadPersonas(reservaRequest.getCantidadPersonas());
        reserva.setTipoReserva(reservaRequest.getTipoReserva());
        reserva.setFrecuenciaVisitas(reservaRequest.getCantidadFrecuencia());
        reserva.setEmail(reservaRequest.getEmail());

        reserva.setFechaInicio(reservaRequest.getFechaReserva());

        //Obtener fecha de termino de la reserva a partir del inicio y del valor obtenido
        // a través de microservicio 1
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reservaRequest.getFechaReserva());
        calendar.add(Calendar.MINUTE, tarifa.getTiempoTotal());
        Date fechaFin = calendar.getTime();
        reserva.setFechaFin(fechaFin);

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
        double basePrice = 0;

        int cantidadVisitasFrecuencia = reserva.getFrecuenciaVisitas();

        if (cantidadVisitasFrecuencia <= 2){
            basePrice = tarifa.getPrecioPersona() * reserva.getCantidadPersonas();
        }else{
            double casoEspecial;
            try {
                casoEspecial = tarifa.getPrecioPersona() * descuentoFrecuenciaClient.getDescuentoFrecuencia(reserva.getFrecuenciaVisitas());
            }catch (Exception e){
                System.out.println("Error obteniendo descuento por frecuencia: " + e.getMessage());
                casoEspecial = tarifa.getPrecioPersona();
            }
            basePrice = tarifa.getPrecioPersona() * reserva.getCantidadPersonas() - tarifa.getTipoTarifa() + casoEspecial;
        }

        Double descuentoVisitas = 0.0;
        try{
            descuentoVisitas = descuentoClient.getDescuentoPersonas(reserva.getCantidadPersonas());
            if (descuentoVisitas == null) {
                descuentoVisitas = 0.0; // Default if service is unavailable
            }
        }catch (Exception e) {
        // Log the exception
        System.out.println("Error calling discount service: " + e.getMessage());
        // Continue with zero discount
        }
        // Apply discount to calculate final price
        double finalPrice = ((basePrice * (1 - descuentoVisitas))* 1.19);
        reserva.setTotal(finalPrice);
        return reservaRepository.save(reserva);
    }

    public List<ReservaEntity> getReservasBetweenDates(Date inicio, Date fin) {
        return reservaRepository.findByfechaInicioBetween(inicio, fin);
    }


    public byte[] crearComprobante(ReservaEntity reserva, int idTarifaEspecial) {
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
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);

            // Reservation details section
            document.add(new Paragraph("DETALLES DE LA RESERVA", subtitleFont));
            document.add(new Paragraph("ID de Reserva: " + reserva.getId(), contentFont));
            document.add(new Paragraph("Cliente: " + reserva.getDuenoReserva(), contentFont));
            document.add(new Paragraph("Email: " + reserva.getEmail(), contentFont));
            document.add(new Paragraph("Fecha y Hora de Inicio: " + reserva.getFechaInicio().toString(), contentFont));
            document.add(new Paragraph("Fecha y Hora de Término: " + reserva.getFechaFin().toString(), contentFont));
            document.add(new Paragraph("Cantidad de personas: " + reserva.getCantidadPersonas(), contentFont));
            document.add(new Paragraph("Frecuencia de visitas: " + reserva.getFrecuenciaVisitas(), contentFont));

            // Get tarifa information
            TarifaResponse tarifa = null;
            try {
                tarifa = tarifaClient.getTarifa(reserva.getTipoReserva());
            } catch (Exception e) {
                // Datos default en caso de falla de microservicio
                tarifa = new TarifaResponse();
                tarifa.setTipoTarifa(1);
                tarifa.setTiempoTotal(30);
                tarifa.setPrecioPersona(10000);
                }

            document.add(new Paragraph("Duración: " + tarifa.getTiempoTotal() + " minutos", contentFont));

            // Separator line
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(BaseColor.LIGHT_GRAY);
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph("\n"));

            // Price calculation section
            document.add(new Paragraph("DETALLE DE PRECIOS", subtitleFont));

            // Get base tarifa price

            double baseTarifaPrice = tarifa.getPrecioPersona();
            document.add(new Paragraph("Precio base por persona: $" + String.format("%,.0f", baseTarifaPrice), contentFont));

            // Special tariff multiplier if applicable
            double tarifaEspecial = 0.0;
            try {
                tarifaEspecial = tarifaEspecialClient.getTarifaEspeciales(idTarifaEspecial);
                if (tarifaEspecial != 0.0 && tarifaEspecial != 1.0) {
                    document.add(new Paragraph("Tarifa especial aplicada: " + String.format("%.2f", tarifaEspecial) + "x", contentFont));
                    document.add(new Paragraph("Precio con tarifa especial: $" + String.format("%,.0f", baseTarifaPrice * tarifaEspecial), contentFont));
                    if(tarifaEspecial < 1){
                        baseTarifaPrice = baseTarifaPrice * tarifaEspecial;
                    }else{
                        baseTarifaPrice = baseTarifaPrice * (1- tarifaEspecial);
                    }
                }
            } catch (Exception e) {
                document.add(new Paragraph("No se aplicó tarifa especial", contentFont));
            }

            // Total for all people before discounts
            double priceAllPersons = baseTarifaPrice * reserva.getCantidadPersonas();
            document.add(new Paragraph("Subtotal para " + reserva.getCantidadPersonas() + " personas: $" + String.format("%,.0f", priceAllPersons), contentFont));

            // Frequency discount if applicable
            double priceAfterFrequencyDiscount = priceAllPersons;
            if (reserva.getFrecuenciaVisitas() > 2) {
                try {
                    //llamado al microservicio de descuento para clientes frecuentes
                    double frecuencyDiscountFactor = descuentoFrecuenciaClient.getDescuentoFrecuencia(reserva.getFrecuenciaVisitas());
                    document.add(new Paragraph("Descuento por frecuencia (" + reserva.getFrecuenciaVisitas() + " visitas): " +
                            String.format("%.0f%%", frecuencyDiscountFactor*100), contentFont));

                    double casoEspecial = baseTarifaPrice * frecuencyDiscountFactor;
                    priceAfterFrequencyDiscount = (baseTarifaPrice * reserva.getCantidadPersonas()) - baseTarifaPrice + casoEspecial;
                    document.add(new Paragraph("Precio después de descuento por frecuencia: $" +
                            String.format("%,.0f", priceAfterFrequencyDiscount), contentFont));
                } catch (Exception e) {
                    document.add(new Paragraph("No se pudo aplicar descuento por frecuencia", contentFont));
                }
            } else {
                document.add(new Paragraph("No aplica descuento por frecuencia (mínimo 3 visitas)", contentFont));
            }

            // Group size discount
            double personasDiscountFactor = 0.0;
            try {
                personasDiscountFactor = descuentoClient.getDescuentoPersonas(reserva.getCantidadPersonas());
                document.add(new Paragraph("Descuento por cantidad de personas (" + reserva.getCantidadPersonas() +
                        "): " + String.format("%.0f%%", personasDiscountFactor * 100), contentFont));
            } catch (Exception e) {
                document.add(new Paragraph("No se pudo aplicar descuento por cantidad de personas", contentFont));
            }

            double priceAfterAllDiscounts = priceAfterFrequencyDiscount * (1 - personasDiscountFactor);
            document.add(new Paragraph("Precio después de todos los descuentos: $" +
                    String.format("%,.0f", priceAfterAllDiscounts), contentFont));

            // IVA calculation
            double ivaAmount = priceAfterAllDiscounts * 0.19;
            document.add(new Paragraph("IVA (19%): $" + String.format("%,.0f", ivaAmount), contentFont));

            // Final price with IVA
            Font priceFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            double finalPrice = priceAfterAllDiscounts * 1.19;
            Paragraph total = new Paragraph("TOTAL A PAGAR: $" + String.format("%,.0f", finalPrice), priceFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Compare with saved total for verification
            if (Math.abs(finalPrice - reserva.getTotal()) > 1) {  // Allow for minor rounding differences
                document.add(new Paragraph("\nNota: El precio calculado puede diferir ligeramente del precio almacenado " +
                        "($" + String.format("%,.0f", reserva.getTotal()) + ") debido a redondeo.",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.GRAY)));
            }

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
