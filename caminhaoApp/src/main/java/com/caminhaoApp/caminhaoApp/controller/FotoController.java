package com.caminhaoApp.caminhaoApp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.caminhaoApp.caminhaoApp.model.Rota;
import com.caminhaoApp.caminhaoApp.repository.RotaRepository;

@Controller
@RequestMapping("/fotos")
public class FotoController {

    @Autowired
    private RotaRepository rotaRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/upload/{id}/chegada")
    public String uploadChegada(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoChegadaPath(salvarArquivo(arquivo));
        
        rota.setInicioEntrega(LocalDateTime.now());
        
        rotaRepository.save(rota);
        return "redirect:/usuarios/listar";
    }

    @PostMapping("/upload/{id}/finalizacao")
    public String uploadFinalizacao(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoFinalizacaoPath(salvarArquivo(arquivo));
        
        rota.setFimEntrega(LocalDateTime.now());
        
        rota.setEntregaConcluida(true);
        
        rotaRepository.save(rota);
        return "redirect:/usuarios/listar";
    }

    private String salvarArquivo(MultipartFile arquivo) throws IOException {
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) Files.createDirectories(root);
        
        String nomeUnico = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
        Files.copy(arquivo.getInputStream(), root.resolve(nomeUnico), StandardCopyOption.REPLACE_EXISTING);
        return nomeUnico;
    }
}
