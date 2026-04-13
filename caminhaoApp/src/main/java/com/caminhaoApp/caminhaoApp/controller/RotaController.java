package com.caminhaoApp.caminhaoApp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.caminhaoApp.caminhaoApp.model.Rota;
import com.caminhaoApp.caminhaoApp.model.Usuario;
import com.caminhaoApp.caminhaoApp.repository.RotaRepository;
import com.caminhaoApp.caminhaoApp.repository.UsuarioRepository;

@Controller
@RequestMapping("/rotas")
public class RotaController {

    @Autowired
    private RotaRepository rotaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/cadastrar")
    public String exibirFormulario(Model model) {
        List<Usuario> motoristas = usuarioRepository.findByPerfil("CAMINHONEIRO");
        model.addAttribute("listaMotoristas", motoristas);
        
        return "usuarios/listar"; 
    }

    @PostMapping("/salvar")
    @Transactional
    public String salvarRota(@ModelAttribute Rota rota, @RequestParam String cpfMotorista) {
        try {
            String cpfLimpo = cpfMotorista.replaceAll("[^0-9]", "");
            Usuario motorista = usuarioRepository.findByCpf(cpfLimpo).orElse(null);

            if (motorista == null || !"CAMINHONEIRO".equals(motorista.getPerfil())) {
                return "redirect:/usuarios/listar?error=motorista_invalido";
            }

            rota.setId(null);
            rota.setMotorista(motorista);
            rotaRepository.save(rota);

            return "redirect:/usuarios/listar?success=rota_criada";
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return "redirect:/usuarios/listar?error=erro_interno_servidor";
        }
    }

    @GetMapping("/iniciar/{id}")
    @Transactional
    public String iniciarViagem(@PathVariable Long id) {
        rotaRepository.findById(id).ifPresent(r -> {
            if (r.getHorarioSaida() == null) {
                r.setHorarioSaida(LocalDateTime.now());
                rotaRepository.save(r);
            }
        });
        return "redirect:/usuarios/listar";
    }
}