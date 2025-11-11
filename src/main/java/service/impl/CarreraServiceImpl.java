package service.impl;

import Repository.CarreraRepository;
import java.util.Arrays;
import java.util.List;
import model.Carrera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.ICarreraService;

@Service
public class CarreraServiceImpl implements ICarreraService {

    @Autowired
    private CarreraRepository carreraRepository;

    @Override
    public Page<Carrera> listarCarreras(String nombre, Pageable pageable) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            return carreraRepository.findByNombreContainingIgnoreCase(nombre, pageable);
        }
        return carreraRepository.findAll(pageable);
    }

    @Override
    public Carrera crearCarrera(Carrera carrera) throws Exception {
        if (carreraRepository.existsById(carrera.getId())) {
            throw new Exception("Carrera ya existe");
        }
        return carreraRepository.save(carrera);
    }
    
    @Override
    public void precargarCarreras() {
        if (carreraRepository.count() == 0) {
            List<String> nombresCarreras = Arrays.asList(
                "Ingeniería en Software",
                "Ingeniería Industrial y de Sistemas",
                "Ingeniería Civil",
                "Ingeniería Mecatrónica",
                "Ingeniería Biomédica",
                "Ingeniería Química",
                "Licenciatura en Administración",
                "Licenciatura en Contaduría Pública",
                "Licenciatura en Psicología",
                "Licenciatura en Diseño Gráfico",
                "Medicina Veterinaria y Zootecnia",
                "Licenciatura en Gastronomía",
                "Licenciatura en Educación Física",
                "Licenciatura en Economía y Finanzas"
                
            );
            
            for (String nombre : nombresCarreras) {
                carreraRepository.save(new Carrera(nombre));
            }
            System.out.println(">>> Carreras precargadas.");
        }
    }
}