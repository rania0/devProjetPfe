package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livreur")
public class LivreurController {

    @GetMapping("/livraisons")
    public String livraisons() {
        return "Bienvenue dans la gestion des livraisons";
    }
}
