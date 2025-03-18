package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/api/magasinier")
    public class MagasinierController {

        @GetMapping("/stocks")
        public String stocks() {
            return "Bienvenue dans la gestion des stocks";
        }
    }


