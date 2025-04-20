package com.example.asistencia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")

public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;
    
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    
    public Long getId() {
        return id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    // 🔵 Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }
}