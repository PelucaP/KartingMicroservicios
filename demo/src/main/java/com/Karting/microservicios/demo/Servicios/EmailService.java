package com.Karting.microservicios.demo.Servicios;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final ComprobanteService comprobanteService;


    public void enviarEmail(String toEmail, String subject, String texto) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String sender = "tomas.carcamo.g@usach.cl";
            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(texto, true);


            byte[] pdf = comprobanteService.crearComprobante();
            ByteArrayResource resource = new ByteArrayResource(pdf);
            helper.addAttachment("Comprobante.pdf", resource);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo " + e.getMessage());
        }

    }
}
