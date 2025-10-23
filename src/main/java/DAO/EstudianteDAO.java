package DAO;

import Config.JpaUtil;
import interfacez.IEstudianteDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Estudiante;
import model.Hobby;

import java.lang.reflect.Type;
import java.util.List;

public class EstudianteDAO implements IEstudianteDAO {
    @Override
    public void crear(Estudiante estudiante, EntityManager em) {
        em.persist(estudiante);
    }

    @Override
    public Estudiante buscarPorId(Long id, EntityManager em) {
        return em.find(Estudiante.class, id);
    }

    @Override
    public Estudiante actualizar(Estudiante estudiante, EntityManager em) {
        return em.merge(estudiante);
    }

    @Override
    public void eliminar(Long id, EntityManager em) {
    Estudiante estudiante = buscarPorId(id, em);
    if (estudiante != null) {
        em.remove(estudiante);
    }
    }

    @Override
    public List<Estudiante> listar(int limit, EntityManager em) {
       int resultadosMaximos = (limit > 0 && limit <= 100) ? limit : 100;
        TypedQuery<Estudiante> query = em.createQuery("SELECT e FROM Estudiante e ORDER BY e.nombre",Estudiante.class);
        query.setMaxResults(resultadosMaximos);
        return query.getResultList();
    }

    @Override
    public Estudiante buscarPorCorreo(String correo, EntityManager em) {
       try {
           TypedQuery<Estudiante> query = em.createQuery("SELECT e FROM Estudiante e WHERE e.correo = :correo",Estudiante.class);
           query.setParameter("correo", correo);
           return query.getSingleResult();
       }catch (Exception e) {
            return null;
       }
    }
}
