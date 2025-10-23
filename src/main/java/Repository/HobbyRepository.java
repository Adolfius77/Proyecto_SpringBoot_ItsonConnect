package com.mycompany.itsonconnect.repository;

import model.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {
   
    Optional<Hobby> findByNombre(String nombre);
}