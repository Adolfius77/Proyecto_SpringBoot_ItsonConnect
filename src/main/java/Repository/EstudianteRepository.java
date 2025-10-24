package Repository;

import model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
 
    Optional<Estudiante> findByCorreo(String correo);
}