/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import java.util.List;
import model.MatchEstudiante;

/**
 *
 * @author jorge
 */
public interface IMatchEstudianteService {
    MatchEstudiante crearMatchEstudiante(MatchEstudiante matchEstudiante) throws Exception;
    MatchEstudiante obtenerMatchEstudiante(Long id);
    MatchEstudiante actualizarMatchEstudiante(MatchEstudiante matchEstudiante) throws Exception;
    void eliminarMatchEstudiante(Long id) throws Exception;
    List<MatchEstudiante> listarMatchEstudiantes(int limit);
}
