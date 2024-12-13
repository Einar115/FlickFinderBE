package mx.grupo935.FlickFinderBE.controllers;

import mx.grupo935.FlickFinderBE.jwt.JwtUtil;
import mx.grupo935.FlickFinderBE.models.Usuario;
import mx.grupo935.FlickFinderBE.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) throws IOException {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Buscar al usuario en los archivos JSON
        Usuario usuario = usuarioService.getUsuarioByNombreUsuario(username);

        if (usuario == null || !passwordEncoder.matches(password, usuario.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }

        // Generar el token JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", usuario.getCorreo());
        String token = jwtUtil.generateToken(usuario.getNombreUsuario(), claims);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
