package com.example.asistencia.model;

import java.util.ArrayList;
import java.util.List;

public class Historial {
    private String fecha;
    private String totalHoras;
    private List<Fichaje> fichajes = new ArrayList<>();

    private int minutosTotales = 0;

    public Historial(String fecha) {
        this.fecha = fecha;
    }

    public void agregarFichaje(String entrada, String salida, int minutos) {
        fichajes.add(new Fichaje(entrada, salida));
        minutosTotales += minutos;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTotalHoras() {
        int horas = minutosTotales / 60;
        int mins = minutosTotales % 60;
        return horas + "h " + mins + "m";
    }

    public List<Fichaje> getFichajes() {
        return fichajes;
    }
}