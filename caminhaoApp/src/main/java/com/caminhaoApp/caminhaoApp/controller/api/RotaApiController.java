package com.caminhaoApp.caminhaoApp.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caminhaoApp.caminhaoApp.model.Rota;
import com.caminhaoApp.caminhaoApp.repository.RotaRepository;
import com.caminhaoApp.caminhaoApp.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class RotaApiController {

    @Autowired
    private RotaRepository rotaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("/motorista/{id}/rotas")
    public List<Rota> listarMinhasRotas(@PathVariable Long id) {
        return rotaRepository.findByMotoristaId(id);
    }

    @PostMapping("/rotas/{id}/iniciar")
    public ResponseEntity<Rota> iniciarViagem(
            @PathVariable Long id, 
            @RequestParam(required = false) String dataEvento) {
        
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setHorarioSaida(parseData(dataEvento));
        return ResponseEntity.ok(rotaRepository.save(rota));
    }

    @PostMapping("/rotas/{id}/chegada")
    public ResponseEntity<Rota> uploadChegada(
            @PathVariable Long id, 
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam(required = false) String dataEvento) throws IOException {
        
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoChegadaPath(salvarArquivo(arquivo));
        rota.setInicioEntrega(parseData(dataEvento));
        return ResponseEntity.ok(rotaRepository.save(rota));
    }

    @PostMapping("/rotas/{id}/finalizacao")
    public ResponseEntity<Rota> uploadFinalizacao(
            @PathVariable Long id, 
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam(required = false) String dataEvento) throws IOException {
        
        Rota rota = rotaRepository.findById(id).orElseThrow();
        rota.setFotoFinalizacaoPath(salvarArquivo(arquivo));
        rota.setFimEntrega(parseData(dataEvento));
        rota.setEntregaConcluida(true);
        return ResponseEntity.ok(rotaRepository.save(rota));
    }

    private LocalDateTime parseData(String dataStr) {
        if (dataStr != null && !dataStr.isEmpty()) {
            try { return LocalDateTime.parse(dataStr); } catch (Exception e) { }
        }
        return LocalDateTime.now();
    }

    private String salvarArquivo(MultipartFile arquivo) throws IOException {
        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) Files.createDirectories(root);
        String nomeUnico = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
        Files.copy(arquivo.getInputStream(), root.resolve(nomeUnico), StandardCopyOption.REPLACE_EXISTING);
        return nomeUnico;
    }
}
