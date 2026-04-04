package service.impl.factorys;

import dto.EstudianteDTO;
import model.Estudiante;
import model.Match;
import model.MatchEstudiante;

import java.util.Date;
import java.util.HashSet;

public class MatchFactory {
    public Match construirMatchVacio(){
         Match match = new Match();
         match.setFecha(new Date());

         match.setParticipantes(new HashSet<>());
         match.setMensajes(new HashSet<>());

         return match;
    }
    public Match construirMatchConEstudiante(Estudiante estudiante1, Estudiante estudiante2){
        Match match = construirMatchVacio();

        MatchEstudiante relacion1 =  new MatchEstudiante();
        relacion1.setMatch(match);
        relacion1.setEstudiante(estudiante1);

        MatchEstudiante relacion2 =  new MatchEstudiante();
        relacion2.setMatch(match);
        relacion2.setEstudiante(estudiante2);

        match.getParticipantes().add(relacion1);
        match.getParticipantes().add(relacion2);

        return match;
    }
}
