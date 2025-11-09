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

/**
 *
 * @author jorge
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final IEstudianteService estudianteService;

    @Autowired
    public EstudianteController(IEstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    private MatchDTO toMatchDTO(model.Match match) {
        if (match == null) {
            return null;
        }
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setFecha(match.getFecha() != null ? match.getFecha().toString() : null);
        List<EstudianteDTO> participantesDTO = Optional.ofNullable(match.getParticipantes())
                .orElse(Collections.emptySet())
                .stream()
                .map(me -> toDTO(me.getEstudiante()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        dto.setParticipantes(participantesDTO);

        return dto;
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

        if (e.getFoto() != null) {
            dto.setFotoBase64(Base64.getEncoder().encodeToString(e.getFoto()));
        }

        if (e.getHobbies() != null) {
            Set<String> hobbyNames = e.getHobbies().stream()
                    .map(hobbyEstudiante -> hobbyEstudiante.getHobby().getNombre())
                    .collect(Collectors.toSet());
            dto.setHobbies(hobbyNames);
        }

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

        if (dto.getFotoBase64() != null && !dto.getFotoBase64().isEmpty()) {
            try {
                e.setFoto(Base64.getDecoder().decode(dto.getFotoBase64()));
            } catch (IllegalArgumentException ex) {
                System.err.println("Error al decodificar foto Base64: " + ex.getMessage());
            }
        }
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
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Estudiante e = estudianteService.obtenerEstudiante(id);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Estudiante no encontrado con id: " + id);
        }
        return ResponseEntity.ok(toDTO(e));
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody EstudianteDTO dto) {
        try {
            Estudiante estudiante = toEntity(dto);

            Estudiante e = estudianteService.crearEstudiante(estudiante, dto.getHobbies());

            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody EstudianteDTO dto) {
        try {
            dto.setId(id);

            Estudiante e = estudianteService.actualizarEstudiante(toEntity(dto),dto.getHobbies());
            return ResponseEntity.ok(toDTO(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            estudianteService.eliminarEstudiante(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EstudianteDTO dto) {
        try {
            Estudiante e = estudianteService.login(dto.getCorreo(), dto.getPassword());
            return ResponseEntity.ok(toDTO(e));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/descubrir")
    public ResponseEntity<?> descubrirEstudiantes(
            @RequestParam Long idActual,
            @RequestParam(defaultValue = "20") int limit) {

        if (idActual == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El parametro 'idActual' es obligatorio.");
        }

        try {
            List<Estudiante> estudiantes = estudianteService.descubrirEstudiantes(idActual, limit);

            List<EstudianteDTO> dtos = estudiantes.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al buscar estudiantes: " + e.getMessage());
        }
    }

    @GetMapping("/{idEstudiante}/matches")
    public ResponseEntity<?> getMatchesDelEstudiante(@PathVariable Long idEstudiante) {
        try {
            List<model.Match> matches = estudianteService.obtenerMatchesPorEstudiante(idEstudiante);
            
            List<MatchDTO> dtos = matches.stream()
                    .map(this::toMatchDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al obtener matches: " + e.getMessage());
        }
    }
}
