package mx.grupo935.FlickFinderBE.services;

import mx.grupo935.FlickFinderBE.models.Usuario;
import mx.grupo935.FlickFinderBE.others.Constantes;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    public Long getNextId() {
        File directorio = new File(Constantes.NFS_DIRECTORY+"/users");
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crea la carpeta si no existe
        }

        // Filtra los archivos que terminan con .dat
        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".dat"));

        // Si no hay archivos, el próximo ID será 1
        return (archivos != null) ? (long) archivos.length + 1 : 1L;
    }

    public void registerUser(Usuario usuario) {
        Long userId = getNextId();
        usuario.setId(userId);

        // Aquí guardarías al usuario en el archivo binario en NFS
        saveUserToFile(usuario);
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        File directorio = new File(Constantes.NFS_DIRECTORY+"/users");
        if (!directorio.exists()) {
            return usuarios; // Devuelve una lista vacía si el directorio no existe
        }

        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".dat"));
        if (archivos != null) {
            for (File archivo : archivos) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                    usuarios.add((Usuario) ois.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return usuarios;
    }


    private void saveUserToFile(Usuario usuario) {
        File directorio = new File(Constantes.NFS_DIRECTORY+"/users");
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crea la carpeta si no existe
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directorio + "/usuario_" + usuario.getId() + ".dat"))) {
            oos.writeObject(usuario); // Guarda el usuario en un archivo separado
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el usuario en archivo");
        }
    }

}
