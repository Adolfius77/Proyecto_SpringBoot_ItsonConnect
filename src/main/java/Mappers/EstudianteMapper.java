package Mappers;

import dto.EstudianteDTO;
import model.Estudiante;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EstudianteMapper {

    public EstudianteDTO toDTO(Estudiante e) {
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
        dto.setCarrera(e.getCarrera());
        dto.setGenero(e.getGenero());

        if (e.getFoto() != null) {
            dto.setFotoBase64(Base64.getEncoder().encodeToString(e.getFoto()));
        }

        if (e.getHobbies() != null) {
            Set<String> hobbyNombres = e.getHobbies().stream()
                    .map(hobbyEstudiante -> hobbyEstudiante.getHobby().getNombre())
                    .collect(Collectors.toSet());
            dto.setHobbies(hobbyNombres);
        }

        return dto;
    }

    public Estudiante toEntity(EstudianteDTO dto) {
        if (dto == null) {
            return null;
        }
        Estudiante e = new Estudiante();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setApPaterno(dto.getApPaterno());
        e.setApMaterno(dto.getApMaterno());
        e.setCorreo(dto.getCorreo());
        e.setPassword(dto.getPassword());
        e.setCarrera(dto.getCarrera());
        e.setGenero(dto.getGenero());

        if (dto.getFotoBase64() != null && !dto.getFotoBase64().isEmpty()) {
            try {
                byte[] fotoBytes = Base64.getDecoder().decode(dto.getFotoBase64());
                e.setFoto(fotoBytes);
            } catch (IllegalArgumentException ex) {
                System.err.println("error decodificando la foto we" + ex.getMessage());
                e.setFoto(null);
            }
        }
        return e;
    }
}
