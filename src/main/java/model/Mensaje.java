/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import java.io.Serializable;
import java.security.Timestamp;

/**
 *
 * @author luisb
 */
@Entity
public class Mensaje implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private Timestamp fechaHora;

    @ManyToOne
    @JoinColumn(name = "id_emisor")
    private Estudiante emisor;

    @ManyToOne
    @JoinColumn(name = "id_match")
    private Match match;

    public Mensaje() {
    }

    public Mensaje(Timestamp fechaHora, Estudiante emisor, Match match) {
        this.fechaHora = fechaHora;
        this.emisor = emisor;
        this.match = match;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Estudiante getEmisor() {
        return emisor;
    }

    public void setEmisor(Estudiante emisor) {
        this.emisor = emisor;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
    
    
   
   
   
}
