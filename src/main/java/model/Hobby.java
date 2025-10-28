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
import java.util.Set;

/**
 *
 * @author luisb
 */
@Entity
@Table(name = "hobbies")
public class Hobby implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "hobby", cascade = CascadeType.ALL)
    private Set<HobbyEstudiante> hobby;

    public Hobby() {
    }

    public Hobby(Long id, String nombre, String descripcion, Set<HobbyEstudiante> hobby) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.hobby = hobby;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<HobbyEstudiante> getHobby() {
        return hobby;
    }

    public void setHobby(Set<HobbyEstudiante> hobby) {
        this.hobby = hobby;
    }
}
