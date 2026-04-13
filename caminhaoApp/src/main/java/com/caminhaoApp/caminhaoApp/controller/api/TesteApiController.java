package com.caminhaoApp.caminhaoApp.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teste")
public class TesteApiController {

    @GetMapping
    public Map<String, String> teste() {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("status", "API funcionando!");
        resposta.put("sincronizacao", "O banco de dados é o mesmo do site");
        return resposta;
    }
}
