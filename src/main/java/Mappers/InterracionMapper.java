package Mappers;

import dto.InteraccionDTO;
import model.Estudiante;
import model.Interaccion;
import org.springframework.stereotype.Component;

@Component
public class InterracionMapper {

    public InteraccionDTO toDTO(Interaccion interaccion) {
        if (interaccion == null) {
            return null;
        }
        return new InteraccionDTO(
                interaccion.getId(),
                interaccion.getEmisor() != null ? interaccion.getEmisor().getId() : null,
                interaccion.getReceptor() != null ? interaccion.getReceptor().getId() : null,
                interaccion.getTipo() != null ? interaccion.getTipo().name() : null,
                interaccion.getFechaHora()
        );
    }

    public Interaccion toEntity(InteraccionDTO dto) {
        if (dto == null) {
            return null;
        }

        Interaccion interaccion = new Interaccion();
        interaccion.setId(dto.getId());

        if (dto.getEmisorId() != null) {
            interaccion.setEmisor(new Estudiante(dto.getEmisorId()));
        }
        if (dto.getReceptorId() != null) {
            interaccion.setReceptor(new Estudiante(dto.getReceptorId()));
        }

        if (dto.getTipo() != null) {
            try {
                interaccion.setTipo(Interaccion.TipoInteraccion.valueOf(dto.getTipo().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("Tipo de interacción invalido recibido: " + dto.getTipo());
            }
        }
        interaccion.setFechaHora(dto.getFechaHora());
        return interaccion;
    }

}
