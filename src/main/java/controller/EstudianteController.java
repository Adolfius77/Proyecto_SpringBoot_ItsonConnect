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

/**
 *
 * @author jorge
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final IEstudianteService estudianteService;
    private final IHobbyService hobbyService;

    @Autowired
    public EstudianteController(IEstudianteService estudianteService, IHobbyService hobbyService) {
        this.estudianteService = estudianteService;
        this.hobbyService = hobbyService;
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

    public EstudianteDTO toDTO(Estudiante estudiante) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(estudiante.getId());
        dto.setNombre(estudiante.getNombre());
        dto.setApPaterno(estudiante.getApPaterno());
        dto.setApMaterno(estudiante.getApMaterno());
        dto.setCorreo(estudiante.getCorreo());
        dto.setPassword(estudiante.getPassword());
        dto.setFechaRegistro(estudiante.getFechaRegistro().toString());

        if (estudiante.getFoto() != null) {
            dto.setFotoBase64(Base64.getEncoder().encodeToString(estudiante.getFoto()));
        }

        Set<String> hobbyNombres = estudiante.getHobbies().stream()
            .map(h -> h.getHobby().getNombre())
            .collect(Collectors.toSet());
        dto.setHobbies(hobbyNombres);

        return dto;
    }

    public Estudiante toEntity(EstudianteDTO dto, Set<Hobby> todosLosHobbies) {
        Estudiante e = new Estudiante();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setApPaterno(dto.getApPaterno());
        e.setApMaterno(dto.getApMaterno());
        e.setCorreo(dto.getCorreo());
        e.setPassword(dto.getPassword());
        e.setFechaRegistro(java.sql.Date.valueOf(dto.getFechaRegistro()));

        if (dto.getFotoBase64() != null) {
            e.setFoto(Base64.getDecoder().decode(dto.getFotoBase64()));
        }

        // convertir nombres de hobbies → objetos HobbyEstudiante
        Set<HobbyEstudiante> hobbyEstudiantes = dto.getHobbies().stream()
            .map(nombre -> {
                Hobby hobby = todosLosHobbies.stream()
                    .filter(h -> h.getNombre().equals(nombre))
                    .findFirst()
                    .orElse(null);
                if (hobby != null) {
                    HobbyEstudiante he = new HobbyEstudiante();
                    he.setEstudiante(e);
                    he.setHobby(hobby);
                    return he;
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        e.setHobbies(hobbyEstudiantes);

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
            Set<Hobby> todosLosHobbies = estudianteService.;
            Estudiante estudiante = toEntity(dto, todosLosHobbies);
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
