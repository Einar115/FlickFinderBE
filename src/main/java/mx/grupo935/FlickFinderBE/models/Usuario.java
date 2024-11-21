package mx.grupo935.FlickFinderBE.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID=1L;

    private long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    //contructores
    public Usuario() {}

    public Usuario(long id, String usuario, String email, String contraseña, String nombre, String apellido, String fechaNacimiento) {
        this.id = id;
        this.username = usuario;
        this.email = email;
        this.password = contraseña;
        this.firstName = nombre;
        this.lastName = apellido;
        this.dateOfBirth = fechaNacimiento;
    }


    //Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
