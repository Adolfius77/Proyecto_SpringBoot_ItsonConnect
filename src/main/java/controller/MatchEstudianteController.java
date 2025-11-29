package controller;

import Mappers.MatchEstudianteMapper;
import dto.MatchEstudianteDTO;
import model.Estudiante;
import model.Match;
import model.MatchEstudiante;
import service.IMatchEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/match_estudiantes")
public class MatchEstudianteController {

    private final IMatchEstudianteService matchEstudianteService;

    @Autowired
    public MatchEstudianteController(IMatchEstudianteService matchEstudianteService) {
        this.matchEstudianteService = matchEstudianteService;
    }
    //mapper MatchEstudiante
    @Autowired
    private MatchEstudianteMapper matchEstudianteMapper;

    //Endpoints
    @GetMapping
    public List<MatchEstudianteDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return matchEstudianteService.listarMatchEstudiantes(limit)
                .stream()
                .map(matchEstudianteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            MatchEstudiante me = matchEstudianteService.obtenerMatchEstudiante(id);
            return ResponseEntity.ok(matchEstudianteMapper.toDTO(me));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody MatchEstudianteDTO dto) {
        try {
            MatchEstudiante me = matchEstudianteMapper.toEntity(dto);
            MatchEstudiante creado = matchEstudianteService.crearMatchEstudiante(me);
            return ResponseEntity.status(HttpStatus.CREATED).body(matchEstudianteMapper.toDTO(creado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody MatchEstudianteDTO dto) {
        try {
            dto.setId(id);
            MatchEstudiante me = matchEstudianteMapper.toEntity(dto);
            MatchEstudiante actualizado = matchEstudianteService.actualizarMatchEstudiante(me);
            return ResponseEntity.ok(matchEstudianteMapper.toDTO(actualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            matchEstudianteService.eliminarMatchEstudiante(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
