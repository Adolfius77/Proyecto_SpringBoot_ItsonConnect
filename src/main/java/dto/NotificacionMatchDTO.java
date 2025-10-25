package dto;

public class NotificacionMatchDTO {
    private Long matchId;
    private Long otroUsuario;
    private String otroUsuarioNombre;
    private String mensaje;

    public NotificacionMatchDTO() {
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getOtroUsuario() {
        return otroUsuario;
    }

    public void setOtroUsuario(Long otroUsuario) {
        this.otroUsuario = otroUsuario;
    }

    public String getOtroUsuarioNombre() {
        return otroUsuarioNombre;
    }

    public void setOtroUsuarioNombre(String otroUsuarioNombre) {
        this.otroUsuarioNombre = otroUsuarioNombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
