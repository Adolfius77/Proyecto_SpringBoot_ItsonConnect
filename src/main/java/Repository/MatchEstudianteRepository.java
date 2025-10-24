package Repository;

import model.Estudiante;
import model.Match;
import model.MatchEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchEstudianteRepository extends JpaRepository<MatchEstudiante, Long> {

    List<MatchEstudiante> findByEstudiante(Estudiante estudiante);
    List<MatchEstudiante> findByMatch(Match match);
}