/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import java.util.List;
import model.Hobby;

/**
 *
 * @author jorge
 */
public interface IHobbyService {
    Hobby crearHobby(Hobby hobby) throws Exception;
    Hobby obtenerHobby(Long id);
    Hobby actualizarHobby(Hobby hobby) throws Exception;
    void eliminarHobby(Long id) throws Exception;
    List<Hobby> listarHobbies(int limit);
}
