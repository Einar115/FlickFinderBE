package mx.grupo935.FlickFinderBE.models;

import java.io.Serial;
import java.io.Serializable;

public class Usuario implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;

    private Long id;
    private String nombreUsuario;
    private String correo;
    private String password;
    private String fechaNacimiento;


    //contructores
    public Usuario() {}

    public Usuario(Long id, String nombreUsuario, String correo, String password, String fechaNacimiento) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getId() {
        return id;
    }

    //setters y getters
    public void setId(int id) {
        this.id = (long) id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    //toString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", correo='" + correo + '\'' +
                ", password='" + password + '\'' +
                ", fechaNacimiento='" + fechaNacimiento +
                '}';
    }
}
