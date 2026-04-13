package com.caminhaoApp.caminhaoApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.caminhaoApp.caminhaoApp.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCpf(String cpf);

    List<Usuario> findByPerfil(String perfil);

    @Query("SELECT u FROM Usuario u WHERE REPLACE(REPLACE(u.cpf, '.', ''), '-', '') = :cpf")
    Optional<Usuario> buscarPorCpfLimpo(@Param("cpf") String cpf);

}