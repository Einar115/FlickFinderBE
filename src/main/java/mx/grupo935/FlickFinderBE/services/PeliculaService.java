package mx.grupo935.FlickFinderBE.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PeliculaService {
    private final RestTemplate restTemplate;

    @Value("${api.tmdb.key}")
    private String apiKey;

    @Value("${api.tmdb.url}")
    private String apiUrl;

    //uso de restTemplate
    public PeliculaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Buscar películas por genero
    public String searchMoviesByGenre(int genreId) {
        String url = apiUrl + "/discover/movie?api_key=" + apiKey + "&language=es-ES&with_genres=" + genreId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    //Buscar Peluculas por id
    public String searchMovieBy(String query){
        String url = apiUrl + "/search/movie?api_key="+apiKey+"&query="+query;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    //Obtener peliculas en emision
    public String getNowPlaying() {
        String url = apiUrl + "/movie/now_playing?api_key=" + apiKey + "&language=en-US&page=1";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    //Obtener detalles de pelicula buscando la pelicula con su ID
    public String getMoviesDetails(long movieId){
        String url = apiUrl + "/movie/"+movieId+"?api_key="+apiKey+"&language=es-ES";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
