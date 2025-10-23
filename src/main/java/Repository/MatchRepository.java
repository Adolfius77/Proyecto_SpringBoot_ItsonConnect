package com.mycompany.itsonconnect.repository;

import model.Estudiante;
import model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    
    
    @Query("SELECT m FROM Match m JOIN m.participantes p1 JOIN m.participantes p2 " +
           "WHERE p1.estudiante = :est1 AND p2.estudiante = :est2")
    Optional<Match> buscarMatchExistente(Estudiante est1, Estudiante est2);
}