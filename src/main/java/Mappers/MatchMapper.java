package Mappers;

import dto.EstudianteDTO;
import dto.MatchDTO;
import model.Match;
import model.MatchEstudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MatchMapper {

    @Autowired
    private EstudianteMapper estudianteMapper;

    public MatchDTO toDTO(Match entidad) {
        if (entidad == null) {
            return null;
        }
        MatchDTO dto = new MatchDTO();
        dto.setId(entidad.getId());
        dto.setFecha(entidad.getFecha() != null ? entidad.getFecha().toString() : null);

        if (entidad.getParticipantes() != null) {
            List<EstudianteDTO> participantesDTO = entidad.getParticipantes().stream()
                    .map(MatchEstudiante::getEstudiante)
                    .map(estudianteMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setParticipantes(participantesDTO);
        } else {
            dto.setParticipantes(Collections.emptyList());
        }
        return dto;
    }

    public Match toEntity(MatchDTO dto) {
        if (dto == null) {
            return null;
        }
        Match entidad = new Match();
        entidad.setId(dto.getId());
        return entidad;
    }
}
