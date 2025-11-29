package controller;

import Mappers.InterracionMapper;
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
    //mapper interaccion
    @Autowired
    private InterracionMapper interracionMapper;


    //Endpoints
    @GetMapping
    public List<InteraccionDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return interaccionService.listarInteracciones(limit)
                .stream()
                .map(interracionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
       try {
            Interaccion interaccion = interaccionService.obtenerInteraccion(id);
            return ResponseEntity.ok(interracionMapper.toDTO(interaccion));
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody InteraccionDTO dto) {
       try {
            Interaccion interaccion = interracionMapper.toEntity(dto);
            Interaccion creada = interaccionService.crearInteraccion(interaccion);
          
            return ResponseEntity.status(HttpStatus.CREATED).body(interracionMapper.toDTO(creada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody InteraccionDTO dto) {
        try {
            dto.setId(id);
            Interaccion interaccion = interracionMapper.toEntity(dto);
            Interaccion actualizada = interaccionService.actualizarInteraccion(interaccion);
            return ResponseEntity.ok(interracionMapper.toDTO(actualizada));
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