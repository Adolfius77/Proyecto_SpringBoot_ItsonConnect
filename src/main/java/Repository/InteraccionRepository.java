package Repository;

import model.Estudiante;
import model.Interaccion;
import model.Interaccion.TipoInteraccion; // Importa el Enum
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Importa Param
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InteraccionRepository extends JpaRepository<Interaccion, Long> {

     
    @Query("SELECT i FROM Interaccion i WHERE i.emisor = :emisor AND i.receptor = :receptor AND i.tipo IN (:likeType, :superlikeType)")
    Optional<Interaccion> buscarLikeOSuperlike(
            @Param("emisor") Estudiante emisor, 
            @Param("receptor") Estudiante receptor,
            @Param("likeType") TipoInteraccion likeType, // Parámetro para LIKE
            @Param("superlikeType") TipoInteraccion superlikeType // Parámetro para SUPERLIKE
    );

    
    default Optional<Interaccion> buscarLikeOSuperlike(Estudiante emisor, Estudiante receptor) {
        return buscarLikeOSuperlike(emisor, receptor, TipoInteraccion.LIKE, TipoInteraccion.SUPERLIKE);
    }
}