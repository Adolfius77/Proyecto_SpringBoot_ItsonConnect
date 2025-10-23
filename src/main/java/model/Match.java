/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author luisb
 */
@Entity
@Table(name = "Matches")
public class Match implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchEstudiante> participantes;
    
    @Column(nullable = false)    
    private Date fecha;
    
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private Set<Mensaje> mensajes;

    public Match() {
    }

    public Match(Long id, Set<MatchEstudiante> participantes, Date fecha, Set<Mensaje> mensajes) {
        this.id = id;
        this.participantes = participantes;
        this.fecha = fecha;
        this.mensajes = mensajes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<MatchEstudiante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Set<MatchEstudiante> participantes) {
        this.participantes = participantes;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Set<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(Set<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
    
    
    
}
