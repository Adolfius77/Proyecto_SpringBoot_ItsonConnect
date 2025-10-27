/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import Repository.HobbyRepository;
import java.util.List;
import model.Hobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import service.IHobbyService;

/**
 *
 * @author adoil
 */
@Service
public class HobbyServiceImpl implements IHobbyService {

    @Autowired
    private HobbyRepository hobbyRepository;

    @Override
    public Hobby crearHobby(Hobby hobby) throws Exception {
        if(hobbyRepository.findByNombre(hobby.getNombre()).isPresent()){
            throw new Exception("Un hobby con ese nombre ya existe");
        }
        return hobbyRepository.save(hobby);
    }

    @Override
    public Hobby obtenerHobby(Long id) {
        return hobbyRepository.findById(id).orElse(null);
    }

    @Override
    public Hobby actualizarHobby(Hobby hobby) throws Exception {
        if(hobby.getId() == null || !hobbyRepository.existsById(hobby.getId())){
           throw new Exception("Hobby no encontrado."); 
        }
        return hobbyRepository.save(hobby);
    }

    @Override
    public void eliminarHobby(Long id) throws Exception {
        if(!hobbyRepository.existsById(id)){
            throw new Exception("Hobby no encontrado con id: " + id);
            
        }
        hobbyRepository.deleteById(id);
    }

    @Override
    public List<Hobby> listarHobbies(int limit) {
        return hobbyRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

}
