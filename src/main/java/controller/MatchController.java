package controller;

import dto.MatchDTO;
import model.Match;
import model.Estudiante;
import model.MatchEstudiante;
import service.IMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final IMatchService matchService;

    @Autowired
    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }

    //Convertidores
    private MatchDTO toDTO(Match match) {
        if (match == null) return null;
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setFecha(match.getFecha() != null ? match.getFecha().toString() : null);

        List<Long> ids = Optional.ofNullable(match.getParticipantes())
                                .orElse(Collections.emptySet())
                                .stream()
                                .map(me -> me.getEstudiante() != null ? me.getEstudiante().getId() : null)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
        dto.setParticipanteIds(ids);

        return dto;
    }

   
    private Match toEntitySimple(MatchDTO dto) {
        if (dto == null) return null;
        Match match = new Match();
        match.setId(dto.getId());
        return match;
    }

    //Endpoints
    @GetMapping
    public List<MatchDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return matchService.listarMatches(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Match match = matchService.obtenerMatch(id); 
            return ResponseEntity.ok(toDTO(match));
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody MatchDTO dto) {
       try {
           Match matchInput = new Match(); 
           Match creado = matchService.crearMatch(matchInput); 
           return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(creado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody MatchDTO dto) {
       try {
            Match matchInput = new Match();
            matchInput.setId(id); 
            Match actualizado = matchService.actualizarMatch(matchInput);
            return ResponseEntity.ok(toDTO(actualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // O NOT_FOUND
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
       try {
            matchService.eliminarMatch(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}