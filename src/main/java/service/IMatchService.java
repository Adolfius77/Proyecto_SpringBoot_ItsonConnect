/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import java.util.List;
import model.Match;

/**
 *
 * @author jorge
 */
public interface IMatchService {
    Match crearMatch(Match match) throws Exception;
    Match obtenerMatch(Long id) throws Exception;
    Match actualizarMatch(Match match) throws Exception;
    void eliminarMatch(Long id) throws Exception;
    List<Match> listarMatches(int limit);
    
}
