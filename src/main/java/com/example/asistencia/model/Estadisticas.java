package com.example.asistencia.model;

import java.util.Map;

public class Estadisticas {
    private String horasTrabajadas;
    private int diasAsistidos;
    private int diasFaltados;
    private Map<String, Boolean> resumenSemanal;

    public Estadisticas(String horasTrabajadas, int diasAsistidos, int diasFaltados, Map<String, Boolean> resumenSemanal) {
        this.horasTrabajadas = horasTrabajadas;
        this.diasAsistidos = diasAsistidos;
        this.diasFaltados = diasFaltados;
        this.resumenSemanal = resumenSemanal;
    }

    public String getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public int getDiasAsistidos() {
        return diasAsistidos;
    }

    public int getDiasFaltados() {
        return diasFaltados;
    }

    public Map<String, Boolean> getResumenSemanal() {
        return resumenSemanal;
    }
}