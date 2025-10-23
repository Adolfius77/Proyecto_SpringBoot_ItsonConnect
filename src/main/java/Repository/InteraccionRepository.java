package com.mycompany.itsonconnect.repository;

import model.Estudiante;
import model.Interaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InteraccionRepository extends JpaRepository<Interaccion, Long> {

    @Query("SELECT i FROM Interaccion i WHERE i.emisor = :emisor AND i.receptor = :receptor AND i.tipo IN (model.Interaccion.TipoInteraccion.LIKE, model.Interaccion.TipoInteraccion.SUPERLIKE)")
    Optional<Interaccion> buscarLikeOSuperlike(Estudiante emisor, Estudiante receptor);
}