package controller;

import dto.HobbyDTO;
import model.Hobby;
import service.IHobbyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/hobbies")
public class HobbyController {

    private final IHobbyService hobbyService;
    @Autowired
    public HobbyController(IHobbyService hobbyService) {
        this.hobbyService = hobbyService;
    }

    //Convertidores
    private HobbyDTO toDTO(Hobby hobby) {
        if (hobby == null) return null;
        HobbyDTO dto = new HobbyDTO();
        dto.setId(hobby.getId());
        dto.setNombre(hobby.getNombre());
        dto.setDescripcion(hobby.getDescripcion());
        return dto;
    }

    private Hobby toEntity(HobbyDTO dto) {
        if (dto == null) return null;
        Hobby hobby = new Hobby();
        hobby.setId(dto.getId());
        hobby.setNombre(dto.getNombre());
        hobby.setDescripcion(dto.getDescripcion());
        return hobby;
    }

    //Endpoints
    @GetMapping
    public List<HobbyDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return hobbyService.listarHobbies(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Hobby hobby = hobbyService.obtenerHobby(id);
        if (hobby == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Hobby no encontrado con id: " + id);
        }
        return ResponseEntity.ok(toDTO(hobby));
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody HobbyDTO dto) {
        try {
            Hobby hobby = hobbyService.crearHobby(toEntity(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(hobby));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody HobbyDTO dto) {
        try {
            dto.setId(id);
            Hobby hobby = hobbyService.actualizarHobby(toEntity(dto));
            return ResponseEntity.ok(toDTO(hobby));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            hobbyService.eliminarHobby(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}