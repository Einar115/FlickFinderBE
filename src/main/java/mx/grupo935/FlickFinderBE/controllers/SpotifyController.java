package mx.grupo935.FlickFinderBE.controllers;

import mx.grupo935.FlickFinderBE.services.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    @Autowired
    public SpotifyController(SpotifyService spotifyService){
        this.spotifyService=spotifyService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchTracks(@RequestParam String query){
        String result = spotifyService.searchTrack(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<String> getTrackDetails(@PathVariable String id){
        String result = spotifyService.getTrackDetails(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/new-releases")
    public ResponseEntity<String> getNewReleases() {
        String newReleases = spotifyService.getNewReleases();
        return ResponseEntity.ok(newReleases);
    }
}
