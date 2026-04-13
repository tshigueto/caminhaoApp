package com.caminhaoApp.caminhaoApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caminhaoApp.caminhaoApp.model.Rota;
import com.caminhaoApp.caminhaoApp.model.Usuario;

public interface RotaRepository extends JpaRepository<Rota, Long> {
    List<Rota> findByMotorista(Usuario motorista);
    List<Rota> findByMotoristaId(Long motoristaId);
}
