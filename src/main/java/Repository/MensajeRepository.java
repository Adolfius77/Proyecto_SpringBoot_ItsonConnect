package com.mycompany.itsonconnect.repository;

import model.Match;
import model.Mensaje;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByMatchOrderByFechaHoraAsc(Match match, Pageable pageable);
}
