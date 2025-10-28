package service.impl;

import Repository.InteraccionRepository;
import Repository.MatchRepository; 
import Repository.MatchEstudianteRepository; 
import java.util.Date;
import java.util.HashSet; 
import java.util.List;
import java.util.Optional;
import java.util.Set; 
import model.Estudiante;
import model.Interaccion;
import model.Interaccion.TipoInteraccion;
import model.Match; 
import model.MatchEstudiante; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 
import service.IInteraccionService;

@Service
public class InteraccionServicempl implements IInteraccionService {

    @Autowired
    private InteraccionRepository interaccionRepository;
    @Autowired
    private MatchRepository matchRepository; 
    @Autowired
    private MatchEstudianteRepository matchEstudianteRepository; 

    @Override
    @Transactional 
    public Interaccion crearInteraccion(Interaccion interaccion) throws Exception {
        if (interaccion.getEmisor() == null || interaccion.getReceptor() == null || interaccion.getTipo() == null) {
            throw new Exception("Emisor, receptor y tipo son obligatorios.");
        }
        if (interaccion.getEmisor().getId().equals(interaccion.getReceptor().getId())) {
            throw new Exception("Un usuario no puede interactuar consigo mismo.");
        }

        // Validacion: Fecha no futura
        Date ahora = new Date();
        if (interaccion.getFechaHora() == null) {
            interaccion.setFechaHora(ahora);
        } else if (interaccion.getFechaHora().after(ahora)) {
            throw new Exception("La fecha de la interaccion no puede ser futura.");
        }

        Estudiante emisor = interaccion.getEmisor();
        Estudiante receptor = interaccion.getReceptor();
        TipoInteraccion tipo = interaccion.getTipo();

        // Validacion: No duplicar LIKE/SUPERLIKE 
        if (tipo == TipoInteraccion.LIKE || tipo == TipoInteraccion.SUPERLIKE) {
            Optional<Interaccion> existente = interaccionRepository.buscarLikeOSuperlike(emisor, receptor);
            if (existente.isPresent()) {
                throw new Exception("Ya existe una interacción de tipo LIKE o SUPERLIKE de " + emisor.getId() + " hacia " + receptor.getId());
            }
        }

        // Guardar la nueva interaccion
        Interaccion nuevaInteraccion = interaccionRepository.save(interaccion);

        // Lógica de Creacion de Match
        // Solo si la nueva interacción es LIKE o SUPERLIKE
        if (tipo == TipoInteraccion.LIKE || tipo == TipoInteraccion.SUPERLIKE) {
            // Buscar si el receptor ya había dado LIKE/SUPERLIKE al emisor
            Optional<Interaccion> interaccionReceptor = interaccionRepository.buscarLikeOSuperlike(receptor, emisor);

            if (interaccionReceptor.isPresent()) {
                // Crear el Match y los MatchEstudiante
                System.out.println(" ¡Hay un match entre ID " + emisor.getId() + " y ID " + receptor.getId() + "!");

                // Verificar si ya existe un match entre ellos para no duplicarlo
                Optional<Match> matchExistente = matchRepository.buscarMatchExistente(emisor, receptor);
                if (matchExistente.isEmpty()) {
                    Match nuevoMatch = new Match();
                    nuevoMatch.setFecha(new Date()); // Fecha actual del match
                    Match matchGuardado = matchRepository.save(nuevoMatch);

                    // Crear las relaciones MatchEstudiante
                    MatchEstudiante me1 = new MatchEstudiante();
                    me1.setEstudiante(emisor);
                    me1.setMatch(matchGuardado);

                    MatchEstudiante me2 = new MatchEstudiante();
                    me2.setEstudiante(receptor);
                    me2.setMatch(matchGuardado);

                    // Guardar las relaciones
                    matchEstudianteRepository.save(me1);
                    matchEstudianteRepository.save(me2);

                   
                    Set<MatchEstudiante> participantes = new HashSet<>();
                    participantes.add(me1);
                    participantes.add(me2);
                    matchGuardado.setParticipantes(participantes);
                    matchRepository.save(matchGuardado); // Actualizar match con participantes

                } else {
                    System.out.println("Match ya existia entre ID " + emisor.getId() + " y ID " + receptor.getId());
                }
            }
        }

        return nuevaInteraccion;
    }

    @Override
    public Interaccion obtenerInteraccion(Long id) throws Exception {
        return interaccionRepository.findById(id)
                .orElseThrow(() -> new Exception("No se encontro la interaccion con id " + id));
    }

    @Override
    @Transactional
    public Interaccion actualizarInteraccion(Interaccion interaccion) throws Exception {
        Interaccion existente = obtenerInteraccion(interaccion.getId()); 

        // Validacion: Fecha no futura
        if (interaccion.getFechaHora() != null && interaccion.getFechaHora().after(new Date())) {
            throw new Exception("La fecha de la interaccion no puede ser futura.");
        }

        existente.setEmisor(interaccion.getEmisor() != null ? interaccion.getEmisor() : existente.getEmisor());
        existente.setReceptor(interaccion.getReceptor() != null ? interaccion.getReceptor() : existente.getReceptor());
        existente.setTipo(interaccion.getTipo() != null ? interaccion.getTipo() : existente.getTipo());
        existente.setFechaHora(interaccion.getFechaHora() != null ? interaccion.getFechaHora() : existente.getFechaHora());

        return interaccionRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarInteraccion(Long id) throws Exception {
        if (!interaccionRepository.existsById(id)) {
            throw new Exception("No se encontro la interaccion con id " + id);
        }
        interaccionRepository.deleteById(id);
    }

    @Override
    public List<Interaccion> listarInteracciones(int limit) {
        // Validacion: Limite <= 100 
        int effectiveLimit = Math.min(Math.max(limit, 1), 100);
        Pageable pageable = PageRequest.of(0, effectiveLimit);
        return interaccionRepository.findAll(pageable).getContent();
    }
}
