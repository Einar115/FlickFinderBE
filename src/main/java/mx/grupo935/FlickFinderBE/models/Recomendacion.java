package mx.grupo935.FlickFinderBE.models;

import java.io.Serial;
import java.io.Serializable;

public class Recomendacion implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;

    private long id;
    private long usuarioId;
    private String tipo;
    private long referenciaId;
    private String razon;

    public Recomendacion(long id, long usuarioId, String tipo, long referenciaId, String razon) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.razon = razon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    @Override
    public String toString() {
        return "Recomendacion{" +
                "id=" + id +
                ", titulo='" + usuarioId + '\'' +
                ", tipo='" + tipo + '\'' +
                ", referenciaId=" + referenciaId +
                ", razon='" + razon + '\'' +
                '}';
    }
}
