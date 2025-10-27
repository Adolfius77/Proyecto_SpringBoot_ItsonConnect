/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import Repository.MatchRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import model.Estudiante;
import model.Match;
import model.MatchEstudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.IMatchService;

/**
 *
 * @author luisb
 */
@Service
public class MatchServiceImpl implements IMatchService{
    @Autowired
    MatchRepository matchRepository;
    
    @Override
    public Match crearMatch(Match match) throws Exception {
        if(matchRepository.existsById(match.getId())) {
            throw new Exception("Ya existe un match con id " + match.getId());
        }
        List<Estudiante> estudiantesParticipantes = match.getParticipantes().stream()
        .map(MatchEstudiante::getEstudiante)
        .toList();
        
        for (int i = 0; i < estudiantesParticipantes.size(); i++) {
            Estudiante e1 = estudiantesParticipantes.get(i);
            for (int j = i + 1; j < estudiantesParticipantes.size(); j++) {
                Estudiante e2 = estudiantesParticipantes.get(j);

                boolean yaHayMatch = e1.getMatchEstudiantes().stream()
                    .anyMatch(me -> me.getMatch().getParticipantes().stream()
                        .anyMatch(other -> other.getEstudiante().equals(e2))
                    );

                if (yaHayMatch) {
                    throw new Exception("Ya existe un match previo entre " + e1.getNombre() + " y " + e2.getNombre());
                }
            }
        }

        return matchRepository.save(match);
        }
        
    

    @Override
    public Match obtenerMatch(Long id) throws Exception{
       Optional<Match> existente = matchRepository.findById(id);
       
       if(existente.isPresent()) {
           return existente.get();
       } else {
           throw new Exception("No se encontro el match con id " + id);
       }
    }

    @Override
    public Match actualizarMatch(Match match) throws Exception {
        Optional<Match> existenteOpt = matchRepository.findById(match.getId());
        
        Match existente = existenteOpt.orElseThrow(
            () -> new Exception("No se encontro la interaccion con id " + match.getId())
        );
        
        existente.setFecha(match.getFecha());
        existente.setMensajes(match.getMensajes());
        existente.setParticipantes(match.getParticipantes());
        
        return matchRepository.save(existente);        
    }

    @Override
    public void eliminarMatch(Long id) throws Exception {
        Optional<Match> existenteOpt = matchRepository.findById(id);
        
        Match existente = existenteOpt.orElseThrow(
            () -> new Exception("No se encontro la interacción con id " + id)
        );
        
        matchRepository.delete(existente);
    }

    @Override
    public List<Match> listarMatches(int limit) {
        Pageable pageable = PageRequest.of(0, limit); 
        return matchRepository.findAll(pageable).getContent();
    }
    
    
    
}
