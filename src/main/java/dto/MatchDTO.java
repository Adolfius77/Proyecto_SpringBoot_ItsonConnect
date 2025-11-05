/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;

/**
 *
 * @author jorge
 */
public class MatchDTO {
    private Long id;
    private String fecha; 
    private List<EstudianteDTO> participantes;

    public MatchDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<EstudianteDTO> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<EstudianteDTO> participantes) {
        this.participantes = participantes;
    }

    @Override
    public String toString() {
        return "MatchDTO{" + "id=" + id + ", fecha=" + fecha + ", participantes=" + participantes + '}';
    }

    
    
}
