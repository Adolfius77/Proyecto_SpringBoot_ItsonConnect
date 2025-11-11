package service;

import model.Carrera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICarreraService {
    Page<Carrera> listarCarreras(String nombre, Pageable pageable);
    Carrera crearCarrera(Carrera carrera) throws Exception;
    void precargarCarreras();
}