package com.example.backend.service;

import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class ContractService {
    private static final String CONTRACTS_FOLDER = "contracts/"; // Dossier où les contrats sont stockés
    private static final String SIGNED_CONTRACTS_FOLDER = "signed_contracts/";
    @Autowired
    private ResponsablePointDeVenteRepository responsableRepository;

    @Value("${contract.storage.path}")
    private String contractStoragePath;


//    public String generateAndSaveContract(ResponsablePointDeVente responsable) {
//        try {
//            PDDocument document = new PDDocument();
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 750);
//                contentStream.showText("CONTRAT DE PARTENARIAT RESPONSABLE POINT DE VENTE");
//                contentStream.endText();
//
//                contentStream.setFont(PDType1Font.HELVETICA, 12);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 720);
//                contentStream.showText("Société: Promosport");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Adresse: Avenue Habib Bourguiba, Tunis");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 680);
//                contentStream.showText("RESPONSABLE POINT DE VENTE");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 660);
//                contentStream.showText("Nom: " + responsable.getNom());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Prénom: " + responsable.getPrenom());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("CIN: " + responsable.getCin());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Téléphone: " + responsable.getTelephone());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Email: " + responsable.getEmail());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Adresse: " + responsable.getAdresse());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Point de Vente: " + responsable.getPointDeVente().getNomP());
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 580);
//                contentStream.showText("ARTICLE 1 - DESCRIPTION DE L'ACTIVITÉ");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Le responsable assure la gestion du point de vente Promosport.");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Il doit respecter les règles et la politique de l'entreprise.");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 540);
//                contentStream.showText("ARTICLE 2 - MODALITÉS DE PAIEMENT");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Le responsable doit régler chaque commande à la livraison.");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Tout retard de paiement entraîne des sanctions.");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 500);
//                contentStream.showText("ARTICLE 3 - ENGAGEMENTS ET RESPONSABILITÉS");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Le responsable s’engage à fournir un service de qualité.");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Il doit respecter les lois en vigueur.");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Promosport peut mettre fin au contrat en cas de non-respect.");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 460);
//                contentStream.showText("ARTICLE 4 - DURÉE DU CONTRAT");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Ce contrat prend effet dès sa signature et est valable pour une durée indéterminée.");
//                contentStream.endText();
//
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 420);
//                contentStream.showText("ARTICLE 5 - SIGNATURES");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Signature du Responsable Point de Vente: ________________________");
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("Signature de Promosport: ________________________");
//                contentStream.endText();
//            }
//
//            String fileName = "contrat_" + responsable.getNom() + "_" + responsable.getPrenom() + ".pdf";
//            String filePath = "contracts/" + fileName;
//
//            File file = new File(filePath);
//            document.save(new FileOutputStream(file));
//            document.close();
//
//            return filePath;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
public String generateAndSaveContract(ResponsablePointDeVente responsable) {
    String nomSanitized = responsable.getNom().trim().replaceAll(" ", "_");
    String prenomSanitized = responsable.getPrenom().trim().replaceAll(" ", "_");
    String fileName = "contrat_" + nomSanitized + "_" + prenomSanitized + ".pdf";
    String filePath = CONTRACTS_FOLDER + fileName;

    // ✅ Vérifier si le contrat existe déjà
    File file = new File(filePath);
    if (file.exists()) {
        return filePath; // ⏩ Retourner le contrat existant sans recréer un nouveau
    }

    try {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 750);
            contentStream.showText("CONTRAT DE PARTENARIAT RESPONSABLE POINT DE VENTE");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 720);
            contentStream.showText("Société: Promosport");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Adresse: Avenue Habib Bourguiba, Tunis");
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 680);
            contentStream.showText("RESPONSABLE POINT DE VENTE");
            contentStream.endText();

            // 🔥 Insertion des informations du responsable
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 660);
            contentStream.showText("Nom: " + responsable.getNom());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Prénom: " + responsable.getPrenom());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("CIN: " + responsable.getCin());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Téléphone: " + responsable.getTelephone());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Email: " + responsable.getEmail());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Adresse: " + responsable.getAdresse());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Point de Vente: " + responsable.getPointDeVente().getNomP());
            contentStream.endText();
        }

        document.save(new FileOutputStream(file));
        document.close();

        // ✅ Enregistrer le chemin du contrat dans la base de données
        responsable.setContractPath(filePath);


        return filePath;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}
    public String signContract(String contractName, String signerName) {
        contractName = contractName.trim().replaceAll(" ", "_"); // ✅ Nettoyage
        String filePath = CONTRACTS_FOLDER + contractName;
        File contractFile = new File(filePath);

        if (!contractFile.exists()) {
            return "Erreur : Le contrat n'existe pas.";
        }

        try {
            PDDocument document = PDDocument.load(contractFile);
            PDPage page = document.getPage(0);
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

            // 🔐 Signature image ou texte
            String signaturePath = System.getProperty("user.home") + "/signatures/signature_" + signerName.trim().replaceAll(" ", "_") + ".png";
            File signatureFile = new File(signaturePath);
            if (signatureFile.exists()) {
                PDImageXObject signatureImage = PDImageXObject.createFromFile(signaturePath, document);
                contentStream.drawImage(signatureImage, 100, 50, 100, 50);
            } else {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 50);
                contentStream.showText("Signé par : " + signerName);
                contentStream.endText();
            }

            contentStream.close();

            String signedFileName = "signed_" + contractName;
            String signedFilePath = SIGNED_CONTRACTS_FOLDER + signedFileName;

            document.save(signedFilePath);
            document.close();

            // ✅ Lire en bytes
            byte[] signedBytes = Files.readAllBytes(Paths.get(signedFilePath));

            // 🔍 Extraire nom et prénom à partir du nom de fichier
            // Format attendu: contrat_<nom>_<prenom>.pdf
            String withoutExtension = contractName.replace(".pdf", "");
            String[] parts = withoutExtension.split("_");
            if (parts.length >= 3) {
                String nom = parts[1];
                String prenom = parts[2];

                Optional<ResponsablePointDeVente> respoOpt = responsableRepository.findByNomAndPrenom(nom, prenom);
                if (respoOpt.isPresent()) {
                    ResponsablePointDeVente responsable = respoOpt.get();
                    responsable.setContractSigned(signedBytes);
                    responsableRepository.save(responsable);
                }
            }

            return "Contrat signé avec succès : " + signedFilePath;

        } catch (IOException e) {
            return "Erreur lors de la signature : " + e.getMessage();
        }
    }


}
