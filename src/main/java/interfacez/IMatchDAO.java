package interfacez;

import java.util.List;
import model.Estudiante;
import model.Match;
import jakarta.persistence.EntityManager;

public interface IMatchDAO {
    void crear(Match match, EntityManager em);
    Match buscarPorId(Long id, EntityManager em);
    Match actualizar(Match match, EntityManager em);
    void eliminar(Long id, EntityManager em);
    List<Match> listar(int limit, EntityManager em);


    Match buscarMatchExistente(Estudiante est1, Estudiante est2, EntityManager em);
}