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

    //Buscar Peluculas
    public String searchMovie(String query){
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

    public String getMoviesDetails(long movieId){
        String url = apiUrl + "/movie/"+movieId+"?api_key="+apiKey+"&language=es-ES";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
