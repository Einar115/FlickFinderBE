package mx.grupo935.FlickFinderBE.controllers;

import mx.grupo935.FlickFinderBE.models.Usuario;
import mx.grupo935.FlickFinderBE.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //Registrar nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Usuario usuario) {
        try{
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuarioService.saveUser(usuario);
            Map<String, String> response = new HashMap<>();

            response.put("message", "Usuario registrado exitosamente");
            return ResponseEntity.ok(response);
        }catch(IOException e){
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al registrar usuario");
            return ResponseEntity.status(500).body(response);
        }catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Ya existe una cuenta con el mismo nombre de usuario o correo electrónico");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    //obtener informacion de algun usuario
    @GetMapping("/{nombreUsuario}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable String nombreUsuario){
        try {
            Usuario usuario=usuarioService.getUsuarioByNombreUsuario(nombreUsuario);
            return  ResponseEntity.ok(usuario);
        }catch (IOException e){
            return ResponseEntity.notFound().build();
        }
    }

    //obtener todos los usuarios registrados
    @GetMapping("/all")
    public List<Usuario> obtenerTodosLosUsuarios() {
        try {
            return usuarioService.getAllUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
