package mx.grupo935.FlickFinderBE.controllers;

import mx.grupo935.FlickFinderBE.models.Usuario;
import mx.grupo935.FlickFinderBE.others.Constantes;
import mx.grupo935.FlickFinderBE.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        usuarioService.registerUser(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @GetMapping("/all")
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerTodos();
    }

    /*@SuppressWarnings("unchecked")
    private List<Usuario> leerUsuarios(){
        File file = new File(Constantes.NFS_DIRECTORY + "/usuarios.dat");
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }*/
}
