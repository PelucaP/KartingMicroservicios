package com.Karting.microservicios.demo.Servicios;

import com.Karting.microservicios.demo.Entities.ComprobanteEntity;
import com.Karting.microservicios.demo.Repository.ComprobanteRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

@Service
public class ComprobanteService {
    @Autowired
    private final ComprobanteRepository comprobanteRepository;
    @Autowired
    private JavaMailSender mailSender;

    public ComprobanteService(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
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


    /*
    public byte[] crearReporteVueltas(Date inicio, Date termino){

    }

     */
}
