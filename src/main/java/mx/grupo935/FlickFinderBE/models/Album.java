package mx.grupo935.FlickFinderBE.models;

import java.io.Serial;
import java.io.Serializable;

public class Album implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;

    private long id;
    private String nombre;
    private String artista;
    private String generoMusical;
    private int anioLanzamiento;
    private String coverUrl;
    private int popularidad;

    public Album(long id, String nombre, String artista, String generoMusical, int anioLanzamiento, String coverUrl, int popularidad) {
        this.id = id;
        this.nombre = nombre;
        this.artista = artista;
        this.generoMusical = generoMusical;
        this.anioLanzamiento = anioLanzamiento;
        this.coverUrl = coverUrl;
        this.popularidad = popularidad;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getGeneroMusical() {
        return generoMusical;
    }

    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }

    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(int anioLanzamiento) {
        this.anioLanzamiento = anioLanzamiento;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(int popularidad) {
        this.popularidad = popularidad;
    }
}