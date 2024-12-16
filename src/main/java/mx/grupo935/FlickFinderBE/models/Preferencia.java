package mx.grupo935.FlickFinderBE.models;

import java.io.Serial;
import java.io.Serializable;

public class Preferencia implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String tipo;
    private String referenciaId;
    private String fechaAgregada;

    public Preferencia() {}

    public Preferencia(Long id, String tipo, String referenciaId, String fechaAgregada) {
        this.id = id;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.fechaAgregada = fechaAgregada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(String referenciaId) {
        this.referenciaId = referenciaId;
    }

    public String getFechaAgregada() {
        return fechaAgregada;
    }

    public void setFechaAgregada(String fechaAgregada) {
        this.fechaAgregada = fechaAgregada;
    }

    @Override
    public String toString() {
        return "Preferencia{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", referenciaId=" + referenciaId +
                ", fechaAgregada=" + fechaAgregada +
                '}';
    }
}
