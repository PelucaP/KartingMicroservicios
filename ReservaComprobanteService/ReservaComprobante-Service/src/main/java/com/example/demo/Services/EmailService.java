package com.example.demo.Services;

import com.example.demo.DTO.ReservaRequest;
import com.example.demo.Entities.ReservaEntity;
import com.example.demo.client.TarifaEspecialClient;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final ReservaService reservaService;
    private final TarifaEspecialClient tarifaEspecialClient;


    public void enviarEmail(String toEmail, String subject, String texto, ReservaEntity reserva, ReservaRequest reservaRequest) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String sender = "tomas.carcamo.g@usach.cl";
            helper.setFrom(sender);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(texto, true);


            byte[] pdf = reservaService.crearComprobante(reserva,reservaRequest.getTarifaEspecial());
            ByteArrayResource resource = new ByteArrayResource(pdf);
            helper.addAttachment("Comprobante.pdf", resource);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo " + e.getMessage());
        }

    }
}
