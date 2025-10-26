/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import Repository.MatchEstudianteRepository;
import java.util.List;
import java.util.Optional;
import model.MatchEstudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.IMatchEstudianteService;

/**
 *jeje
 * @author luisb
 */
@Service
public class MatchEstudianteServiceImpl implements IMatchEstudianteService {
    
    @Autowired
    MatchEstudianteRepository matchEstudianteRepository;

    @Override
    public MatchEstudiante crearMatchEstudiante(MatchEstudiante matchEstudiante) throws Exception {
        if(matchEstudianteRepository.existsById(matchEstudiante.getId())) {
            throw new Exception("Ya existe un matchestudiante con id " + matchEstudiante.getId());
        }
        return matchEstudianteRepository.save(matchEstudiante);
    }

    @Override
    public MatchEstudiante obtenerMatchEstudiante(Long id) throws Exception {
        Optional<MatchEstudiante> matchEstudiante = matchEstudianteRepository.findById(id);
        
         if (matchEstudiante.isPresent()) {
            return matchEstudiante.get();
        } else {
            throw new Exception("No se encontró el matchEstudiante con id " + id);
        }
        
    }

    @Override
    public MatchEstudiante actualizarMatchEstudiante(MatchEstudiante matchEstudiante) throws Exception {
        Optional<MatchEstudiante> existenteOpt = matchEstudianteRepository.findById(matchEstudiante.getId());
        
        MatchEstudiante existente = existenteOpt.orElseThrow(
            () -> new Exception("No se encontró la matchEstudiante con id " + matchEstudiante.getId())
        );
        existente.setEstudiante(matchEstudiante.getEstudiante());
        existente.setMatch(matchEstudiante.getMatch());
        return matchEstudianteRepository.save(existente);
    }

    @Override
    public void eliminarMatchEstudiante(Long id) throws Exception {
        Optional<MatchEstudiante> existenteOpt = matchEstudianteRepository.findById(id);
        
        MatchEstudiante existente = existenteOpt.orElseThrow(
            () -> new Exception("No se encontró la matchEstudiante con id " + id)
        );
        
        matchEstudianteRepository.delete(existente);
    }

    @Override
    public List<MatchEstudiante> listarMatchEstudiantes(int limit) {
        Pageable pageable = PageRequest.of(0, limit); 
        return matchEstudianteRepository.findAll(pageable).getContent();
    }
    
}
