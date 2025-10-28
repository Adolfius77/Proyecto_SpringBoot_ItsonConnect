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
    private List<Long> estudiantesId;

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

    public List<Long> getEstdudiantesId() {
        return estudiantesId;
    }

    public void setParticipanteIds(List<Long> estudiantesID) {
        this.estudiantesId = estudiantesId;
    }

    @Override
    public String toString() {
        return "MatchDTO{" + "id=" + id + ", fecha=" + fecha + ", estudiantesId=" + estudiantesId + '}';
    }
    
}
