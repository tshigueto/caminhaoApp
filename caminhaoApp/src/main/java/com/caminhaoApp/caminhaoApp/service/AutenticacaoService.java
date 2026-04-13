package com.caminhaoApp.caminhaoApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.caminhaoApp.caminhaoApp.model.Usuario;
import com.caminhaoApp.caminhaoApp.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String cpfLimpo = username.replaceAll("[^0-9]", "").trim();

        System.out.println("Tentativa de login com CPF: " + cpfLimpo);

        Usuario usuario = repository.findByCpf(cpfLimpo)
                .orElseThrow(() -> {
                    System.out.println("ERRO: CPF " + cpfLimpo + " não encontrado no banco!");
                    return new UsernameNotFoundException("Usuário não encontrado");
                });

        System.out.println("Usuário encontrado: " + usuario.getNome() + " | Perfil: " + usuario.getPerfil());
        
        return usuario;
    }
}
