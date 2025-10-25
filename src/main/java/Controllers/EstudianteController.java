/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;


import DTOS.EstudianteDTO;
import java.util.List;
import java.util.stream.Collectors;
import model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.IEstudianteService;
/**
 *
 * @author jorge
 */
public class EstudianteController {
    
    private final IEstudianteService estudianteService;

    public EstudianteController(IEstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    private EstudianteDTO toDTO(Estudiante e) {
        if (e == null) {
            return null;
        }
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setApPaterno(e.getApPaterno());
        dto.setApMaterno(e.getApMaterno());
        dto.setCorreo(e.getCorreo());
        dto.setPassword(e.getPassword()); 
        dto.setFechaRegistro(e.getFechaRegistro() != null ? e.getFechaRegistro().toString() : null);
        return dto;
    }

    private Estudiante toEntity(EstudianteDTO dto) {
        if (dto == null) {
            return null;
        }
        Estudiante e = new Estudiante();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setApPaterno(dto.getApPaterno());
        e.setApMaterno(dto.getApMaterno());
        e.setCorreo(dto.getCorreo());
        e.setPassword(dto.getPassword());
        return e;
    }
    
    @GetMapping
    public List<EstudianteDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return estudianteService.listarEstudiantes(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EstudianteDTO obtenerPorId(@PathVariable Long id) {
        return toDTO(estudianteService.obtenerEstudiante(id));
    }

    @PostMapping
    public EstudianteDTO registrar(@RequestBody EstudianteDTO dto) throws Exception {
        Estudiante e = estudianteService.crearEstudiante(toEntity(dto));
        return toDTO(e);
    }

    @PutMapping("/{id}")
    public EstudianteDTO actualizar(@PathVariable Long id, @RequestBody EstudianteDTO dto) throws Exception {
        dto.setId(id);
        Estudiante e = estudianteService.actualizarEstudiante(toEntity(dto));
        return toDTO(e);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) throws Exception {
        estudianteService.eliminarEstudiante(id);
    }

    @PostMapping("/login")
    public EstudianteDTO login(@RequestBody EstudianteDTO dto) {
        // Aquí asumir que tu servicio devuelve la entidad
        Estudiante e = estudianteService.listarEstudiantes(100) // ejemplo simple
                .stream()
                .filter(est -> est.getCorreo().equals(dto.getCorreo())
                && est.getPassword().equals(dto.getPassword()))
                .findFirst()
                .orElse(null);
        return toDTO(e);
    }
}
