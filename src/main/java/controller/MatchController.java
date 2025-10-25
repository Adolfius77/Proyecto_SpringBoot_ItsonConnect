/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dto.MatchDTO;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import model.Match;
import service.IMatchService;

/**
 *
 * @author jorge
 */
@RestController
@RequestMapping("/matches")
public class MatchController {
    private final IMatchService matchService;

    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }

    private MatchDTO toDTO(Match match) {
        if (match == null) return null;
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setFecha(match.getFecha() != null ? match.getFecha().toString() : null);

        if (match.getParticipantes() != null) {
            List<Long> ids = match.getParticipantes()
                    .stream()
                    .map(me -> me.getEstudiante() != null ? me.getEstudiante().getId() : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            dto.setParticipanteIds(ids);
        } else {
            dto.setParticipanteIds(Collections.emptyList());
        }

        return dto;
    }

    private Match toEntity(MatchDTO dto) {
        if (dto == null) return null;
        Match match = new Match();
        match.setId(dto.getId());
        match.setFecha(dto.getFecha() != null ? new Date() : new Date()); 
        
        return match;
    }


    @GetMapping
    public List<MatchDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return matchService.listarMatches(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public MatchDTO obtenerPorId(@PathVariable Long id) {
        return toDTO(matchService.obtenerMatch(id));
    }

    @PostMapping
    public MatchDTO registrar(@RequestBody MatchDTO dto) throws Exception {
        Match match = matchService.crearMatch(toEntity(dto));
        return toDTO(match);
    }

    @PutMapping("/{id}")
    public MatchDTO actualizar(@PathVariable Long id, @RequestBody MatchDTO dto) throws Exception {
        dto.setId(id);
        Match match = matchService.actualizarMatch(toEntity(dto));
        return toDTO(match);
    }
    
    

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) throws Exception {
        matchService.eliminarMatch(id);
    }
    
    
    
    
    
    
    
}
