package mx.grupo935.FlickFinderBE.models;

import java.io.Serial;
import java.io.Serializable;

public class Pelicula implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;

    private long id;
    private String titulo;
    private String descripcion;
    private String genero;
    private String anioEstreno;
    private String img;
    private int calificacion;

    public Pelicula(long id, String titulo, String descripcion, String genero, String anioEstreno, String img, int calificacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.genero = genero;
        this.anioEstreno = anioEstreno;
        this.img = img;
        this.calificacion = calificacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAnioEstreno() {
        return anioEstreno;
    }

    public void setAnioEstreno(String anioEstreno) {
        this.anioEstreno = anioEstreno;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
