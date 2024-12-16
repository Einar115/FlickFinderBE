package mx.grupo935.FlickFinderBE.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.grupo935.FlickFinderBE.models.Preferencia;
import mx.grupo935.FlickFinderBE.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class RecomendacionService {
    @Autowired
    private PeliculaService peliculaService;
    @Autowired
    private SpotifyService spotifyService;
    @Autowired
    private PreferenciaService preferenciaService;

    public List<String> recomendarPeliculas(Usuario usuario, int limite) throws IOException {
        // Obtener las preferencias del usuario
        List<Preferencia> preferencias = preferenciaService.getAllPreferencias(usuario);

        // Extraer los referenciaId de las preferencias de tipo "pelicula"
        Set<String> referenciaIds = preferencias.stream()
                .filter(pref -> "pelicula".equalsIgnoreCase(pref.getTipo()))
                .map(Preferencia::getReferenciaId)
                .collect(Collectors.toSet());

        // Conjunto para almacenar los IDs de género únicos
        Set<Integer> genreIds = new HashSet<>();

        // Iterar sobre los referenciaId para obtener los detalles y extraer los géneros
        for (String referenciaId : referenciaIds) {
            try {
                String detallesPeliculaJson = peliculaService.getMoviesDetails(Long.parseLong(referenciaId));

                // Convertir los detalles a un Map
                Map<String, Object> detalles = new ObjectMapper().readValue(detallesPeliculaJson, Map.class);

                // Extraer los géneros desde los detalles (genre_ids)
                List<Map<String, Object>> listaGeneros = (List<Map<String, Object>>) detalles.get("genres");
                if (listaGeneros != null) {
                    for (Map<String, Object> genero : listaGeneros) {
                        genreIds.add((Integer) genero.get("id"));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al procesar los detalles para referenciaId " + referenciaId + ": " + e.getMessage());
            }
        }

        // Usar los genreIds únicos para buscar películas recomendadas y aplicar el límite
        List<String> recomendacionesPeliculas = genreIds.stream()
                .map(genreId -> {
                    try {
                        return peliculaService.searchMoviesByGenre(genreId);
                    } catch (Exception e) {
                        System.err.println("Error al buscar películas para genreId " + genreId + ": " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .limit(limite) // Aplicar el límite de resultados
                .collect(Collectors.toList());

        return recomendacionesPeliculas;
    }

    /*public List<String> recomendarAlbumes(Usuario usuario) throws IOException {
        // Obtener preferencias del usuario
        List<Preferencia> preferencias = preferenciaService.getAllPreferencias(usuario);

        // Filtrar preferencias por tipo "album" para obtener géneros y artistas
        Set<String> generosAlbumes = preferencias.stream()
                .filter(pref -> "album".equalsIgnoreCase(pref.getTipo()))
                .map(pref -> String.valueOf(pref.getReferenciaId())) // Se asume que ReferenciaId contiene el género
                .collect(Collectors.toSet());

        // Generar recomendaciones de álbumes por género
        List<String> recomendacionesPorGenero = generosAlbumes.stream()
                .map(spotifyService::searchAlbumsByGenre)
                .collect(Collectors.toList());

        // Si también deseas buscar por artista, puedes incluir esta lógica:
        Set<String> artistasFavoritos = preferencias.stream()
                .filter(pref -> "album".equalsIgnoreCase(pref.getTipo()))
                .map(pref -> String.valueOf(pref.getReferenciaId())) // Asume que hay una separación para géneros y artistas
                .collect(Collectors.toSet());

        List<String> recomendacionesPorArtista = artistasFavoritos.stream()
                .map(spotifyService::searchAlbumsByArtist)
                .collect(Collectors.toList());

        // Combinar resultados
        List<String> recomendacionesAlbumes = new ArrayList<>();
        recomendacionesAlbumes.addAll(recomendacionesPorGenero);
        recomendacionesAlbumes.addAll(recomendacionesPorArtista);

        return recomendacionesAlbumes;
    }*/

    public String recomendarAlbumes(Usuario usuario, int limite) throws IOException {
        // Obtener las preferencias del usuario
        List<Preferencia> preferencias = preferenciaService.getAllPreferencias(usuario);
        System.out.println("Preferencias del usuario: " + preferencias);

        // Extraer los referenciaId (IDs de álbumes)
        Set<String> referenciaIds = preferencias.stream()
                .filter(pref -> "album".equalsIgnoreCase(pref.getTipo()))
                .map(Preferencia::getReferenciaId)
                .collect(Collectors.toSet());
        System.out.println("Referencia IDs (álbumes): " + referenciaIds);

        StringBuilder recomendacionesAlbumes = new StringBuilder();

        for (String albumId : referenciaIds) {
            try {
                // Obtener detalles del álbum para extraer el artista principal
                String albumDetailsJson = spotifyService.getAlbumDetails(albumId);
                System.out.println("Detalles del álbum: " + albumDetailsJson);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode albumNode = objectMapper.readTree(albumDetailsJson);
                String artistId = albumNode.path("artists").get(0).get("id").asText();
                System.out.println("Artista principal del álbum: " + artistId);

                // Buscar álbumes relacionados por artista
                String resultadosAlbumesJson = spotifyService.searchAlbumsByArtist(artistId);
                System.out.println("Álbumes relacionados del artista: " + resultadosAlbumesJson);

                // Añadir resultados al string acumulativo
                recomendacionesAlbumes.append(resultadosAlbumesJson).append("\n");
            } catch (Exception e) {
                System.err.println("Error al buscar álbumes para el álbum con ID " + albumId + ": " + e.getMessage());
            }
        }

        // Retornar el resultado como un único String
        return recomendacionesAlbumes.toString().trim();
    }


}
