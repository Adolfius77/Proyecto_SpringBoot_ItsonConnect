package controller;

import Mappers.EstudianteMapper;
import Mappers.MatchMapper;
import dto.EstudianteDTO;
import dto.MatchDTO;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IMensajeService;
import service.impl.EstudianteServiceImpl;

/**
 *
 * @author jorge
 */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    //mapper de estudiantes
    @Autowired
    private EstudianteMapper estudianteMapper;
    @Autowired
    private final EstudianteServiceImpl estudianteService;
    //mapper macthes
    @Autowired
    private MatchMapper matchMapper;

    private final IMensajeService mensajeService;

    @Autowired
    public EstudianteController(EstudianteServiceImpl estudianteService, IMensajeService mensajeService) {
        this.estudianteService = estudianteService;
        this.mensajeService = mensajeService;
    }

    @GetMapping
    public List<EstudianteDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return estudianteService.listarEstudiantes(limit)
                .stream()
                .map(estudianteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Estudiante e = estudianteService.obtenerEstudiante(id);
        if (e == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Estudiante no encontrado con id: " + id);
        }
        return ResponseEntity.ok(estudianteMapper.toDTO(e));
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody EstudianteDTO dto) {
        try {
            Estudiante e = estudianteMapper.toEntity(dto);
            Estudiante estudianteGuardado = estudianteService.crearEstudiante(e, dto.getHobbies());

            return ResponseEntity.status(HttpStatus.CREATED).body(estudianteMapper.toDTO(estudianteGuardado));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody EstudianteDTO dto) {
        try {
            dto.setId(id);
            Estudiante estudianteParaActualizar = estudianteMapper.toEntity(dto);

            if (dto.getFotoBase64() != null && !dto.getFotoBase64().isEmpty()) { 
                try {
                    byte[] fotoBytes = Base64.getDecoder().decode(dto.getFotoBase64()); 
                    estudianteParaActualizar.setFoto(fotoBytes); 
                } catch (IllegalArgumentException ex) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de foto Base64 invalido.");
                }
            }

            Estudiante e = estudianteService.actualizarEstudiante(estudianteParaActualizar, dto.getHobbies()); 

            return ResponseEntity.ok(estudianteMapper.toDTO(e));

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
            return ResponseEntity.ok(estudianteMapper.toDTO(e));
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
                    .map(estudianteMapper::toDTO) // Usa el toDTO completo
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al buscar estudiantes: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/matches")
    public ResponseEntity<?> obtenerMatches(@PathVariable Long id) {
        try {
            List<model.Match> matches = estudianteService.obtenerMatchesPorEstudiante(id);
            List<MatchDTO> matchDTOs = matches.stream()
                    .map(matchMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(matchDTOs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cargar matches: " + e.getMessage());
        }
    }
}
