package mx.grupo935.FlickFinderBE.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Preferencia implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long usuarioId;
    private String tipo;
    private Long referenciaId;
    private LocalDate fechaAgregada;

    public Preferencia(Long id, Long usuarioId, String tipo, Long referenciaId, LocalDate fechaAgregada) {
        this.id = id;
        this.usuarioId = usuarioId;
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }

    public LocalDate getFechaAgregada() {
        return fechaAgregada;
    }

    public void setFechaAgregada(LocalDate fechaAgregada) {
        this.fechaAgregada = fechaAgregada;
    }
}
