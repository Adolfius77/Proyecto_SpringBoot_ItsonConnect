package Mappers;

import dto.ChatMensajeDTO;
import model.Estudiante;
import model.Match;
import model.Mensaje;
import org.springframework.stereotype.Component;

@Component
public class MensajeMapper {

    public ChatMensajeDTO toDTO(Mensaje entity) {
        if (entity == null) return null;

        ChatMensajeDTO dto = new ChatMensajeDTO();
        dto.setContenido(entity.getContenido());

        if (entity.getEmisor() != null) {
            dto.setEmisorId(entity.getEmisor().getId());
            dto.setEmisorNombre(entity.getEmisor().getNombre());
        }

        if (entity.getMatch() != null) {
            dto.setMatchId(entity.getMatch().getId());
        }

        return dto;
    }

    public Mensaje toEntity(ChatMensajeDTO dto) {
        if (dto == null) return null;

        Mensaje entity = new Mensaje();
        entity.setContenido(dto.getContenido());

        if (dto.getEmisorId() != null) {
            entity.setEmisor(new Estudiante(dto.getEmisorId()));
        }
        if (dto.getMatchId() != null) {
            Match match = new Match();
            match.setId(dto.getMatchId());
            entity.setMatch(match);
        }

        return entity;
    }
}
