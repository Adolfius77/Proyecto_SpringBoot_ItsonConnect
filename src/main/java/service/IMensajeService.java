package service;

import dto.ChatMensajeDTO;
import model.Mensaje;

import java.util.List;

public interface IMensajeService {
    Mensaje guardarMensaje(ChatMensajeDTO dto)throws Exception;
    Mensaje obtenerMensaje(Long id);
    void eliminarMensaje(Long id) throws Exception;
    List<Mensaje> listarMensajesPorMatch(Long idMatch, int limit);
    
}
