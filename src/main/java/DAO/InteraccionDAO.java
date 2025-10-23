package DAO;


import interfacez.IInteraccionDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Estudiante;
import model.Interaccion;

import java.util.List;

public class InteraccionDAO implements IInteraccionDAO {
    @Override
    public void crear(Interaccion interaccion, EntityManager em) {
         em.persist(interaccion);
    }

    @Override
    public Interaccion buscarPorId(Long id, EntityManager em) {
        return em.find(Interaccion.class, id);
    }

    @Override
    public Interaccion actualizar(Interaccion interaccion, EntityManager em) {
        return em.merge(interaccion);
    }

    @Override
    public void eliminar(Long id, EntityManager em) {
        Interaccion interaccion = buscarPorId(id, em);
        if (interaccion != null) {
            em.remove(interaccion);
        }
    }

    @Override
    public List<Interaccion> listar(int limit, EntityManager em) {
        int resultadosMaximos = (limit > 0 && limit <= 100) ? limit : 100;
        TypedQuery<Interaccion> query = em.createQuery("SELECT i FROM Interaccion i ORDER BY i.fecha", Interaccion.class);
        query.setMaxResults(resultadosMaximos);
        return query.getResultList();
    }

    @Override
    public Interaccion buscarLikeOSuperlike(Estudiante emisor, Estudiante receptor, EntityManager em) {
        try{
            TypedQuery<Interaccion> query = em.createQuery(
                    "SELECT i FROM Interaccion i WHERE i.emisor = :emisor " +
                            "AND i.receptor = :receptor " +
                            "AND (i.tipo = :like OR i.tipo = :superlike)",
                            Interaccion.class);
            query.setParameter("emisor", emisor);
            query.setParameter("receptor", receptor);
            query.setParameter("like", Interaccion.TipoInteraccion.LIKE);
            query.setParameter("superlike", Interaccion.TipoInteraccion.SUPERLIKE);

            query.setMaxResults(1);
            return query.getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
}
