package com.example.backend.repository;

import com.example.backend.entity.SessionCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionCommandeRepository extends JpaRepository<SessionCommande, Long> {
    List<SessionCommande> findByDateDebutBeforeAndDateFinAfter(LocalDateTime now1, LocalDateTime now2);
}
