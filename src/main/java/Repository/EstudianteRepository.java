package Repository;

import java.util.List;
import model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByCorreo(String correo);

    @Query("SELECT e FROM Estudiante e WHERE e.id != :idActual AND e.id NOT IN (SELECT i.receptor.id FROM Interaccion i WHERE i.emisor.id = :idActual)")
    List<Estudiante> findEstudiantesParaDescubrir(@Param("idActual") Long idActual, Pageable pageable);
}
