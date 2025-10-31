package service;

import java.util.List;
import java.util.Set; // <-- Importar Set
import model.Estudiante;

/**
 *
 * @author USER
 */
public interface IEstudianteService {
   

   Estudiante crearEstudiante(Estudiante estudiante, Set<String> hobbyNames) throws Exception;
   Estudiante obtenerEstudiante(Long id);
   Estudiante actualizarEstudiante(Estudiante estudiante) throws Exception;
   void eliminarEstudiante(Long id) throws Exception;
   List<Estudiante> listarEstudiantes(int limit);
   Estudiante login(String correo, String password) throws Exception;
}