package mx.grupo935.FlickFinderBE.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.grupo935.FlickFinderBE.jwt.JwtUtil;
import mx.grupo935.FlickFinderBE.models.Preferencia;
import mx.grupo935.FlickFinderBE.models.Usuario;
import mx.grupo935.FlickFinderBE.services.PeliculaService;
import mx.grupo935.FlickFinderBE.services.PreferenciaService;
import mx.grupo935.FlickFinderBE.services.SpotifyService;
import mx.grupo935.FlickFinderBE.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/preferencias")
public class PreferenciaController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private PreferenciaService preferenciaService;

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private JwtUtil jwtUtil;

    //obtiene la informacion de los archivos de preferencia
    @GetMapping("/favoritos")
    public List<Preferencia> obtenerFavoritos(@RequestHeader("Authorization") String authHeader) throws IOException {
        // Extraer el token y el nombre de usuario
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        // Obtener el usuario asociado al token
        Usuario usuario = usuarioService.getUsuarioByNombreUsuario(username);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Obtener todas las preferencias del usuario y retornarlas
        return preferenciaService.getAllPreferencias(usuario);
    }

    //obtener informacion de la pelicula con la API TMDB tomando en cuenta los archivos de preferencia del usuario
    @GetMapping("/favoritos/peliculas")
    public ResponseEntity<List<Map<String, Object>>> obtenerDetallesPeliculas(@RequestHeader("Authorization") String authHeader) throws IOException {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        Usuario usuario = usuarioService.getUsuarioByNombreUsuario(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Usuario no encontrado
        }
        // Obtener las preferencias del usuario
        List<Preferencia> preferencias = preferenciaService.getAllPreferencias(usuario);

        //Ubtener los detalles para las preferencias de tipo pelicula
        List<Map<String, Object>> detalles = new ArrayList<>();
        for (Preferencia preferencia : preferencias) {
            if ("pelicula".equals(preferencia.getTipo())) {
                String detallesPeliculaJson = peliculaService.getMoviesDetails(Long.parseLong(preferencia.getReferenciaId()));
                Map<String, Object> detalle = new ObjectMapper().readValue(detallesPeliculaJson, Map.class);
                detalles.add(detalle);
            }
        }
        return ResponseEntity.ok(detalles);
    }

    //obtiene informacion de albumes musicales con la API de Spotify con las preferencias del usuario
    @GetMapping("/favoritos/albumes")
    public ResponseEntity<List<Map<String, Object>>> obtenerDetallesAlbumes(@RequestHeader("Authorization") String authHeader) throws IOException {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        Usuario usuario = usuarioService.getUsuarioByNombreUsuario(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Usuario no encontrado
        }

        List<Preferencia> preferencias = preferenciaService.getAllPreferencias(usuario);

        //Obtener los detalles de para las preferencias de tipo album
        List<Map<String, Object>> detalles = new ArrayList<>();
        for (Preferencia preferencia : preferencias) {
            if ("album".equals(preferencia.getTipo())) {
                String detallesAlbumJson = spotifyService.getAlbumDetails(preferencia.getReferenciaId());
                // Convertir el JSON recibido a un Map para consistencia
                Map<String, Object> detalle = new ObjectMapper().readValue(detallesAlbumJson, Map.class);
                detalles.add(detalle);
            }
        }

        return ResponseEntity.ok(detalles); // Devolver los detalles en formato JSON
    }

    //eliminar la preferencia de un usuario
    @DeleteMapping("/favoritos/delete/{referenciaId}")
    public ResponseEntity<String> eliminarPreferenciaPorReferenciaId(@RequestHeader("Authorization") String authHeader, @PathVariable String referenciaId) throws IOException {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        Usuario usuario = usuarioService.getUsuarioByNombreUsuario(username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        boolean eliminado = preferenciaService.deletePreferencia(String.valueOf(referenciaId), usuario);

        if (eliminado) {
            return ResponseEntity.ok("Preferencia eliminada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preferencia no encontrada");
        }
    }

    //guarda la preferencia del usario
    @PostMapping("/guardar")
    public Preferencia guardarPreferencia(@RequestHeader("Authorization") String authHeader, @RequestBody Preferencia preferencia) throws IOException {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        Usuario usuario = usuarioService.getUsuarioByNombreUsuario(username);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Guardar la preferencia
        return preferenciaService.savePreferencia(preferencia, usuario);
    }

}