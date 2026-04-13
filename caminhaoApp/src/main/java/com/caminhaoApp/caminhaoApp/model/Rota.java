package com.caminhaoApp.caminhaoApp.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cepPartida;
    private String cepDestino;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime horarioSaida;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime inicioEntrega;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fimEntrega;

    private String fotoChegadaPath;
    private String fotoFinalizacaoPath;

    @Column(name = "entrega_concluida", nullable = false)
    private boolean entregaConcluida = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "motorista_id")
    private Usuario motorista;

    public Rota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCepPartida() { return cepPartida; }
    public void setCepPartida(String cepPartida) { this.cepPartida = cepPartida; }

    public String getCepDestino() { return cepDestino; }
    public void setCepDestino(String cepDestino) { this.cepDestino = cepDestino; }

    public LocalDateTime getHorarioSaida() { return horarioSaida; }
    public void setHorarioSaida(LocalDateTime horarioSaida) { this.horarioSaida = horarioSaida; }

    public LocalDateTime getInicioEntrega() { return inicioEntrega; }
    public void setInicioEntrega(LocalDateTime inicioEntrega) { this.inicioEntrega = inicioEntrega; }

    public LocalDateTime getFimEntrega() { return fimEntrega; }
    public void setFimEntrega(LocalDateTime fimEntrega) { this.fimEntrega = fimEntrega; }

    public String getFotoChegadaPath() { return fotoChegadaPath; }
    public void setFotoChegadaPath(String fotoChegadaPath) { this.fotoChegadaPath = fotoChegadaPath; }

    public String getFotoFinalizacaoPath() { return fotoFinalizacaoPath; }
    public void setFotoFinalizacaoPath(String fotoFinalizacaoPath) { this.fotoFinalizacaoPath = fotoFinalizacaoPath; }

    public boolean isEntregaConcluida() { return entregaConcluida; }
    public void setEntregaConcluida(boolean entregaConcluida) { this.entregaConcluida = entregaConcluida; }

    public Usuario getMotorista() { return motorista; }
    public void setMotorista(Usuario motorista) { this.motorista = motorista; }
}
