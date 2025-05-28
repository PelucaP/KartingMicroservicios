package com.example.demo.Services;

import com.example.demo.Entities.ReservaEntity;
import com.example.demo.Repositories.ReservaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;


    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
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
            // A clash is found
            throw new IllegalStateException("La franja horaria solicitada para la reserva ya está ocupada o se superpone con otra reserva.");
        }
        // No conflicts, proceed to save
        return reservaRepository.save(reserva);
    }

    public byte[] crearComprobante() {
        try {
            Document document = new com.itextpdf.text.Document();
            ByteArrayOutputStream pdf = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, pdf);
            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Chunk chunk = new Chunk("Comprobante", font);
            document.add(chunk);
            document.close();
            return pdf.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
