package com.caminhaoApp.caminhaoApp.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caminhaoApp.caminhaoApp.model.Rota;
import com.caminhaoApp.caminhaoApp.repository.RotaRepository;

@RestController
@RequestMapping("/api/fotos")
public class FotoApiController {

    @Autowired
    private RotaRepository rotaRepository;

    @Value("${upload.path}")
    private String uploadPath;

    private LocalDateTime obterDataReal(String dataEventoStr) {
        if (dataEventoStr != null && !dataEventoStr.isEmpty()) {
            try {
                return LocalDateTime.parse(dataEventoStr);
            } catch (Exception e) {
                return LocalDateTime.now();
            }
        }
        return LocalDateTime.now();
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Rota> iniciarViagem(
            @PathVariable Long id, 
            @RequestParam(value = "dataEvento", required = false) String dataEvento) {
        
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setHorarioSaida(obterDataReal(dataEvento));
        
        return ResponseEntity.ok(rotaRepository.save(rota));
    }

    @PostMapping("/upload/{id}/chegada")
    public ResponseEntity<Rota> uploadChegada(
            @PathVariable Long id, 
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam(value = "dataEvento", required = false) String dataEvento) throws IOException {
        
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoChegadaPath(salvarArquivo(arquivo));
        rota.setInicioEntrega(obterDataReal(dataEvento));
        
        return ResponseEntity.ok(rotaRepository.save(rota));
    }

    @PostMapping("/upload/{id}/finalizacao")
    public ResponseEntity<Rota> uploadFinalizacao(
            @PathVariable Long id, 
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam(value = "dataEvento", required = false) String dataEvento) throws IOException {
        
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoFinalizacaoPath(salvarArquivo(arquivo));
        rota.setFimEntrega(obterDataReal(dataEvento));
        rota.setEntregaConcluida(true);
        
        return ResponseEntity.ok(rotaRepository.save(rota));
    }

    private String salvarArquivo(MultipartFile arquivo) throws IOException {
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) Files.createDirectories(root);
        String nomeUnico = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
        Files.copy(arquivo.getInputStream(), root.resolve(nomeUnico), StandardCopyOption.REPLACE_EXISTING);
        return nomeUnico;
    }
}






/* package com.caminhaoApp.caminhaoApp.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caminhaoApp.caminhaoApp.model.Rota;
import com.caminhaoApp.caminhaoApp.repository.RotaRepository;

@RestController
@RequestMapping("/api/fotos")
public class FotoApiController {

    @Autowired
    private RotaRepository rotaRepository;

    @Value("${upload.path}")
    private String uploadPath;

    // --- PASSO 1: INICIAR VIAGEM ---
    // Registra apenas o horário de saída (ícone do caminhão 🚚)
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Rota> iniciarViagem(@PathVariable Long id) {
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setHorarioSaida(LocalDateTime.now());
        
        Rota rotaAtualizada = rotaRepository.save(rota);
        return ResponseEntity.ok(rotaAtualizada);
    }

    // --- PASSO 2: CHEGADA NO DESTINO ---
    // Registra o 1º horário de entrega (ícone 📍) e a 1ª foto
    @PostMapping("/upload/{id}/chegada")
    public ResponseEntity<Rota> uploadChegada(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoChegadaPath(salvarArquivo(arquivo));
        rota.setInicioEntrega(LocalDateTime.now());
        
        Rota rotaAtualizada = rotaRepository.save(rota);
        return ResponseEntity.ok(rotaAtualizada);
    }

    // --- PASSO 3: FINALIZAÇÃO / DESCARREGAMENTO ---
    // Registra o 2º horário de entrega (ícone 🏁), a 2ª foto e conclui a rota
    @PostMapping("/upload/{id}/finalizacao")
    public ResponseEntity<Rota> uploadFinalizacao(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoFinalizacaoPath(salvarArquivo(arquivo));
        rota.setFimEntrega(LocalDateTime.now());
        rota.setEntregaConcluida(true);
        
        Rota rotaAtualizada = rotaRepository.save(rota);
        return ResponseEntity.ok(rotaAtualizada);
    }

    // Método auxiliar para salvar o arquivo físico no disco
    private String salvarArquivo(MultipartFile arquivo) throws IOException {
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) Files.createDirectories(root);
        
        String nomeUnico = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
        Files.copy(arquivo.getInputStream(), root.resolve(nomeUnico), StandardCopyOption.REPLACE_EXISTING);
        return nomeUnico;
    }
}*/