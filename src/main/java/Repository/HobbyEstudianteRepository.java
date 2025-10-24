package Repository;

import model.Estudiante;
import model.Hobby;
import model.HobbyEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HobbyEstudianteRepository extends JpaRepository<HobbyEstudiante, Long> {
    

    List<HobbyEstudiante> findByEstudiante(Estudiante estudiante);
    List<HobbyEstudiante> findByHobby(Hobby hobby);
}