package controller;

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

    private EstudianteDTO toEstudianteDTO(model.Estudiante e) {
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
            Set<String> hobbyNombres = e.getHobbies().stream()
                    .map(hobbyEstudiante -> hobbyEstudiante.getHobby().getNombre())
                    .collect(Collectors.toSet());
        }
        return dto;
    }

    //Convertidores
    private MatchDTO toDTO(Match match) {
        if (match == null) {
            return null;
        }
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setFecha(match.getFecha() != null ? match.getFecha().toString() : null);

        List<EstudianteDTO> participantesDTO = Optional.ofNullable(match.getParticipantes())
                .orElse(Collections.emptySet())
                .stream()
                .map(me -> toEstudianteDTO(me.getEstudiante()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        dto.setParticipantes(participantesDTO);

        return dto;
    }

    private Match toEntitySimple(MatchDTO dto) {
        if (dto == null) {
            return null;
        }
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
