package Mappers;

import dto.MatchEstudianteDTO;
import model.Estudiante;
import model.Match;
import model.MatchEstudiante;
import org.springframework.stereotype.Component;

@Component
public class MatchEstudianteMapper {

    public MatchEstudianteDTO toDTO(MatchEstudiante me) {
        if (me == null) {
            return null;
        }
        MatchEstudianteDTO dto = new MatchEstudianteDTO();
        dto.setId(me.getId());
        dto.setEstudianteId(me.getEstudiante() != null ? me.getEstudiante().getId() : null);
        dto.setMatchId(me.getMatch() != null ? me.getMatch().getId() : null);

        return dto;
    }

    public MatchEstudiante toEntity(MatchEstudianteDTO dto) {
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
}
