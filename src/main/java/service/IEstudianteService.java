/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import java.util.List;
import java.util.Set;
import model.Estudiante;

/**
 *
 * @author USER
 */
public interface IEstudianteService {

    Estudiante crearEstudiante(Estudiante estudiante, Set<String> hobbyNames) throws Exception;
    Estudiante obtenerEstudiante(Long id);
    Estudiante actualizarEstudiante(Estudiante estudiante) throws Exception;
    void eliminarEstudiante(Long id) throws Exception;
    List<Estudiante> listarEstudiantes(int limit);
}
