package com.example.backend.service;

import com.example.backend.dto.ResponsablePointDeVenteDTO;
import com.example.backend.entity.PtVente;
import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.StatutResponsable;
import com.example.backend.repository.PtVenteRepository;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsablePointDeVenteService {

    @Autowired
    private ResponsablePointDeVenteRepository responsableRepository;

    @Autowired
    private PtVenteRepository ptVenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ContractService contractService;
    @Autowired
    private ResponsablePointDeVenteRepository responsablePointDeVenteRepository;
    @Autowired
    private EmailService emailService;


//    public String inscrireResponsable(ResponsablePointDeVenteDTO dto) {
//        Optional<PtVente> pointDeVenteOpt = ptVenteRepository.findById(dto.getPointDeVenteId());
//
//        if (pointDeVenteOpt.isEmpty()) {
//            return "Erreur: Point de vente non trouvé.";
//        }
//
//        ResponsablePointDeVente responsable = ResponsablePointDeVente.builder()
//                .nom(dto.getNom())
//                .prenom(dto.getPrenom())
//                .email(dto.getEmail())
//                .motDePasse(passwordEncoder.encode(dto.getMotDePasse()))
//                .telephone(dto.getTelephone())
//                .cin(dto.getCin())
//                .adresse(dto.getAdresse())
//                .typeActivite(dto.getTypeActivite())
//                .pointDeVente(pointDeVenteOpt.get())
//                .status(StatutResponsable.EN_ATTENTE)
//                .build();
//
//        responsableRepository.save(responsable);
//
//        // Appeler la génération du contrat ici
//        String contractPath = contractService.generateAndSaveContract(responsable);
//
//        return "Inscription réussie. Contrat généré : " + contractPath;
//    }

    public String inscrireResponsable(ResponsablePointDeVenteDTO dto) {
        Optional<PtVente> pointDeVenteOpt = ptVenteRepository.findById(dto.getPointDeVenteId());

        if (pointDeVenteOpt.isEmpty()) {
            return "Erreur: Point de vente non trouvé.";
        }

        ResponsablePointDeVente responsable = ResponsablePointDeVente.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .motDePasse(passwordEncoder.encode(dto.getMotDePasse()))
                .rawPassword(dto.getMotDePasse())
                .telephone(dto.getTelephone())
                .cin(dto.getCin())
                .adresse(dto.getAdresse())
                .typeActivite(dto.getTypeActivite())
                .pointDeVente(pointDeVenteOpt.get())
                .status(StatutResponsable.EN_ATTENTE)
                .build();

        responsableRepository.save(responsable);

        return "Inscription réussie. Vous devez générer le contrat manuellement.";
    }
//    public List<ResponsablePointDeVenteDTO> getDemandesEnAttente() {
//        List<ResponsablePointDeVente> responsables = responsablePointDeVenteRepository.findByStatus(StatutResponsable.EN_ATTENTE);
//
//
//        return responsables.stream().map(respo -> {
//            ResponsablePointDeVenteDTO dto = new ResponsablePointDeVenteDTO();
//            dto.setId(respo.getId());
//            dto.setNom(respo.getNom());
//            dto.setPrenom(respo.getPrenom());
//            dto.setEmail(respo.getEmail());
//            dto.setTelephone(respo.getTelephone());
//            dto.setCin(respo.getCin());
//            dto.setAdresse(respo.getAdresse());
//            dto.setTypeActivite(respo.getTypeActivite());
//            dto.setPointDeVenteId(respo.getPointDeVente().getIdP());
//            return dto;
//        }).toList();
//    }
public List<ResponsablePointDeVenteDTO> getDemandesEnAttente() {
    List<ResponsablePointDeVente> responsables = responsablePointDeVenteRepository.findByStatus(StatutResponsable.EN_ATTENTE);


    return responsables.stream().map(respo -> {
        ResponsablePointDeVenteDTO dto = new ResponsablePointDeVenteDTO();
        dto.setId(respo.getId());
        dto.setNom(respo.getNom());
        dto.setPrenom(respo.getPrenom());
        dto.setEmail(respo.getEmail());
        dto.setTelephone(respo.getTelephone());
        dto.setCin(respo.getCin());
        dto.setAdresse(respo.getAdresse());
        dto.setTypeActivite(respo.getTypeActivite());
        dto.setPointDeVenteId(respo.getPointDeVente().getIdP());
        dto.setContratUrl("http://localhost:9090/api/contract/contrat/" + respo.getId());

        return dto;
    }).toList();
}
    public void accepterDemande(Long responsableId) {
        ResponsablePointDeVente responsable = responsableRepository.findById(responsableId)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        responsable.setStatus(StatutResponsable.ACCEPTE);
        responsableRepository.save(responsable);

        // Récupérer le mot de passe original depuis rawPassword (déjà assigné dans confirm)
        String rawPassword = responsable.getRawPassword(); // ❌ sera null car pas en DB


        // Préparer et envoyer le mail
        try {
            emailService.envoyerEmailConfirmationResponsable(responsable, rawPassword);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
    public void refuserDemande(Long responsableId, String raison) {
        ResponsablePointDeVente responsable = responsableRepository.findById(responsableId)
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        responsable.setStatus(StatutResponsable.REFUSE); // tu peux adapter le nom du statut si besoin
        responsableRepository.save(responsable);

        try {
            emailService.envoyerEmailRefusResponsable(responsable, raison);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de refus : " + e.getMessage());
        }
    }
    public List<ResponsablePointDeVenteDTO> getDemandesTraitees() {
        List<StatutResponsable> statuts = List.of(StatutResponsable.ACCEPTE, StatutResponsable.REFUSE);
        List<ResponsablePointDeVente> responsables = responsableRepository.findByStatusIn(statuts);

        return responsables.stream().map(respo -> {
            ResponsablePointDeVenteDTO dto = new ResponsablePointDeVenteDTO();
            dto.setId(respo.getId());
            dto.setNom(respo.getNom());
            dto.setPrenom(respo.getPrenom());
            dto.setEmail(respo.getEmail());
            dto.setTelephone(respo.getTelephone());
            dto.setCin(respo.getCin());
            dto.setAdresse(respo.getAdresse());
            dto.setTypeActivite(respo.getTypeActivite());
            dto.setPointDeVenteId(respo.getPointDeVente().getIdP());
            dto.setStatus(respo.getStatus().name()); // ✅ ici on renvoie le statut (ACCEPTE ou REFUSEE)
            return dto;
        }).toList();
    }










}
