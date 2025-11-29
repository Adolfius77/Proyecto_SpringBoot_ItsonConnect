package Mappers;

import dto.HobbyDTO;
import model.Hobby;
import org.springframework.stereotype.Component;

@Component
public class HobbyMapper {
    public HobbyDTO toDTO(Hobby hobby) {
        if (hobby == null) return null;
        HobbyDTO dto = new HobbyDTO();
        dto.setId(hobby.getId());
        dto.setNombre(hobby.getNombre());
        dto.setDescripcion(hobby.getDescripcion());
        return dto;
    }

    public Hobby toEntity(HobbyDTO dto) {
        if (dto == null) return null;
        Hobby hobby = new Hobby();
        hobby.setId(dto.getId());
        hobby.setNombre(dto.getNombre());
        hobby.setDescripcion(dto.getDescripcion());
        return hobby;
    }
}
