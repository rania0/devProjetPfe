package com.example.backend.service;

import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.Utilisateur;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ✅ 1️⃣ إرسال البريد عند إنشاء حساب جديد
    public void envoyerEmailCreationUtilisateur(Utilisateur utilisateur, String rawPassword) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(utilisateur.getMail());
        helper.setSubject("Bienvenue chez PromoSport");

        String emailContent = lireTemplateCreationCompte(utilisateur, rawPassword);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    private String lireTemplateCreationCompte(Utilisateur utilisateur, String rawPassword) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/email.html");
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        String template = new String(bytes, StandardCharsets.UTF_8);

        template = template.replace("{{nom}}", utilisateur.getNom());
        template = template.replace("{{prenom}}", utilisateur.getPrenom());
        template = template.replace("{{email}}", utilisateur.getMail());
        template = template.replace("{{password}}", rawPassword);

        return template;
    }

    // ✅ 2️⃣ إرسال البريد عند حذف حساب المستخدم
    public void envoyerEmailSuppressionUtilisateur(Utilisateur utilisateur) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(utilisateur.getMail());
        helper.setSubject("Suppression de votre compte - PromoSport");

        // Lire et remplir le template HTML
        String emailContent = lireTemplateSuppression(utilisateur);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    private String lireTemplateSuppression(Utilisateur utilisateur) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/email_suppression.html");
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        String template = new String(bytes, StandardCharsets.UTF_8);

        template = template.replace("{{prenom}}", utilisateur.getPrenom());
        template = template.replace("{{nom}}", utilisateur.getNom());

        return template;
    }

    // ✅ 3️⃣ إرسال البريد عند تغيير كلمة المرور
    public void envoyerEmailChangementMotDePasse(Utilisateur utilisateur, String nouveauMotDePasse) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(utilisateur.getMail());
        helper.setSubject("🔐 Mise à jour de votre mot de passe");

        // Lire et remplir le template HTML
        String emailContent = lireTemplateChangementMotDePasse(utilisateur, nouveauMotDePasse);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    private String lireTemplateChangementMotDePasse(Utilisateur utilisateur, String nouveauMotDePasse) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/email-update-password.html");
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        String template = new String(bytes, StandardCharsets.UTF_8);

        template = template.replace("{{nom}}", utilisateur.getNom());
        template = template.replace("{{prenom}}", utilisateur.getPrenom());
        template = template.replace("{{email}}", utilisateur.getMail());
        template = template.replace("{{password}}", nouveauMotDePasse);

        return template;
    }
    public void envoyerEmailConfirmationResponsable(ResponsablePointDeVente responsable, String rawPassword)
            throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(responsable.getEmail());
        helper.setSubject("✅ Confirmation d'inscription");

        String body = "Bonjour " + responsable.getPrenom() + " " + responsable.getNom() + ",\n\n"
                + "Votre demande a été acceptée.\n"
                + "Voici vos identifiants pour vous connecter à la plateforme :\n\n"
                + "📧 Email : " + responsable.getEmail() + "\n"
                + "🔐 Mot de passe : " + rawPassword + "\n\n" // ✅ corriger ici
                + "Merci de vous connecter depuis l'application web.";

        helper.setText(body);
        mailSender.send(message);
    }
    public void envoyerEmailRefusResponsable(ResponsablePointDeVente responsable, String raison)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(responsable.getEmail());
        helper.setSubject("❌ Refus de votre demande d'inscription");

        String body = "Bonjour " + responsable.getPrenom() + " " + responsable.getNom() + ",\n\n"
                + "Nous vous informons que votre demande d'inscription a été refusée.\n\n"
                + "📝 Raison du refus : " + raison + "\n\n"
                + "Pour plus d'informations, veuillez contacter notre support.\n\n"
                + "Merci.";

        helper.setText(body);
        mailSender.send(message);
    }




}
