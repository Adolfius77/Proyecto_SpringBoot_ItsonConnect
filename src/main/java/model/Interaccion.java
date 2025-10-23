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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import java.io.Serializable;
import java.security.Timestamp;

/**
 *
 * @author luisb
 */
@Entity
@Table(name = "interacciones")
public class Interaccion implements Serializable {

    public enum TipoInteraccion {
        LIKE,
        SUPERLIKE,
        PASS,
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_emisor")
    private Estudiante emisor;
    
    @ManyToOne
    @JoinColumn(name = "id_receptor")
    private Estudiante receptor;
    
    @Column(nullable = false)
    private TipoInteraccion tipo; 
    
    @Column(name = "fecha_hora",nullable = false)
    private Timestamp fechaHora;

    public Interaccion() {
    }

    public Interaccion(Long id, Estudiante emisor, Estudiante receptor, TipoInteraccion tipo, Timestamp fechaHora) {
        this.id = id;
        this.emisor = emisor;
        this.receptor = receptor;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante getEmisor() {
        return emisor;
    }

    public void setEmisor(Estudiante emisor) {
        this.emisor = emisor;
    }

    public Estudiante getReceptor() {
        return receptor;
    }

    public void setReceptor(Estudiante receptor) {
        this.receptor = receptor;
    }

    public TipoInteraccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoInteraccion tipo) {
        this.tipo = tipo;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    
    
    }
