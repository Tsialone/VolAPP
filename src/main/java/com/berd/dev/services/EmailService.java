package com.berd.dev.services;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

    private  final JavaMailSender mailSender; // Spring l'injecte grâce aux properties ci-dessus

    public void envoyerEmail(String vers, String sujet, String contenu) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(vers);
        message.setSubject(sujet);
        message.setText(contenu);
        mailSender.send(message);
    }
}

