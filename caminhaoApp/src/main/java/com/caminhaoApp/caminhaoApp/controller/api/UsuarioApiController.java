package com.caminhaoApp.caminhaoApp.controller.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caminhaoApp.caminhaoApp.model.Usuario;
import com.caminhaoApp.caminhaoApp.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class UsuarioApiController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint para realizar o Login via API
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCpf(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Verifica se é primeiro acesso (senha está vazia no banco)
            if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
                return ResponseEntity.status(403)
                    .body("PRIMEIRO_ACESSO: Usuário encontrado, mas a senha ainda não foi cadastrada.");
            }

            // Valida a senha criptografada
            if (passwordEncoder.matches(password, usuario.getSenha())) {
                return ResponseEntity.ok(usuario);
            }
        }
        return ResponseEntity.status(401).body("CPF ou Senha inválidos");
    }

    /**
     * Endpoint para o usuário cadastrar a senha no primeiro acesso
     */
    @PostMapping("/cadastrar-senha")
    public ResponseEntity<?> cadastrarSenha(@RequestParam String cpf, @RequestParam String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCpf(cpf);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Segurança: impede sobrescrever senha se ela já existir (opcional)
            if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário já possui senha cadastrada.");
            }

            // Criptografa a senha antes de salvar
            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Senha cadastrada com sucesso! Agora você já pode realizar o login.");
        }

        return ResponseEntity.status(404).body("Usuário não encontrado.");
    }
}





/*package com.caminhaoApp.caminhaoApp.controller.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caminhaoApp.caminhaoApp.model.Usuario;
import com.caminhaoApp.caminhaoApp.repository.RotaRepository;
import com.caminhaoApp.caminhaoApp.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class UsuarioApiController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RotaRepository rotaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestParam String cpf, @RequestParam String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCpf(cpf);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return ResponseEntity.ok(usuario);
            }
        }
        return ResponseEntity.status(401).body("CPF ou Senha inválidos");
    }
}*/