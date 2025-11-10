package controller;

import dto.EstudianteDTO;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IEstudianteService;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;
import dto.MatchDTO;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import model.Hobby;
import model.HobbyEstudiante;
import service.IHobbyService;
import service.impl.EstudianteServiceImpl;


/**
 *
 * @author jorge
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteServiceImpl estudianteService;
    
    @Autowired
    public EstudianteController(EstudianteServiceImpl estudianteService) {
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

    // --- ENDPOINTS PÚBLICOS DEL API ---

    /**
     * Obtiene todos los estudiantes con un límite.
     */
    @GetMapping
    public List<EstudianteDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return estudianteService.listarEstudiantes(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un estudiante por su ID.
     * Devuelve 200 OK o 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Estudiante e = estudianteService.obtenerEstudiante(id);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Estudiante no encontrado con id: " + id);
        }
        return ResponseEntity.ok(toDTO(e));
    }

    /**
     * Registra un nuevo estudiante.
     * Devuelve 201 Created o 400 Bad Request.
     */
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody EstudianteDTO dto) {
        try {
            Estudiante e = estudianteService.crearEstudiante(toEntity(dto), dto.getHobbies());
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(e));
        } catch (Exception e) {
            // Esto atrapa el error si el correo ya existe
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Actualiza un estudiante existente por ID.
     * Devuelve 200 OK o 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody EstudianteDTO dto) {
        try {
            dto.setId(id);
            Estudiante e = estudianteService.actualizarEstudiante(toEntity(dto), dto.getHobbies());
            return ResponseEntity.ok(toDTO(e));
        } catch (Exception e) {
            // Esto atrapa el error si el estudiante no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Elimina un estudiante por ID.
     * Devuelve 204 No Content o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            estudianteService.eliminarEstudiante(id);
            // 204 No Content es la respuesta estándar para un DELETE exitoso
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Endpoint de Login.
     * Devuelve 200 OK o 401 Unauthorized.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EstudianteDTO dto) {
        try {
            // Llama al método de login eficiente del servicio
            Estudiante e = estudianteService.login(dto.getCorreo(), dto.getPassword());
            return ResponseEntity.ok(toDTO(e));
        } catch (Exception e) {
            // Devuelve 401 No Autorizado si las credenciales son incorrectas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}