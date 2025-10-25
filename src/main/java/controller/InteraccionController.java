/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dto.InteraccionDTO;
import java.util.List;
import java.util.stream.Collectors;
import model.Estudiante;
import model.Interaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.IInteraccionService;

/**
 *
 * @author jorge
 */

@RestController
@RequestMapping("/interacciones")
public class InteraccionController {
    @Autowired
    private IInteraccionService interaccionService;

    @GetMapping
    public List<InteraccionDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return interaccionService.listarInteracciones(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public InteraccionDTO obtenerPorId(@PathVariable Long id) {
        return toDTO(interaccionService.obtenerInteraccion(id));
    }


    @PostMapping
    public InteraccionDTO crear(@RequestBody InteraccionDTO dto) throws Exception {
        Interaccion interaccion = toEntity(dto);
        return toDTO(interaccionService.crearInteraccion(interaccion));
    }

    @PutMapping("/{id}")
    public InteraccionDTO actualizar(@PathVariable Long id, @RequestBody InteraccionDTO dto) throws Exception {
        dto.setId(id);
        Interaccion interaccion = toEntity(dto);
        return toDTO(interaccionService.actualizarInteraccion(interaccion));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) throws Exception {
        interaccionService.eliminarInteraccion(id);
    }

    private InteraccionDTO toDTO(Interaccion interaccion) {
        if (interaccion == null) return null;
        return new InteraccionDTO(
                interaccion.getId(),
                interaccion.getEmisor() != null ? interaccion.getEmisor().getId() : null,
                interaccion.getReceptor() != null ? interaccion.getReceptor().getId() : null,
                interaccion.getTipo() != null ? interaccion.getTipo().name() : null,
                interaccion.getFechaHora()
        );
    }
    
    //para q funcione el metodo debe haber un contructor en el model estudiante con solo el id evitamos consulta a base de datos
    private Interaccion toEntity(InteraccionDTO dto) {
        if (dto == null) return null;
        Interaccion interaccion = new Interaccion();
        interaccion.setId(dto.getId());
        interaccion.setEmisor(dto.getEmisorId() != null ? new Estudiante(dto.getEmisorId()) : null);
        interaccion.setReceptor(dto.getReceptorId() != null ? new Estudiante(dto.getReceptorId()) : null);
        interaccion.setTipo(Interaccion.TipoInteraccion.valueOf(dto.getTipo()));
        interaccion.setFechaHora(dto.getFechaHora());
        return interaccion;
    }
}
