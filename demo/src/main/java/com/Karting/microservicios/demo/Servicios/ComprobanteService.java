package com.Karting.microservicios.demo.Servicios;

import com.Karting.microservicios.demo.Entities.ComprobanteEntity;
import com.Karting.microservicios.demo.Repository.ComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ComprobanteService {
    @Autowired
    private final ComprobanteRepository comprobanteRepository;
    @Autowired
    private JavaMailSender mailSender;

    public ComprobanteService(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }
    public void enviarEmail(String toEmail, String subject, String texto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("tomas.carcamo.g@usach.cl");
        message.setText(texto);
        message.setSubject(subject);
        mailSender.send(message);
    }

}
