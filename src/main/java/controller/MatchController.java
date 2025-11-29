package controller;

import Mappers.MatchMapper;
import dto.ChatMensajeDTO;
import dto.EstudianteDTO;
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
import model.Mensaje;
import service.IMensajeService;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final IMatchService matchService;
    @Autowired
    private IMensajeService mensajeService;

    @Autowired
    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }
    //mapper Match
    @Autowired
    private MatchMapper matchMapper;


    //Endpoints
    @GetMapping
    public List<MatchDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return matchService.listarMatches(limit)
                .stream()
                .map(matchMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Match match = matchService.obtenerMatch(id);
            return ResponseEntity.ok(matchMapper.toDTO(match));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody MatchDTO dto) {
        try {
            Match matchInput = new Match();
            Match creado = matchService.crearMatch(matchInput);
            return ResponseEntity.status(HttpStatus.CREATED).body(matchMapper.toDTO(creado));
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
            return ResponseEntity.ok(matchMapper.toDTO(actualizado));
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
    @GetMapping("/{matchId}/mensajes")
    public ResponseEntity<?> obtenerHistorialMensajes(
            @PathVariable Long matchId,
            @RequestParam(defaultValue = "50") int limit) {
        
        try {
            List<Mensaje> mensajes = mensajeService.listarMensajesPorMatch(matchId, limit);

            List<ChatMensajeDTO> dtos = mensajes.stream()
                    .map(m -> {
                        ChatMensajeDTO dto = new ChatMensajeDTO();
                        dto.setContenido(m.getContenido());
                        dto.setEmisorId(m.getEmisor() != null ? m.getEmisor().getId() : null);
                        dto.setEmisorNombre(m.getEmisor() != null ? m.getEmisor().getNombre() : "Sistema");
                        dto.setMatchId(m.getMatch() != null ? m.getMatch().getId() : null);
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al obtener mensajes: " + e.getMessage());
        }
    }
}
