package interfacez;

import jakarta.persistence.EntityManager;
import model.Estudiante;

import java.util.List;

public interface IEstudianteDAO {
    void crear(Estudiante estudiante , EntityManager em);
    Estudiante buscarPorId(Long id, EntityManager em);
    Estudiante actualizar(Estudiante estudiante , EntityManager em);
    void eliminar(Long id, EntityManager em);
    List<Estudiante> listar(int limit, EntityManager em);

    Estudiante buscarPorCorreo(String correo, EntityManager em);
}
