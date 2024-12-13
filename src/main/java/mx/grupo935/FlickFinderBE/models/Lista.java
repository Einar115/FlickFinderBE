package mx.grupo935.FlickFinderBE.models;

import java.io.Serializable;
import java.util.List;

public class Lista implements Serializable {
    private long id;
    private long idUsuario;
    private String nombre;
    private List<Long> elementos;

    public Lista(long id, long idUsuario, String nombre, List<Long> elementos) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.elementos = elementos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Long> getElementos() {
        return elementos;
    }

    public void setElementos(List<Long> elementos) {
        this.elementos = elementos;
    }
}
