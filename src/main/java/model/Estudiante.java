package model;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    @Column(name = "carrera", nullable = false)
    private String carrera;
    
    @Column(name = "genero", nullable = false)
    private String genero;
    
    @Column(name = "fecha_registro", nullable = false)
    private Date fechaRegistro;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apPaterno;

    @Column(nullable = false)
    private String apMaterno;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;

    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL)
    private Set<Interaccion> interaccionesEnviadas;

    @OneToMany(mappedBy = "receptor", cascade = CascadeType.ALL)
    private Set<Interaccion> interaccionesRecibidas;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private Set<HobbyEstudiante> hobbies;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchEstudiante> matchEstudiantes;

    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL)
    private Set<Mensaje> mensajesEnviados;

    public Estudiante() {
    }
    public Estudiante(Long id) {
        this.id = id;
    }
    public Estudiante(Long id, String carrera, String genero, Date fechaRegistro, String nombre, String apPaterno, String apMaterno, String correo, String password, byte[] foto, Set<Interaccion> interaccionesEnviadas, Set<Interaccion> interaccionesRecibidas, Set<HobbyEstudiante> hobbies, Set<MatchEstudiante> matchEstudiantes, Set<Mensaje> mensajesEnviados) {
        this.id = id;
        this.carrera = carrera;
        this.genero = genero;
        this.fechaRegistro = fechaRegistro;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.correo = correo;
        this.password = password;
        this.foto = foto;
        this.interaccionesEnviadas = interaccionesEnviadas;
        this.interaccionesRecibidas = interaccionesRecibidas;
        this.hobbies = hobbies;
        this.matchEstudiantes = matchEstudiantes;
        this.mensajesEnviados = mensajesEnviados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Set<Interaccion> getInteraccionesEnviadas() {
        return interaccionesEnviadas;
    }

    public void setInteraccionesEnviadas(Set<Interaccion> interaccionesEnviadas) {
        this.interaccionesEnviadas = interaccionesEnviadas;
    }

    public Set<Interaccion> getInteraccionesRecibidas() {
        return interaccionesRecibidas;
    }

    public void setInteraccionesRecibidas(Set<Interaccion> interaccionesRecibidas) {
        this.interaccionesRecibidas = interaccionesRecibidas;
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

    public Set<Mensaje> getMensajesEnviados() {
        return mensajesEnviados;
    }

    public void setMensajesEnviados(Set<Mensaje> mensajesEnviados) {
        this.mensajesEnviados = mensajesEnviados;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "id=" + id + ", carrera=" + carrera + ", genero=" + genero + ", fechaRegistro=" + fechaRegistro + ", nombre=" + nombre + ", apPaterno=" + apPaterno + ", apMaterno=" + apMaterno + ", correo=" + correo + ", password=" + password + ", foto=" + foto + ", interaccionesEnviadas=" + interaccionesEnviadas + ", interaccionesRecibidas=" + interaccionesRecibidas + ", hobbies=" + hobbies + ", matchEstudiantes=" + matchEstudiantes + ", mensajesEnviados=" + mensajesEnviados + '}';
    }
    
    

    
}
