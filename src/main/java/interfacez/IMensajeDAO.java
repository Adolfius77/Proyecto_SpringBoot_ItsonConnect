package interfacez;

import java.util.List;
import model.Match;
import model.Mensaje;
import jakarta.persistence.EntityManager;

public interface IMensajeDAO {
    void crear(Mensaje mensaje, EntityManager em);
    Mensaje buscarPorId(Long id, EntityManager em);
    Mensaje actualizar(Mensaje mensaje, EntityManager em);
    void eliminar(Long id, EntityManager em);
    List<Mensaje> listar(int limit, EntityManager em);

    List<Mensaje> listarPorMatch(Match match, int limit, EntityManager em);
}