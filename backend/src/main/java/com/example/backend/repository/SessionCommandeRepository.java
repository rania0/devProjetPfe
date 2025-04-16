package com.example.backend.repository;

import com.example.backend.entity.SessionCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionCommandeRepository extends JpaRepository<SessionCommande, Long> {
    List<SessionCommande> findByDateDebutBeforeAndDateFinAfter(LocalDateTime now1, LocalDateTime now2);
    @Query("SELECT s FROM SessionCommande s WHERE s.dateFin < CURRENT_TIMESTAMP ORDER BY s.dateFin DESC")
    List<SessionCommande> findAllByDateFinBeforeNowOrderByDateFinDesc();


}
