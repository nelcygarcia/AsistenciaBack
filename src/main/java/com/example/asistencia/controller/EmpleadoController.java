package com.example.asistencia.controller;

import com.example.asistencia.model.Asistencia;
import com.example.asistencia.model.Empleado;
import com.example.asistencia.repository.AsistenciaRepository;
import com.example.asistencia.repository.EmpleadoRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoRepository repository;
    
    @Autowired
    private AsistenciaRepository repositoryAsistencia;

    // Obtener todos los empleados
    @GetMapping
    public List<Empleado> getAll() {
        return repository.findAll();
    }

    // Obtener un empleado por ID
    @GetMapping("/{id}")
    public Optional<Empleado> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Crear un empleado
    @PostMapping
    public Empleado create(@RequestBody Empleado empleado) {
        return repository.save(empleado);
    }

    // Actualizar un empleado
    @PutMapping("/{id}")
    public Empleado update(@PathVariable Long id, @RequestBody Empleado empleadoDetails) {
        return repository.findById(id).map(empleado -> {
            empleado.setNombre(empleadoDetails.getNombre());
            empleado.setApellido(empleadoDetails.getApellido());
            empleado.setDni(empleadoDetails.getDni());
            return repository.save(empleado);
        }).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    // Eliminar un empleado
   // @DeleteMapping("/{id}")
   // public void delete(@PathVariable Long id) {
    //    repository.deleteById(id);
   // }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Empleado> empleadoOpt = repository.findById(id);

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();

            // ✅ NUEVO: Eliminar asistencias manualmente
            List<Asistencia> asistencias = repositoryAsistencia.findByEmpleadoId(empleado.getId());
            for (Asistencia asistencia : asistencias) {
            	repositoryAsistencia.delete(asistencia);
            }

            // Ahora sí puedes borrar el empleado
            repository.delete(empleado);

        } else {
            throw new RuntimeException("Empleado no encontrado");
        }
    }
    
    @DeleteMapping
    public void deleteAll() {
        repository.deleteAll();
    }
}