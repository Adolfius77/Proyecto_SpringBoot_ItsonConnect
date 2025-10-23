package interfacez;

import java.util.List;
import model.Estudiante;
import model.Interaccion;
import jakarta.persistence.EntityManager;

public interface IInteraccionDAO {
    void crear(Interaccion interaccion, EntityManager em);
    Interaccion buscarPorId(Long id, EntityManager em);
    Interaccion actualizar(Interaccion interaccion, EntityManager em);
    void eliminar(Long id, EntityManager em);
    List<Interaccion> listar(int limit, EntityManager em);

    Interaccion buscarLikeOSuperlike(Estudiante emisor, Estudiante receptor, EntityManager em);
}