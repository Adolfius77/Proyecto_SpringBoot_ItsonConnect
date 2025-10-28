package controller;

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

    //Convertidores
    private MatchEstudianteDTO toDTO(MatchEstudiante me) {
        if (me == null) {
            return null;
        }
        MatchEstudianteDTO dto = new MatchEstudianteDTO();
        dto.setId(me.getId());
        dto.setEstudianteId(me.getEstudiante() != null ? me.getEstudiante().getId() : null);
        dto.setMatchId(me.getMatch() != null ? me.getMatch().getId() : null);
        return dto;
    }

    private MatchEstudiante toEntity(MatchEstudianteDTO dto) {
        if (dto == null) {
            return null;
        }
        MatchEstudiante me = new MatchEstudiante();
        me.setId(dto.getId());
        if (dto.getEstudianteId() != null) {
            me.setEstudiante(new Estudiante(dto.getEstudianteId()));
        }
        if (dto.getMatchId() != null) {
            me.setMatch(new Match());
            me.getMatch().setId(dto.getMatchId());
        }
        return me;
    }

    //Endpoints
    @GetMapping
    public List<MatchEstudianteDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return matchEstudianteService.listarMatchEstudiantes(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            MatchEstudiante me = matchEstudianteService.obtenerMatchEstudiante(id);
            return ResponseEntity.ok(toDTO(me));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody MatchEstudianteDTO dto) {
        try {
            MatchEstudiante me = toEntity(dto);
            MatchEstudiante creado = matchEstudianteService.crearMatchEstudiante(me);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(creado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody MatchEstudianteDTO dto) {
        try {
            dto.setId(id);
            MatchEstudiante me = toEntity(dto);
            MatchEstudiante actualizado = matchEstudianteService.actualizarMatchEstudiante(me);
            return ResponseEntity.ok(toDTO(actualizado));
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
