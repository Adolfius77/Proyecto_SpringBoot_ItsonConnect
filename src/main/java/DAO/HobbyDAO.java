package DAO;


import interfacez.IHobbyDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Hobby;

import java.util.List;

public class HobbyDAO implements IHobbyDAO {
    @Override
    public Hobby actualizar(Hobby hobby, EntityManager em) {
        return em.merge(hobby);
    }

    @Override
    public void crear(Hobby hobby, EntityManager em) {
        em.persist(hobby);
    }

    @Override
    public Hobby buscarPorId(Long id, EntityManager em) {
        return em.find(Hobby.class, id);
    }

    @Override
    public void eliminar(Long id, EntityManager em) {
        Hobby hobby = buscarPorId(id, em);
        if (hobby != null) {
            em.remove(hobby);
        }
    }

    @Override
    public List<Hobby> listar(int limit, EntityManager em) {
        int resultadosMaximos = (limit > 0 && limit <= 100) ? limit : 100;
        TypedQuery<Hobby> query = em.createQuery("SELECT h FROM Hobby h ORDER BY h.nombre", Hobby.class);
        query.setMaxResults(resultadosMaximos);
        return query.getResultList();
    }

    @Override
    public Hobby buscarPorNombre(String nombre, EntityManager em) {
        try {
          TypedQuery<Hobby>query = em.createQuery("SELECT h FROM Hobby h WHERE h.nombre = :nombre ", Hobby.class);
          query.setParameter("nombre", nombre);
          return query.getSingleResult();
        }catch (Exception e) {
            return null;
        }
    }
}
