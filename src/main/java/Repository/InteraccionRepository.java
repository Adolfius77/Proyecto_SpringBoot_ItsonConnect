package Repository;

import model.Estudiante;
import model.Interaccion;
import model.Interaccion.TipoInteraccion; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InteraccionRepository extends JpaRepository<Interaccion, Long> {

     
    @Query("SELECT i FROM Interaccion i WHERE i.emisor = :emisor AND i.receptor = :receptor AND i.tipo IN (:likeType, :superlikeType)")
    Optional<Interaccion> buscarLikeOSuperlike(
            @Param("emisor") Estudiante emisor, 
            @Param("receptor") Estudiante receptor,
            @Param("likeType") TipoInteraccion likeType, // Parametro para LIKE
            @Param("superlikeType") TipoInteraccion superlikeType // Parametro para SUPERLIKE
    );

    
    default Optional<Interaccion> buscarLikeOSuperlike(Estudiante emisor, Estudiante receptor) {
        return buscarLikeOSuperlike(emisor, receptor, TipoInteraccion.LIKE, TipoInteraccion.SUPERLIKE);
    }
}