package mx.grupo935.FlickFinderBE.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.grupo935.FlickFinderBE.jwt.JwtUtil;
import mx.grupo935.FlickFinderBE.models.Usuario;
import mx.grupo935.FlickFinderBE.services.RecomendacionService;
import mx.grupo935.FlickFinderBE.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    @Autowired
    private RecomendacionService recomendacionService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/peliculas")
    public ResponseEntity<List<String>> recomendarPeliculas(@RequestHeader("Authorization") String authHeader) throws IOException {
        Usuario usuario = obtenerUsuarioDesdeToken(authHeader);
        List<String> recomendaciones = recomendacionService.recomendarPeliculas(usuario, 1);
        return ResponseEntity.ok(recomendaciones);
    }

    @GetMapping("/albumes")
    public ResponseEntity<String> recomendarAlbumes(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "10") int limite) throws IOException {
        // Obtener el usuario desde el token
        Usuario usuario = obtenerUsuarioDesdeToken(authHeader);

        // Llamar al servicio para obtener recomendaciones
        String recomendaciones = recomendacionService.recomendarAlbumes(usuario, limite);
        // Retornar el resultado como un String
        return ResponseEntity.ok(recomendaciones);
    }



    /*@GetMapping("/albumes")
    public ResponseEntity<List<Map<String, Object>>> recomendarAlbumes(@RequestHeader("Authorization") String authHeader) throws IOException {
        Usuario usuario = obtenerUsuarioDesdeToken(authHeader);
        List<String> recomendacionesJson = recomendacionService.recomendarAlbumes(usuario);

        List<Map<String, Object>> recomendacionesProcesadas = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (String recomendacionJson : recomendacionesJson) {
            JsonNode recomendacionNode = objectMapper.readTree(recomendacionJson);
            JsonNode items = recomendacionNode.path("albums").path("items");

            for (JsonNode item : items) {
                Map<String, Object> album = new HashMap<>();
                album.put("id", item.get("id").asText()); // ID único del álbum
                album.put("name", item.get("name").asText());
                album.put("artist", item.get("artists").get(0).get("name").asText());
                album.put("image", item.get("images").get(0).get("url").asText());
                album.put("release_date", item.get("release_date").asText());
                recomendacionesProcesadas.add(album);
            }
        }

        return ResponseEntity.ok(recomendacionesProcesadas);
    }*/


    private Usuario obtenerUsuarioDesdeToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        try {
            return usuarioService.getUsuarioByNombreUsuario(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

