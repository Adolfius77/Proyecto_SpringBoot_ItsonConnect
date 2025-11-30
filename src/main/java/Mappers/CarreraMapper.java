package Mappers;

import dto.CarreraDTO;
import model.Carrera;
import org.springframework.stereotype.Component;

@Component
public class CarreraMapper {

    public CarreraDTO toDTO(Carrera carrera) {
        CarreraDTO dto = new CarreraDTO();
        dto.setId(carrera.getId());
        dto.setNombre(carrera.getNombre());
        return dto;
    }

    public Carrera toEntity(CarreraDTO dto) {
        if (dto == null) {
            return null;
        }
        Carrera entity = new Carrera();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }
}
