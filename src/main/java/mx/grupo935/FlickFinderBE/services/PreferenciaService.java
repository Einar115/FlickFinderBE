package mx.grupo935.FlickFinderBE.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import mx.grupo935.FlickFinderBE.models.Preferencia;
import mx.grupo935.FlickFinderBE.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PreferenciaService{
    @Value("${nfs.mount.directory}")
    private String NFS_DIRECTORY;

    @Value("${encryption.key}")
    private String SECRET_KEY;

    @Value("${encryption.salt}")
    private String SALT;

    private final ObjectMapper objectMapper;
    private TextEncryptor encryptor;


    public PreferenciaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        this.encryptor = Encryptors.text(SECRET_KEY, SALT);
        try {
            Files.createDirectories(Paths.get(NFS_DIRECTORY + "/preferencias"));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento", e);
        }
    }

    public Preferencia getPreferenciaById(long id, Usuario usuario) throws IOException{
        String filename = NFS_DIRECTORY+"/preferencias/usuario"+usuario.getId()+"/preferencia_"+id+"-json";
        if (!Files.exists(Paths.get(filename))){
            return null;
        }

        String encryptedJson = Files.readString(Paths.get(filename));
        Map<String, Object> decryptedMap = objectMapper.readValue(encryptedJson, Map.class);
        decryptedMap.replaceAll((key, value) -> value == null ? null: encryptor.decrypt(value.toString()));

        return objectMapper.convertValue(decryptedMap, Preferencia.class);

    }

    public List<Preferencia> getAllPreferencias(Usuario usuario) throws IOException {
        // Directorio de preferencias del usuario
        String userDir = NFS_DIRECTORY + "/preferencias/usuario" + usuario.getId() + "/";
        File directory = new File(userDir);

        // Verificar si el directorio existe
        if (!directory.exists() || !directory.isDirectory()) {
            return new ArrayList<>(); // Si no hay preferencias, devolver lista vacía
        }

        // Listar los archivos de preferencias en el directorio
        File[] preferenciaFiles = directory.listFiles((dir, name) -> name.startsWith("preferencia_") && name.endsWith(".json"));
        if (preferenciaFiles == null || preferenciaFiles.length == 0) {
            return new ArrayList<>(); // Sin archivos de preferencias
        }

        // Procesar cada archivo y construir la lista de preferencias
        List<Preferencia> preferencias = new ArrayList<>();
        for (File file : preferenciaFiles) {
            try {
                // Leer el contenido cifrado del archivo
                String encryptedJson = Files.readString(file.toPath());

                // Descifrar y convertir a Preferencia
                Map<String, Object> decryptedMap = objectMapper.readValue(encryptedJson, Map.class);
                decryptedMap.replaceAll((key, value) -> value == null ? null : encryptor.decrypt(value.toString()));
                Preferencia preferencia = objectMapper.convertValue(decryptedMap, Preferencia.class);

                preferencias.add(preferencia);
            } catch (IOException e) {
                // Manejar errores de archivo o formato JSON inválido
                System.err.println("Error al leer la preferencia del archivo: " + file.getName());
                e.printStackTrace();
            }
        }

        return preferencias;
    }

    public Preferencia savePreferencia(Preferencia preferencia, Usuario usuario) throws IOException {
        // Crear el directorio si no existe
        Files.createDirectories(Paths.get(NFS_DIRECTORY + "/preferencias/usuario" + usuario.getId()));

        // Obtener todas las preferencias del usuario
        List<Preferencia> preferenciasExistentes = getAllPreferencias(usuario);

        // Validar si ya existe una preferencia con el mismo referenciaId y tipo
        boolean existe = preferenciasExistentes.stream().anyMatch(p ->
                p.getReferenciaId() != null &&
                        p.getReferenciaId().equals(preferencia.getReferenciaId()) &&
                        p.getTipo().equals(preferencia.getTipo())
        );


        if (existe) {
            throw new IllegalArgumentException("Ya existe una preferencia con el mismo 'referenciaId' y 'tipo' para este usuario.");
        }

        // Generar un ID único para la nueva preferencia
        long preferenciaId = generateUniqueId(usuario);
        preferencia.setId(preferenciaId);

        // Convertir la preferencia a un mapa y cifrar los valores
        Map<String, Object> preferenciaMap = objectMapper.convertValue(preferencia, Map.class);
        preferenciaMap.replaceAll((key, value) -> value == null ? null : encryptor.encrypt(value.toString()));
        String encryptedPreferenciaJson = objectMapper.writeValueAsString(preferenciaMap);

        // Guardar el archivo en el sistema
        String filename = NFS_DIRECTORY + "/preferencias/usuario" + usuario.getId() + "/preferencia_" + preferenciaId + ".json";
        Files.writeString(Paths.get(filename), encryptedPreferenciaJson);

        System.out.println(preferencia.toString());
        return preferencia;
    }

    public boolean deletePreferencia(String referenciaId, Usuario usuario) throws IOException {
        String userDir = NFS_DIRECTORY + "/preferencias/usuario" + usuario.getId() + "/";
        File directory = new File(userDir);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("El directorio no existe para el usuario: " + usuario.getId());
            return false; // No hay preferencias
        }
        File[] preferenciaFiles = directory.listFiles((dir, name) -> name.startsWith("preferencia_") && name.endsWith(".json"));
        if (preferenciaFiles == null || preferenciaFiles.length == 0) {
            System.out.println("No se encontraron preferencias para el usuario: " + usuario.getId());
            return false; // Sin archivos de preferencias
        }
        for (File file : preferenciaFiles) {
            String encryptedJson = Files.readString(file.toPath());
            Map<String, Object> decryptedMap = objectMapper.readValue(encryptedJson, Map.class);
            decryptedMap.replaceAll((key, value) -> value == null ? null : encryptor.decrypt(value.toString()));

            Preferencia preferencia = objectMapper.convertValue(decryptedMap, Preferencia.class);

            if (preferencia.getReferenciaId().equals(referenciaId)) {
                try {
                    Files.delete(file.toPath());
                    System.out.println("Preferencia eliminada: " + file.getName());
                    return true;
                } catch (IOException e) {
                    System.err.println("Error al eliminar la preferencia: " + e.getMessage());
                    throw e;
                }
            }
        }
        System.out.println("No se encontró ninguna preferencia con referenciaId: " + referenciaId);
        return false;
    }


    private long generateUniqueId(Usuario usuario) {
        File storageDir = new File(NFS_DIRECTORY + "/preferencias/usuario"+usuario.getId()+"/");
        File[] preferenciaFiles = storageDir.listFiles((dir, name) -> name.startsWith("preferencia_") && name.endsWith(".json"));

        if (preferenciaFiles == null || preferenciaFiles.length == 0) {
            return 1;
        }

        int maxNumber = 0;
        for (File file : preferenciaFiles) {
            String name = file.getName();
            String numberPart = name.replace("preferencia_", "").replace(".json", "");
            try {
                int number = Integer.parseInt(numberPart);
                maxNumber = Math.max(maxNumber, number);
            } catch (NumberFormatException ignored) {
            }
        }

        return maxNumber + 1;
    }

}