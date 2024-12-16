package mx.grupo935.FlickFinderBE.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.grupo935.FlickFinderBE.services.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    @Autowired
    public SpotifyController(SpotifyService spotifyService){
        this.spotifyService=spotifyService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam String query, @RequestParam String type) {
        if (!type.equals("track") && !type.equals("album")) {
            throw new IllegalArgumentException("Invalid type. Only 'track' or 'album' are allowed.");
        }
        String result = spotifyService.search(query, type);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<String> getTrackDetails(@PathVariable long id){
        String result = spotifyService.getTrackDetailsById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/new-albums")
    public ResponseEntity<?> getNewAlbums() {
        try {
            String newAlbumsJson = spotifyService.getNewReleases();
            System.out.println("albumes:"+newAlbumsJson);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode newAlbumsNode = objectMapper.readTree(newAlbumsJson);

            // Procesar los datos para enviar solo la información necesaria
            List<Map<String, Object>> albumes = new ArrayList<>();
            for (JsonNode albumNode : newAlbumsNode.get("albums").get("items")) {
                Map<String, Object> album = new HashMap<>();
                album.put("id", albumNode.get("id").asText()); // Extraer y agregar el ID del álbum
                album.put("name", albumNode.get("name").asText());
                album.put("artist", albumNode.get("artists").get(0).get("name").asText());
                album.put("image", albumNode.get("images").get(0).get("url").asText());
                album.put("release_date", albumNode.get("release_date").asText());
                albumes.add(album);
            }

            return ResponseEntity.ok(albumes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching new albums", "details", e.getMessage()));
        }
    }

    @GetMapping("/new-releases")
    public ResponseEntity<List<Map<String,String>>> getNewReleases() {
        String newReleases = spotifyService.getNewReleases();
        try{
            JsonNode jsonNode=new ObjectMapper().readTree(newReleases);
            List<Map<String,String>> tracks = new ArrayList<>();

            for (JsonNode album: jsonNode.get("albums").get("items")){
                Map<String, String> trackInfo=new HashMap<>();
                trackInfo.put("name", album.get("name").asText());
                trackInfo.put("artist", album.get("artists").get(0).get("name").asText());
                if (album.has("images") && album.get("images").isArray() && album.get("images").size() > 0) {
                    trackInfo.put("image", album.get("images").get(0).path("url").asText("default-image.jpg"));
                } else {
                    trackInfo.put("image", "default-image.jpg");
                }
                tracks.add(trackInfo);
            }
            return ResponseEntity.ok(tracks);
        } catch (Exception e){
            throw new RuntimeException("Error al obtener los temas: " + e.getMessage(), e);
        }
    }
}
