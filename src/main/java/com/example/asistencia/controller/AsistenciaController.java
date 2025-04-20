package com.example.asistencia.controller;


import com.example.asistencia.model.Asistencia;
import com.example.asistencia.model.Estadisticas;
import com.example.asistencia.model.Historial;
import com.example.asistencia.repository.AsistenciaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@RestController
@RequestMapping("/asistencias")
public class AsistenciaController {
    @Autowired
    private AsistenciaRepository repository;
    

    // Obtener todas las asistencias
    @GetMapping
    public List<Asistencia> getAll() {
        return repository.findAll();
    }

    // Obtener una asistencia por ID
    @GetMapping("/{id}")
    public Optional<Asistencia> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Crear una asistencia
    @PostMapping
    public Asistencia create(@RequestBody Asistencia asistencia) {
        return repository.save(asistencia);
    }

    // Actualizar una asistencia
    @PutMapping("/{id}")
    public Asistencia update(@PathVariable Long id, @RequestBody Asistencia asistenciaDetails) {
        return repository.findById(id).map(asistencia -> {
            asistencia.setHoraEntrada(asistenciaDetails.getHoraEntrada());
            asistencia.setHoraSalida(asistenciaDetails.getHoraSalida());
            return repository.save(asistencia);
        }).orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
    }

    // Eliminar una asistencia
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
    
    @DeleteMapping
    public void deleteAll() {
        repository.deleteAll();
    }
    
    @GetMapping("/ultimo")
    public ResponseEntity<Asistencia> getAsistenciaHoySinSalida(@RequestParam Long empleadoId) {
        Optional<Asistencia> asistencia = repository.findAsistenciaDeHoySinSalida(empleadoId);
        return asistencia.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/ficharSalida")
    public Asistencia ficharSalida(@RequestParam Long empleadoId) {
        LocalDate hoy = LocalDate.now();

        Optional<Asistencia> asistenciaDeHoy = repository.findTopByEmpleadoIdAndHoraSalidaIsNullAndHoraEntradaBetweenOrderByHoraEntradaDesc(
            empleadoId,
            hoy.atStartOfDay(),
            hoy.plusDays(1).atStartOfDay()
        );

        if (asistenciaDeHoy.isPresent()) {
            Asistencia asistencia = asistenciaDeHoy.get();
            asistencia.setHoraSalida(LocalDateTime.now());
            return repository.save(asistencia);
        } else {
            throw new RuntimeException("No hay entrada previa registrada hoy.");
        }
    }
    


    @GetMapping("/estadisticas")
    public Estadisticas getEstadisticas(@RequestParam Long empleadoId) {
        List<Asistencia> asistencias = repository.findByEmpleadoId(empleadoId);

        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(6); // Últimos 7 días incluyendo hoy

        int totalMinutos = 0;
        int diasAsistidos = 0;
        Map<String, Boolean> resumen = new LinkedHashMap<>();

        // 🔁 Recorremos 7 días
        for (int i = 0; i < 7; i++) {
            LocalDate dia = inicioSemana.plusDays(i);
            boolean asistio = asistencias.stream()
                .anyMatch(a -> a.getHoraEntrada().toLocalDate().equals(dia));
            if (asistio) diasAsistidos++;
            resumen.put(dia.toString(), asistio);
        }

        // ⏱️ Sumar horas trabajadas
        for (Asistencia a : asistencias) {
            if (a.getHoraEntrada() != null && a.getHoraSalida() != null) {
                Duration duracion = Duration.between(a.getHoraEntrada(), a.getHoraSalida());
                totalMinutos += duracion.toMinutes();
            }
        }

        int horas = totalMinutos / 60;
        int minutos = totalMinutos % 60;
        int diasFaltados = 7 - diasAsistidos;

        return new Estadisticas(horas + "h " + minutos + "m", diasAsistidos, diasFaltados, resumen);
    }
    
    
    @GetMapping("/historial")
    public List<Historial> historialPorFecha(@RequestParam Long empleadoId) {
        List<Asistencia> asistencias = repository.findByEmpleadoId(empleadoId);

        // Agrupamos por fecha con orden descendente
        Map<String, Historial> mapa = new TreeMap<>(Collections.reverseOrder());

        for (Asistencia a : asistencias) {
            if (a.getHoraEntrada() != null && a.getHoraSalida() != null) {
                String fecha = a.getHoraEntrada().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String entrada = a.getHoraEntrada().toLocalTime().toString().substring(0, 5);
                String salida = a.getHoraSalida().toLocalTime().toString().substring(0, 5);

                int minutos = (int) Duration.between(a.getHoraEntrada(), a.getHoraSalida()).toMinutes();

                mapa.computeIfAbsent(fecha, f -> new Historial(f))
                    .agregarFichaje(entrada, salida, minutos);
            }
        }

        return new ArrayList<>(mapa.values());
    }

}