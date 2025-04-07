package com.example.backend.repository;


import com.example.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    boolean existsByCin(String cin);
    Optional<Utilisateur> findByMail(String mail);

    boolean existsByMail(String mail);
    Optional<Utilisateur> findByRefreshToken(String refreshToken);
    List<Utilisateur> findByRole(String role);

    List<Utilisateur> findByCinContaining(String cin);

    List<Utilisateur> findByNomContainingIgnoreCase(String nom);

    List<Utilisateur> findByPrenomContainingIgnoreCase(String prenom);

    List<Utilisateur> findByMailContainingIgnoreCase(String mail);

    List<Utilisateur> findByNumTelContaining(String numTel);
    @Query("SELECT u FROM Utilisateur u WHERE u.role <> 'admin'")
    List<Utilisateur> findAllNonAdminUsers();
    List<Utilisateur> findByRoleAndType(String role, String type);


}