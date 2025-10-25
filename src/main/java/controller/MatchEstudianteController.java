/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dto.MatchEstudianteDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import model.Estudiante;
import model.Match;
import model.MatchEstudiante;
import service.IMatchEstudianteService;

/**
 *
 * @author jorge
 */
@RestController
@RequestMapping("/match_estudiantes")
public class MatchEstudianteController {
    private final IMatchEstudianteService matchEstudianteService;

    public MatchEstudianteController(IMatchEstudianteService matchEstudianteService) {
        this.matchEstudianteService = matchEstudianteService;
    }

    private MatchEstudianteDTO toDTO(MatchEstudiante me) {
        if (me == null) return null;
        MatchEstudianteDTO dto = new MatchEstudianteDTO();
        dto.setId(me.getId());
        dto.setEstudianteId(me.getEstudiante() != null ? me.getEstudiante().getId() : null);
        dto.setMatchId(me.getMatch() != null ? me.getMatch().getId() : null);
        return dto;
    }

    private MatchEstudiante toEntity(MatchEstudianteDTO dto) {
        if (dto == null) return null;
        MatchEstudiante me = new MatchEstudiante();
        me.setId(dto.getId());    
        Estudiante estudiante = new Estudiante();// solo asignamos entidades por el id, sin traer realaciones
        estudiante.setId(dto.getEstudianteId());
        me.setEstudiante(estudiante);

        Match match = new Match();
        match.setId(dto.getMatchId());
        me.setMatch(match);

        return me;
    }

    // saca todos los matchestudiante y mete limite por defecto de 100
    @GetMapping
    public List<MatchEstudianteDTO> obtenerTodos(@RequestParam(defaultValue = "100") int limit) {
        return matchEstudianteService.listarMatchEstudiantes(limit)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // obtine un estudiante por id
    @GetMapping("/{id}")
    public MatchEstudianteDTO obtenerPorId(@PathVariable Long id) {
        return toDTO(matchEstudianteService.obtenerMatchEstudiante(id));
    }

    // crea el match estudiante
    @PostMapping
    public MatchEstudianteDTO registrar(@RequestBody MatchEstudianteDTO dto) throws Exception {
        MatchEstudiante me = matchEstudianteService.crearMatchEstudiante(toEntity(dto));
        return toDTO(me);
    }

    //actualiza el match
    @PutMapping("/{id}")
    public MatchEstudianteDTO actualizar(@PathVariable Long id, @RequestBody MatchEstudianteDTO dto) throws Exception {
        dto.setId(id);
        MatchEstudiante me = matchEstudianteService.actualizarMatchEstudiante(toEntity(dto));
        return toDTO(me);
    }

    // elimina un matchEstudiante por id
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) throws Exception {
        matchEstudianteService.eliminarMatchEstudiante(id);
    }

}
