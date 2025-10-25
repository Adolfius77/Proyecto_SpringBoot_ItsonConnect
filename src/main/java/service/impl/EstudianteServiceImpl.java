package service.impl;

import Repository.EstudianteRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import service.IEstudianteService;

@Service
public class EstudianteServiceImpl implements IEstudianteService {

    @Autowired 
    private EstudianteRepository estudianteRepository;

    public Estudiante login(String correo, String password) throws Exception {
        Estudiante estudiante = estudianteRepository.findByCorreo(correo)
                .orElseThrow(() -> new Exception("Correo o contraseña incorrectos"));

        if (!estudiante.getPassword().equals(password)) {
            throw new Exception("Correo o contraseña incorrectos");
        }
        return estudiante;
    }

    @Override
    public Estudiante crearEstudiante(Estudiante estudiante) throws Exception {
        if (estudianteRepository.findByCorreo(estudiante.getCorreo()).isPresent()) {
            throw new Exception("El correo ya está registrado");
        }
        estudiante.setFechaRegistro(new Date());
        return estudianteRepository.save(estudiante);
    }

    @Override
    public Estudiante obtenerEstudiante(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    @Override
    public Estudiante actualizarEstudiante(Estudiante estudiante) throws Exception {
        if (estudiante.getId() == null || !estudianteRepository.existsById(estudiante.getId())) {
            throw new Exception("Estudiante no encontrado, no se puede actualizar.");
        }
        return estudianteRepository.save(estudiante);
    }

    @Override
    public void eliminarEstudiante(Long id) throws Exception {
        if (!estudianteRepository.existsById(id)) {
            throw new Exception("Estudiante no encontrado con id: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    @Override
    public List<Estudiante> listarEstudiantes(int limit) {
        return estudianteRepository.findAll(PageRequest.of(0, limit)).getContent();
    }
}
