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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author luisb
 */
@Entity
@Table(name = "estudiantes")
public class Estudiante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "fecha_registro", nullable = false)
    private Date fechaRegistro;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apPaterno;
    
    @Column(nullable = false)
    private String apMaterno;
    
    @Column(nullable = false)
    private String correo;
    
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL)
    private Set<Interaccion> interacciones;
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private Set<HobbyEstudiante> hobbies;
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchEstudiante> matchEstudiantes;
    
    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL)
    private Set<Mensaje> mensajes;

    public Estudiante() {
    }

    public Estudiante(Long id, Date fechaRegistro, String nombre, String apPaterno, String apMaterno, String correo, String password, Set<Interaccion> interacciones, Set<HobbyEstudiante> hobbies, Set<MatchEstudiante> matchEstudiantes, Set<Mensaje> mensajes) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.correo = correo;
        this.password = password;
        this.interacciones = interacciones;
        this.hobbies = hobbies;
        this.matchEstudiantes = matchEstudiantes;
        this.mensajes = mensajes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Interaccion> getInteracciones() {
        return interacciones;
    }

    public void setInteracciones(Set<Interaccion> interacciones) {
        this.interacciones = interacciones;
    }

    public Set<HobbyEstudiante> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<HobbyEstudiante> hobbies) {
        this.hobbies = hobbies;
    }

    public Set<MatchEstudiante> getMatchEstudiantes() {
        return matchEstudiantes;
    }

    public void setMatchEstudiantes(Set<MatchEstudiante> matchEstudiantes) {
        this.matchEstudiantes = matchEstudiantes;
    }

    public Set<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(Set<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Estudiante(Long id) {
        this.id = id;
    }
    
    
    
    
    
}
