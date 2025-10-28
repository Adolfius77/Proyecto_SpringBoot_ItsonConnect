/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import Repository.EstudianteRepository;
import Repository.MatchRepository;
import Repository.MensajeRepository;
import dto.ChatMensajeDTO;
import java.util.Date;
import java.util.List;
import model.Estudiante;
import model.Match;
import model.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import service.IMensajeService;

/**
 *
 * @author adoil
 */
@Service
public class MensajeServiceImpl implements IMensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private MatchRepository matchRepository;

    @Override
    public Mensaje guardarMensaje(ChatMensajeDTO dto) throws Exception {
        Estudiante emisor = estudianteRepository.findById(dto.getEmisorId())
                .orElseThrow(() -> new Exception("emisor no encontrado"));
        Match match = matchRepository.findById(dto.getMatchId())
                .orElseThrow(() -> new Exception("match no econtrado"));

        //aqui se crea el mensaje completo plebes
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(dto.getContenido());
        mensaje.setEmisor(emisor);
        mensaje.setMatch(match);
        mensaje.setFechaHora(new Date());

        return mensajeRepository.save(mensaje);

    }

    @Override
    public Mensaje obtenerMensaje(Long id) {
        return mensajeRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarMensaje(Long id) throws Exception {
        if (!mensajeRepository.existsById(id)) {
            throw new Exception("Mensaje no encontrado");
        }
        mensajeRepository.deleteById(id);
    }

    @Override
    public List<Mensaje> listarMensajesPorMatch(Long idMatch, int limit) {
        Match match = new Match();
        match.setId(idMatch);
        return mensajeRepository.findByMatchOrderByFechaHoraAsc(match, PageRequest.of(0, limit));
    }
}
