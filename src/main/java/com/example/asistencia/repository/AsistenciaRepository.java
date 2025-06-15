package com.example.asistencia.repository;

import com.example.asistencia.model.Asistencia;
import com.example.asistencia.model.Empleado;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
	
	List<Asistencia> findByEmpleadoId(Long empleadoId);
	
	void deleteByEmpleado(Empleado empleado);
	
	Optional<Asistencia> findTopByEmpleadoIdAndHoraSalidaIsNullAndHoraEntradaBetweenOrderByHoraEntradaDesc(
		    Long empleadoId,
		    LocalDateTime inicio,
		    LocalDateTime fin
		);
	
	@Query("SELECT a FROM Asistencia a WHERE a.empleado.id = :empleadoId AND a.horaSalida IS NULL AND DATE(a.horaEntrada) = CURRENT_DATE ORDER BY a.horaEntrada DESC")
	Optional<Asistencia> findAsistenciaDeHoySinSalida(@Param("empleadoId") Long empleadoId);
}