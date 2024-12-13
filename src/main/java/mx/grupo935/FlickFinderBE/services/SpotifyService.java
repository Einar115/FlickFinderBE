package mx.grupo935.FlickFinderBE.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

@Service
public class SpotifyService {

    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spotify.api.url}")
    private String spotifyApiUrl;

    @Value("${spotify.api.client-id}")
    private String CLIENT_ID;

    @Value("${spotify.api.client-secret}")
    private String CLIENT_SECRET;

    public SpotifyService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String searchTrack(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());

        String url = UriComponentsBuilder.fromHttpUrl(spotifyApiUrl + "/search")
                .queryParam("q", query)
                .queryParam("type", "track")
                .queryParam("limit", 10)
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public String getTrackDetails(String trackId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());

        String url = spotifyApiUrl + "/tracks/" + trackId;

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String getNewReleases() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());

        String url = spotifyApiUrl + "/browse/new-releases";

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody();
    }

    public String getToken() {
        try {
            String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + encodedCredentials);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);

            // Parse the response to extract the token
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve access token: " + e.getMessage(), e);
        }
    }
}