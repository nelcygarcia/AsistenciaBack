package com.example.asistencia.model;

public class Fichaje {
    private String entrada;
    private String salida;

    public Fichaje(String entrada, String salida) {
        this.entrada = entrada;
        this.salida = salida;
    }

    public String getEntrada() {
        return entrada;
    }

    public String getSalida() {
        return salida;
    }
}