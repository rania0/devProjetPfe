package com.example.backend.service;

import com.example.backend.dto.CommandeRequest;

public class CommandeService {
    public void passerCommande(CommandeRequest commandeDTO, String emailResponsable) {
        // Tu peux utiliser l'emailResponsable pour :
        // - récupérer l'entité ResponsablePointDeVente
        // - vérifier ses droits ou sa session active
        // - enregistrer la commande avec un lien vers lui

        System.out.println("🔔 Email du responsable : " + emailResponsable);

        // Ex : ResponsablePointDeVente responsable = responsableRepository.findByEmail(emailResponsable);
        // Puis : création de la commande avec ses produits...
    }

}
