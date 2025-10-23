package interfacez;

import jakarta.persistence.EntityManager;
import model.Hobby;

import java.util.List;

public interface IHobbyDAO {
    void crear(Hobby hobby, EntityManager em);
    Hobby buscarPorId(Long id, EntityManager em);
    Hobby actualizar(Hobby hobby, EntityManager em);
    void eliminar(Long id, EntityManager em);
    List<Hobby> listar(int limit, EntityManager em);

    Hobby buscarPorNombre(String nombre, EntityManager em);


}
