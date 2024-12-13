package mx.grupo935.FlickFinderBE.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import mx.grupo935.FlickFinderBE.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {
    @Value("${nfs.mount.directory}")
    private String NFS_DIRECTORY;

    @Value("${encryption.key}")
    private String SECRET_KEY;

    @Value("${encryption.salt}")
    private String SALT;

    private final ObjectMapper objectMapper;
    private TextEncryptor encryptor;

    public UsuarioService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        this.encryptor = Encryptors.text(SECRET_KEY,SALT);
        try {
            Files.createDirectories(Paths.get(NFS_DIRECTORY+"/users"));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de almacenamiento", e);
        }
    }

    public void saveUser(Usuario usuario) throws IOException {
        if (isDuplicate(usuario)) {
            throw new IllegalArgumentException("Ya existe una cuenta con el mismo nombre de usuario o correo electrónico.");
        }

        int userId = generateUniqueId();
        usuario.setId(userId);
        usuario.setPassword(encryptor.encrypt(usuario.getPassword()));

        System.out.println("Usuario original: " + usuario.toString());
        Map<String, Object> userMap = objectMapper.convertValue(usuario, Map.class);
        userMap.replaceAll((key, value) -> value == null ? null : encryptor.encrypt(value.toString()));
        String encryptedUserJson = objectMapper.writeValueAsString(userMap);

        String filename = NFS_DIRECTORY + "/users/usuario_" + userId + ".json";
        System.out.println("Guardando archivo en: " + filename);
        System.out.println("Contenido encriptado: " + encryptedUserJson);
        Files.writeString(Paths.get(filename), encryptedUserJson);
    }

    private boolean isDuplicate(Usuario usuario) throws IOException {
        File storageDir = new File(NFS_DIRECTORY + "/users/");
        File[] usuarioFiles = storageDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (usuarioFiles != null) {
            for (File usuarioFile : usuarioFiles) {
                String encryptedUserJson = Files.readString(usuarioFile.toPath());
                Map<String, Object> encryptedUserMap = objectMapper.readValue(encryptedUserJson, Map.class);

                // Desencriptar los valores
                encryptedUserMap.replaceAll((key, value) -> value == null ? null : encryptor.decrypt(value.toString()));

                // Convertir a Usuario para comparar
                Usuario existingUser = objectMapper.convertValue(encryptedUserMap, Usuario.class);

                if (existingUser.getNombreUsuario().equals(usuario.getNombreUsuario()) ||
                        existingUser.getCorreo().equals(usuario.getCorreo())) {
                    return true; // Ya existe un usuario con el mismo nombre o correo
                }
            }
        }
        return false; // No hay duplicados
    }

    public Usuario getUsuarioByEmail(String email) throws IOException {
        List<Usuario> usuarios = getAllUsers();
        return usuarios.stream()
                .filter(user -> user.getCorreo().equals(email))
                .findFirst()
                .orElse(null);
    }


    public Usuario getUsuarioByNombreUsuario(String nombreUsuario) throws IOException {
        List<Usuario> usuarios = getAllUsers();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombreUsuario)) {
                return usuario; // Retorna el usuario si coincide
            }
        }
        return null; // Retorna null si no encuentra coincidencias
    }

    /*public Usuario validarUsuario(String nombreUsuario, String password) throws IOException{
        List<Usuario> usuarios = getAllUsers();

        for (Usuario usuario: usuarios){
            if (usuario.getNombreUsuario().equals(nombreUsuario) && usuario.getPassword().equals(password))
                return usuario;
        }
        throw new IllegalArgumentException("Credenciales invalidas");
    }*/
    public Usuario validarUsuario(String nombreUsuario, String password) throws IOException {
        Usuario usuario = getUsuarioByNombreUsuario(nombreUsuario);
        if (usuario != null && encryptor.decrypt(usuario.getPassword()).equals(password)) {
            return usuario; // Credenciales válidas
        }
        throw new IllegalArgumentException("Credenciales inválidas");
    }


    public List<Usuario> getAllUsers() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        File storageDir = new File(NFS_DIRECTORY + "/users/");
        File[] usuarioFiles = storageDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (usuarioFiles != null) {
            for (File usuarioFile : usuarioFiles) {
                String encryptedUserJson = Files.readString(usuarioFile.toPath());
                Map<String, Object> encryptedUserMap = objectMapper.readValue(encryptedUserJson, Map.class);
                encryptedUserMap.replaceAll((key, value) -> value == null ? null : encryptor.decrypt(value.toString()));
                usuarios.add(objectMapper.convertValue(encryptedUserMap, Usuario.class));
            }
        }
        return usuarios;
    }

    public void deleteUsuario(String nombreUsuario) throws IOException{
        String filename = NFS_DIRECTORY+"/users/"+nombreUsuario+".json";
        Files.deleteIfExists(Paths.get(filename));
    }

    private int generateUniqueId() {
        File storageDir = new File(NFS_DIRECTORY + "/users/");
        File[] usuarioFiles = storageDir.listFiles((dir, name) -> name.startsWith("usuario_") && name.endsWith(".json"));

        if (usuarioFiles == null || usuarioFiles.length == 0) {
            return 1; // Primer ID
        }

        int maxNumber = 0;
        for (File file : usuarioFiles) {
            String name = file.getName();
            String numberPart = name.replace("usuario_", "").replace(".json", "");
            try {
                int number = Integer.parseInt(numberPart);
                maxNumber = Math.max(maxNumber, number);
            } catch (NumberFormatException ignored) {
            }
        }

        return maxNumber + 1; // Siguiente ID
    }


}
