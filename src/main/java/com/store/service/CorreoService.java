package com.store.service;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    @Value("${spring.mail.username}")
    private String remitente;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public CorreoService(JavaMailSender mailSender, MessageSource messageSource) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
    }

    public void enviarActivacion(String correo, String nombre, String token) {
        String asunto = messageSource.getMessage("registro.mensaje.activacion", null, Locale.getDefault());
        String plantilla = messageSource.getMessage("registro.correo.activar", null, Locale.getDefault());
        String cuerpo = String.format(plantilla, nombre, "", baseUrl, correo, token);
        enviar(correo, asunto, cuerpo);
    }

    public void enviarRecordatorio(String correo, String nombre, String token) {
        String asunto = messageSource.getMessage("registro.mensaje.recordar", null, Locale.getDefault());
        String plantilla = messageSource.getMessage("registro.correo.recordar", null, Locale.getDefault());
        String cuerpo = String.format(plantilla, nombre, "", baseUrl, correo, token);
        enviar(correo, asunto, cuerpo);
    }

    private void enviar(String destinatario, String asunto, String cuerpoHtml) {
        try {
            var mensaje = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true);
            mailSender.send(mensaje);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar correo a " + destinatario, e);
        }
    }
}
