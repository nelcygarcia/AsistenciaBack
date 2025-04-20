package com.example.asistencia.controller;

import com.example.asistencia.model.Administrador;
import com.example.asistencia.repository.AdministradorRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorRepository repository;

    // Obtener todos los administradores
    @GetMapping
    public List<Administrador> getAll() {
        return repository.findAll();
    }

    // Obtener un administrador por ID
    @GetMapping("/{id}")
    public Optional<Administrador> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Crear un administrador
    @PostMapping
    public Administrador create(@RequestBody Administrador administrador) {
        return repository.save(administrador);
    }

    // Actualizar un administrador
    @PutMapping("/{id}")
    public Administrador update(@PathVariable Long id, @RequestBody Administrador administradorDetails) {
        return repository.findById(id).map(admin -> {
            admin.setUsuario(administradorDetails.getUsuario());
            admin.setPassword(administradorDetails.getPassword());
            return repository.save(admin);
        }).orElseThrow(() -> new RuntimeException("Administrador no encontrado"));
    }

    // Eliminar un administrador
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
    
    @DeleteMapping
    public void deleteAll() {
        repository.deleteAll();
    }
    
    @PostMapping("/login")
    public boolean login(@RequestBody Administrador admin) {
        return repository.findAll().stream()
                .anyMatch(a -> a.getUsuario().equals(admin.getUsuario()) &&
                               a.getPassword().equals(admin.getPassword()));
    }
    
}