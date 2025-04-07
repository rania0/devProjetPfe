package com.example.backend.controller;


import com.example.backend.dto.RefusDTO;
import com.example.backend.dto.ResponsablePointDeVenteDTO;
import com.example.backend.entity.PtVente;
import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.StatutResponsable;
import com.example.backend.repository.PtVenteRepository;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import com.example.backend.service.ContractService;
import com.example.backend.service.ResponsablePointDeVenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contract")
public class ContractController {

    private final ContractService contractService;
    @Autowired
    private ResponsablePointDeVenteRepository responsableRepository;
    @Autowired
    private PtVenteRepository ptVenteRepository;


    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

//    @GetMapping("/generate")
//    public ResponseEntity<String> generateContract(
//            @RequestParam String nom,
//            @RequestParam String prenom,
//            @RequestParam String nomPointDeVente,
//            @RequestParam String adresse) {
//
//        // 🔥 Créer un objet ResponsablePointDeVente au lieu de passer des Strings
//        ResponsablePointDeVente responsable = new ResponsablePointDeVente();
//        responsable.setNom(nom);
//        responsable.setPrenom(prenom);
//        responsable.setAdresse(adresse);
//
//        PtVente pointDeVente = new PtVente();
//        pointDeVente.setNomP(nomPointDeVente);
//        responsable.setPointDeVente(pointDeVente);
//
//        String filePath = contractService.generateAndSaveContract(responsable);
//
//        if (filePath == null) {
//            return ResponseEntity.internalServerError().body("Erreur lors de la génération du contrat.");
//        }
//
//        return ResponseEntity.ok("Contrat enregistré : " + filePath);
//    }
//
//
//    @PostMapping("/sign")
//    public ResponseEntity<String> signContract(
//            @RequestParam String contractName,
//            @RequestParam String signerName) {
//        String result = contractService.signContract(contractName, signerName);
//        return ResponseEntity.ok(result);
//    }
@PostMapping("/generate")
public ResponseEntity<String> generateContract(@RequestBody ResponsablePointDeVenteDTO dto) {
    Optional<PtVente> pointDeVenteOpt = ptVenteRepository.findById(dto.getPointDeVenteId());

    if (pointDeVenteOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("Erreur: Point de vente non trouvé.");
    }

    // ✅ Créer un objet ResponsablePointDeVente avec toutes les informations
    ResponsablePointDeVente responsable = ResponsablePointDeVente.builder()
            .nom(dto.getNom())
            .prenom(dto.getPrenom())
            .email(dto.getEmail())
            .telephone(dto.getTelephone())
            .cin(dto.getCin())
            .adresse(dto.getAdresse())
            .typeActivite(dto.getTypeActivite())
            .pointDeVente(pointDeVenteOpt.get())
            .status(StatutResponsable.EN_ATTENTE)
            .build();

    // ✅ Vérifier si le contrat existe déjà
    if (responsable.getContractPath() != null && new File(responsable.getContractPath()).exists()) {
        return ResponseEntity.ok("Contrat déjà existant à : " + responsable.getContractPath());
    }

    // ✅ Générer et sauvegarder le contrat
    String contractPath = contractService.generateAndSaveContract(responsable);
    return ResponseEntity.ok("Contrat généré avec succès à : " + contractPath);
//    responsableRepository.save(responsable);


}



    @PostMapping("/uploadSignature")
    public ResponseEntity<String> uploadSignature(
            @RequestParam("file") MultipartFile file,
            @RequestParam("signerName") String signerName) {
        try {
            // 🔥 Définir un chemin sûr sur le disque (dans le dossier utilisateur)
            String signatureDir = System.getProperty("user.home") + "/signatures/";
            File directory = new File(signatureDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Création du dossier s'il n'existe pas
            }

            // 🔥 Sauvegarde de l’image dans un dossier accessible
            String filePath = signatureDir + "signature_" + signerName + ".png";
            file.transferTo(new File(filePath));

            return ResponseEntity.ok("Signature enregistrée avec succès : " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'upload : " + e.getMessage());
        }
    }
    @GetMapping("/get")
    public ResponseEntity<String> getContract(@RequestParam String nom, @RequestParam String prenom) {
        Optional<ResponsablePointDeVente> responsableOpt =
                responsableRepository.findByNomAndPrenom(nom, prenom);

        if (responsableOpt.isPresent()) {
            ResponsablePointDeVente responsable = responsableOpt.get();

            // ✅ Vérifier si le contrat existe
            if (responsable.getContractPath() != null && new File(responsable.getContractPath()).exists()) {
                return ResponseEntity.ok("Contrat disponible à : " + responsable.getContractPath());
            }

            return ResponseEntity.badRequest().body("Aucun contrat trouvé pour ce responsable. Veuillez le générer.");
        } else {
            return ResponseEntity.badRequest().body("Aucun responsable trouvé avec ce nom et prénom.");
        }
    }
    @GetMapping("/signedContract")
    public ResponseEntity<byte[]> getSignedContract(@RequestParam String nom, @RequestParam String prenom) {
        Optional<ResponsablePointDeVente> respoOpt = responsableRepository.findByNomAndPrenom(nom, prenom);
        if (respoOpt.isPresent() && respoOpt.get().getContractSigned() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"contrat_signed.pdf\"")
                    .body(respoOpt.get().getContractSigned());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/sign")
    public ResponseEntity<String> signContract(
            @RequestParam String contractName,
            @RequestParam String signerName) {
        String result = contractService.signContract(contractName, signerName);

        if (result.startsWith("Erreur")) {
            return ResponseEntity.status(404).body(result);
        }

        return ResponseEntity.ok(result);
    }

//    @GetMapping("/preview")
//    public ResponseEntity<byte[]> previewContract(@RequestParam String contractName) {
//        try {
//            File file = new File("contracts/" + contractName);
//            if (!file.exists()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            byte[] content = Files.readAllBytes(file.toPath());
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + contractName + "\"")
//                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
//                    .header("X-Frame-Options", "ALLOWALL") // 🛠 autorise l'iframe
//                    .body(content);
//
//        } catch (IOException e) {
//            return ResponseEntity.status(500).build();
//        }
//    }

    @GetMapping("/preview")
    public ResponseEntity<byte[]> previewContract(@RequestParam String contractName) {
        try {
            Path contractPath = Paths.get("contracts", contractName);
            byte[] fileBytes = Files.readAllBytes(contractPath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + contractName + "\"")
                    .contentType(MediaType.APPLICATION_PDF) // ✅ Important
                    .body(fileBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/signed/preview")
    public ResponseEntity<Resource> previewSignedContract(@RequestParam String contractName) {
        try {
            Path filePath = Paths.get("signed_contracts").resolve(contractName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + contractName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/contrat/{id}")
    public ResponseEntity<byte[]> getContratPDFPublic(@PathVariable Long id) {
        ResponsablePointDeVente responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable non trouvé"));

        byte[] contratPdf = responsable.getContractSigned();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("contrat.pdf").build());

        return new ResponseEntity<>(contratPdf, headers, HttpStatus.OK);
    }








}
