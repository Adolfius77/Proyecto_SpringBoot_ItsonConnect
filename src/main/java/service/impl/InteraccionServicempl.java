/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import Repository.InteraccionRepository;
import java.util.List;
import java.util.Optional;
import model.Estudiante;
import model.Interaccion;
import model.Interaccion.TipoInteraccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import service.IInteraccionService;

/**
 *
 * @author luisb
 */
@Service
public class InteraccionServicempl implements IInteraccionService{
    
    @Autowired
    private InteraccionRepository interaccionRepository;

    @Override
    public Interaccion crearInteraccion(Interaccion interaccion) throws Exception {
        Estudiante emisor = interaccion.getEmisor();
        Estudiante receptor = interaccion.getReceptor();
        TipoInteraccion tipo = interaccion.getTipo();

        Optional<Interaccion> existente = interaccionRepository.buscarLikeOSuperlike(emisor, receptor);

        if (existente.isPresent()) {
            throw new Exception("Ya existe una interacción de tipo LIKE o SUPERLIKE entre estos usuarios.");
        }

        Interaccion nueva = interaccionRepository.save(interaccion);

        Optional<Interaccion> respuesta = interaccionRepository.buscarLikeOSuperlike(receptor, emisor);
        if (respuesta.isPresent()) {
            // Aquí podrías notificar un “match”, enviar evento por WebSocket, etc.
            System.out.println("🎉 ¡Hay un match entre " + emisor.getNombre() + " y " + receptor.getNombre() + "!");
        }

        return nueva;
    }

    @Override
    public Interaccion obtenerInteraccion(Long id) throws Exception {
        Optional<Interaccion> interaccion = interaccionRepository.findById(id);

        if (interaccion.isPresent()) {
            return interaccion.get();
        } else {
            throw new Exception("No se encontró la interacción con id " + id);
        }
    }
         
    

    @Override
    public Interaccion actualizarInteraccion(Interaccion interaccion) throws Exception {
        Optional<Interaccion> existenteOpt = interaccionRepository.findById(interaccion.getId());

        Interaccion existente = existenteOpt.orElseThrow(
            () -> new Exception("No se encontró la interacción con id " + interaccion.getId())
        );
        
        existente.setEmisor(interaccion.getEmisor());
        existente.setFechaHora(interaccion.getFechaHora());
        existente.setReceptor(interaccion.getReceptor());
        existente.setTipo(interaccion.getTipo());
        
        return interaccionRepository.save(existente);
        
    }

    @Override
    public void eliminarInteraccion(Long id) throws Exception {
        Optional<Interaccion> interaccion = interaccionRepository.findById(id);

        if (interaccion.isPresent()) {
            interaccionRepository.delete(interaccion.get());
        } else {
            throw new Exception("No se encontró la interacción con id " + id);
        }
    }

    @Override
    public List<Interaccion> listarInteracciones(int limit) {
        Pageable pageable = PageRequest.of(0, limit); 
        return interaccionRepository.findAll(pageable).getContent();
    }
    
}
