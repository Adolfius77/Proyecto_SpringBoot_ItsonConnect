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

    public Estudiante(Long id, Date fechaRegistro, String nombre, String apPaterno, String apMaterno, String correo, String password, byte[] foto /*, otros sets si los necesitas inicializar */) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.correo = correo;
        this.password = password;
        this.foto = foto;
        this.interaccionesEnviadas = new HashSet<>();
        this.interaccionesRecibidas = new HashSet<>();
        this.hobbies = new HashSet<>();
        this.matchEstudiantes = new HashSet<>();
        this.mensajesEnviados = new HashSet<>();
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
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Estudiante that = (Estudiante) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "Estudiante{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", correo='" + correo + '\''
                + 
                '}';
    }
}
