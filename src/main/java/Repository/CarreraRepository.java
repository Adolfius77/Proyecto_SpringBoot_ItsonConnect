package Repository;

import model.Carrera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long>, PagingAndSortingRepository<Carrera, Long> {

    Page<Carrera> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Carrera> findAll(Pageable pageable);
}
