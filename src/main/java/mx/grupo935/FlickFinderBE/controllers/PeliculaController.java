package mx.grupo935.FlickFinderBE.controllers;

import mx.grupo935.FlickFinderBE.services.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class PeliculaController {

    private final PeliculaService peliculaService;

    @Autowired
    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    //buscar peliculas por nombre
    @GetMapping("/search")
    public ResponseEntity<String> searchMovies(@RequestParam String query) {
        String result = peliculaService.searchMovieBy(query);
        return ResponseEntity.ok(result);
    }

    //mostrar peliculas en emision actual
    @GetMapping("/now-playing")
    public ResponseEntity<String> getNowPlayingMovies(){
        String result = peliculaService.getNowPlaying();
        return ResponseEntity.ok(result);
    }

    //ver detalles de pelicula
    @GetMapping("/details/{id}")
    public ResponseEntity<String> getMovieDetails(@PathVariable long id){
        String result=peliculaService.getMoviesDetails(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detailsByGender/{id}")
    public ResponseEntity<String> getMovieDetailsByGender(@PathVariable int id){
        String result=peliculaService.searchMoviesByGenre(id);
        return ResponseEntity.ok(result);
    }

}
