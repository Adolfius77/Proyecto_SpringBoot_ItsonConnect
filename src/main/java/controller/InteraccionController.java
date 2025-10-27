package controller;

import dto.InteraccionDTO;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import model.Estudiante;
import model.Interaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IInteraccionService;

@RestController
@RequestMapping("/api/interacciones")
public class InteraccionController {

    @Autowired
    private IInteraccionService interaccionService;

    //Convertidores
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

    private Interaccion toEntity(InteraccionDTO dto) {
        if (dto == null) return null;
        Interaccion interaccion = new Interaccion();
        interaccion.setId(dto.getId());
        if(dto.getEmisorId() != null) interaccion.setEmisor(new Estudiante(dto.getEmisorId()));
        if(dto.getReceptorId() != null) interaccion.setReceptor(new Estudiante(dto.getReceptorId()));
        if(dto.getTipo() != null) {
             try {
                 interaccion.setTipo(Interaccion.TipoInteraccion.valueOf(dto.getTipo().toUpperCase()));
             } catch (IllegalArgumentException e) {
                 System.err.println("Tipo de interacción invalido recibido: " + dto.getTipo());
             }
        }
        interaccion.setFechaHora(dto.getFechaHora());
        return interaccion;
    }

    //Endpoints
    @GetMapping
    public List<InteraccionDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return interaccionService.listarInteracciones(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
       try {
            Interaccion interaccion = interaccionService.obtenerInteraccion(id);
            return ResponseEntity.ok(toDTO(interaccion));
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody InteraccionDTO dto) {
       try {
            Interaccion interaccion = toEntity(dto);
            Interaccion creada = interaccionService.crearInteraccion(interaccion);
          
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(creada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody InteraccionDTO dto) {
        try {
            dto.setId(id);
            Interaccion interaccion = toEntity(dto);
            Interaccion actualizada = interaccionService.actualizarInteraccion(interaccion);
            return ResponseEntity.ok(toDTO(actualizada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            interaccionService.eliminarInteraccion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}