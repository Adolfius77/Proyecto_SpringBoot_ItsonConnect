/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import java.util.List;
import model.Interaccion;

/**
 *
 * @author jorge
 */
public interface IInteraccionService {
    
    Interaccion crearInteraccion(Interaccion interaccion) throws Exception;
    Interaccion obtenerInteraccion(Long id) throws Exception;
    Interaccion actualizarInteraccion(Interaccion interaccion) throws Exception;
    void eliminarInteraccion(Long id) throws Exception;
    List<Interaccion> listarInteracciones(int limit);
    
}
