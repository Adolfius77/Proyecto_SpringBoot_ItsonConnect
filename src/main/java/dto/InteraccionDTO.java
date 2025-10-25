/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.Date;

/**
 *
 * @author jorge
 */
public class InteraccionDTO {
    private Long id;
    private Long emisorId;
    private Long receptorId;
    private String tipo;
    private Date fechaHora;

    public InteraccionDTO() {
    }

    public InteraccionDTO(Long id, Long emisorId, Long receptorId, String tipo, Date fechaHora) {
        this.id = id;
        this.emisorId = emisorId;
        this.receptorId = receptorId;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(Long emisorId) {
        this.emisorId = emisorId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(Long receptorId) {
        this.receptorId = receptorId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        return "InteraccionDTO{" + "id=" + id + ", emisorId=" + emisorId + ", receptorId=" + receptorId + ", tipo=" + tipo + ", fechaHora=" + fechaHora + '}';
    }
    
    
}
