package service.impl;

import Repository.EstudianteRepository;
import Repository.HobbyEstudianteRepository;
import Repository.HobbyRepository;
import Repository.MatchEstudianteRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import model.Estudiante;
import model.Hobby;
import model.HobbyEstudiante;
import model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.IEstudianteService;

@Service
public class EstudianteServiceImpl implements IEstudianteService {

    @Autowired
    private MatchEstudianteRepository matchEstudianteRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private HobbyEstudianteRepository hobbyEstudianteRepository;

    @Override
    public Estudiante login(String correo, String password) throws Exception {

        if (correo == null || correo.isBlank()) {
            throw new Exception("El correo es obligatorio.");
        }
        if (password == null || password.isBlank()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        //checa si el correo existe en la base de datos
        Estudiante estudiante = estudianteRepository.findByCorreo(correo)
                .orElseThrow(() -> new Exception("Correo incorrecto o no existe"));
        
      
        if (!estudiante.getPassword().equals(password)) {
            throw new Exception("contraseña incorrecta");
        }
        return estudiante;
    }

    @Override
    @Transactional
    public Estudiante crearEstudiante(Estudiante estudiante, Set<String> hobbyNames) throws Exception {

        if (estudiante.getNombre() == null || estudiante.getNombre().isBlank()) {
            throw new Exception("El nombre es obligatorio.");
        }
        if (estudiante.getApPaterno() == null || estudiante.getApPaterno().isBlank()) {
            throw new Exception("El apellido paterno es obligatorio.");
        }
        if (estudiante.getApMaterno() == null || estudiante.getApMaterno().isBlank()) {
            throw new Exception("El apellido materno es obligatorio.");
        }
        if (estudiante.getCorreo() == null || estudiante.getCorreo().isBlank()) {
            throw new Exception("El correo es obligatorio.");
        }
        if (estudiante.getPassword() == null || estudiante.getPassword().isBlank()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        if (!estudiante.getCorreo().contains("@") || !estudiante.getCorreo().contains(".")) {
            throw new Exception("El formato del correo no es valido.");
        }

        String correo = estudiante.getCorreo().toLowerCase();
        if (!correo.endsWith("@potros.itson.edu.mx") && !correo.endsWith("@itson.mx")) {
            throw new Exception("Debe ser un correo institucional (@potros.itson.edu.mx o @itson.mx).");
        }

        if (estudianteRepository.findByCorreo(estudiante.getCorreo()).isPresent()) {
            throw new Exception("El correo '" + estudiante.getCorreo() + "' ya está registrado.");
        }
        if (estudiante.getCarrera() == null || estudiante.getCarrera().isBlank() || estudiante.getCarrera().equals("Seleccione Carrera")) {
            throw new Exception("Debe seleccionar una carrera valida.");
        }
        if (estudiante.getCarrera() == null || estudiante.getCarrera().isBlank() || estudiante.getCarrera().equals("Seleccione Carrera")) {
            throw new Exception("Debe seleccionar una carrera valida.");
        }
        if (estudiante.getGenero() == null || estudiante.getApPaterno().isBlank()) {
            throw new Exception("El genero es obigatorio");
        }

        estudiante.setFechaRegistro(new Date());
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);

        if (hobbyNames != null && !hobbyNames.isEmpty()) {
            for (String nombreHobby : hobbyNames) {
                Hobby hobby = hobbyRepository.findByNombre(nombreHobby).orElse(null);

                if (hobby != null) {
                    HobbyEstudiante hobbyEstudiante = new HobbyEstudiante();
                    hobbyEstudiante.setEstudiante(estudianteGuardado);
                    hobbyEstudiante.setHobby(hobby);
                    hobbyEstudianteRepository.save(hobbyEstudiante);
                } else {
                    System.err.println("Advertencia: No se encontro el hobby con nombre: " + nombreHobby);
                }
            }
        }

        return estudianteGuardado;
    }

    @Override
    public Estudiante obtenerEstudiante(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        if (estudiante != null) {
            estudiante.getHobbies().size();
        }
        return estudiante;
    }

    @Override
    @Transactional
    public Estudiante actualizarEstudiante(Estudiante estudiante, Set<String> nombreHobbies) throws Exception {
        Estudiante existente = estudianteRepository.findById(estudiante.getId())
                .orElseThrow(() -> new Exception("Estudiante no encontrado, no se puede actualizar."));

        existente.setNombre(estudiante.getNombre());
        existente.setApPaterno(estudiante.getApPaterno());
        existente.setApMaterno(estudiante.getApMaterno());
        existente.setCarrera(estudiante.getCarrera());
        existente.setGenero(estudiante.getGenero());

        if (estudiante.getFoto() != null && estudiante.getFoto().length > 0) {
            existente.setFoto(estudiante.getFoto());
        }

        if (nombreHobbies != null) {
            List<model.HobbyEstudiante> hobbiesAnteriores = hobbyEstudianteRepository.findByEstudiante(existente);
            hobbyEstudianteRepository.deleteAll(hobbiesAnteriores);

            for (String nombreHobby : nombreHobbies) {
                model.Hobby hobby = hobbyRepository.findByNombre(nombreHobby).orElse(null);
                if (hobby != null) {
                    model.HobbyEstudiante nuevoHobby = new model.HobbyEstudiante();
                    nuevoHobby.setEstudiante(existente);
                    nuevoHobby.setHobby(hobby);
                    hobbyEstudianteRepository.save(nuevoHobby);
                }
            }
        }

        return estudianteRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarEstudiante(Long id) throws Exception {
        if (!estudianteRepository.existsById(id)) {
            throw new Exception("Estudiante no encontrado con id: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    @Override
    public List<Estudiante> listarEstudiantes(int limit) {
        int effectiveLimit = Math.min(Math.max(limit, 1), 100);
        Pageable pageable = PageRequest.of(0, effectiveLimit);

        List<Estudiante> estudiantes = estudianteRepository.findAll(pageable).getContent();

        for (Estudiante e : estudiantes) {
            e.getHobbies().size();
        }
        return estudiantes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estudiante> descubrirEstudiantes(Long idActual, int limit) {
        int effectiveLimit = Math.min(Math.max(limit, 1), 50);
        Pageable pageable = PageRequest.of(0, effectiveLimit);

        List<Estudiante> estudiantes = estudianteRepository.findEstudiantesParaDescubrir(idActual, pageable);
        for (Estudiante e : estudiantes) {
            e.getHobbies().size();
        }
        return estudiantes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> obtenerMatchesPorEstudiante(Long idEstudiante) {
        Estudiante estudiante = new Estudiante(idEstudiante);
        List<model.MatchEstudiante> relaciones = matchEstudianteRepository.findByEstudiante(estudiante);

        return relaciones.stream()
                .map(relacion -> {
                    Match match = relacion.getMatch();
                    match.getParticipantes().size();
                    match.getParticipantes().forEach(p -> {
                        p.getEstudiante().getNombre();
                    });
                    return match;
                })
                .collect(Collectors.toList());
    }
}
