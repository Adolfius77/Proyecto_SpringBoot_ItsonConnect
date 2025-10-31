/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.Set;

/**
 *
 * @author jorge
 */
public class EstudianteDTO {
    private Long id;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String correo;
    private String password;
    private String fechaRegistro;
    private String fotoBase64;
    private Set<String> hobbies;
    

    public EstudianteDTO() {
    }

    public EstudianteDTO(Long id, String nombre, String apPaterno, String apMaterno, String correo, String password, String fechaRegistro, String fotoBase64, Set<String> hobbies) {
        this.id = id;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.correo = correo;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
        this.fotoBase64 = fotoBase64;
        this.hobbies = hobbies;
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

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public Set<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "EstudianteDTO{" + "id=" + id + ", nombre=" + nombre + ", apPaterno=" + apPaterno + ", apMaterno=" + apMaterno + ", correo=" + correo + ", password=" + password + ", fechaRegistro=" + fechaRegistro + ", fotoBase64=" + fotoBase64 + ", hobbies=" + hobbies + '}';
    }

    
    
}
