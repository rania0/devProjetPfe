package com.example.backend.repository;


import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.StatutResponsable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponsablePointDeVenteRepository extends JpaRepository<ResponsablePointDeVente, Long> {
    Optional<ResponsablePointDeVente> findByEmail(String email);
    Optional<ResponsablePointDeVente> findByNomAndPrenom(String nom, String prenom);
    List<ResponsablePointDeVente> findByStatus(StatutResponsable status);
    List<ResponsablePointDeVente> findByStatusIn(List<StatutResponsable> statuts);



}

