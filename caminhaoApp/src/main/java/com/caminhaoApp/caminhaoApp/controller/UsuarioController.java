package com.caminhaoApp.caminhaoApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.caminhaoApp.caminhaoApp.model.Usuario;
import com.caminhaoApp.caminhaoApp.repository.RotaRepository;
import com.caminhaoApp.caminhaoApp.repository.UsuarioRepository;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private RotaRepository rotaRepository;
    
    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/usuarios/novo")
    public String formulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro-usuario"; 
    }

    @PostMapping("/usuarios/salvar")
    public String salvar(Usuario usuario, @AuthenticationPrincipal Usuario usuarioLogado) {
        if ("EMPRESA".equals(usuarioLogado.getPerfil()) && "ADMIN".equals(usuario.getPerfil())) {
            return "redirect:/usuarios/listar?error=permissao_negada";
        }

        if (usuario.getCpf() != null) {
            usuario.setCpf(usuario.getCpf().replaceAll("[^0-9]", ""));
        }
        
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty() && !usuario.getSenha().startsWith("$2a$")) {
            usuario.setSenha(encoder.encode(usuario.getSenha()));
        }
        
        repository.save(usuario);
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido"));
        model.addAttribute("usuario", usuario);
        return "cadastro-usuario"; 
    }

    @GetMapping("/usuarios/excluir/{id}")
    public String excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        Usuario usuarioParaExcluir = repository.findById(id).orElseThrow();

        if ("EMPRESA".equals(usuarioLogado.getPerfil()) && "ADMIN".equals(usuarioParaExcluir.getPerfil())) {
            return "redirect:/usuarios/listar?error=nao_pode_excluir_admin";
        }

        if (usuarioLogado.getId().equals(id)) {
            return "redirect:/usuarios/listar?error=auto_exclusao_proibida";
        }

        repository.deleteById(id);
        return "redirect:/usuarios/listar?success=usuario_excluido";
    }

    @GetMapping("/usuarios/listar")
    public String listar(Model model, @AuthenticationPrincipal Usuario usuarioLogado) {
        model.addAttribute("usuarios", repository.findAll());
        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("rotas", rotaRepository.findAll());

        List<Usuario> motoristas = repository.findByPerfil("CAMINHONEIRO");
        model.addAttribute("listaMotoristas", motoristas);

        return "lista-usuarios";
    }
}