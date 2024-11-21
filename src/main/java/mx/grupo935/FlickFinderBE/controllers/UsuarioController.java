package mx.grupo935.FlickFinderBE.controllers;

import mx.grupo935.FlickFinderBE.models.Usuario;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private static final String NFS_DIRECTORY = "D:/Redes III/users";

    public UsuarioController(){
        try{
            Files.createDirectories(Paths.get(NFS_DIRECTORY));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/register")
    public String registrarUsuario(@RequestBody Usuario usuario){

        List<Usuario> usuarios=leerUsuarios();

        usuarios.add(usuario);

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NFS_DIRECTORY+"/usuarios.dat"))) {
            oos.writeObject(usuarios);
        }catch (IOException e){
            return "Error al guardar el usuario"+e.getMessage();
        }
        return "usuario guardado exitosamente";
    }

    @GetMapping("/all")
    public List<Usuario> getAllUsuarios(){
        return leerUsuarios();
    }

    @SuppressWarnings("unchecked")
    private List<Usuario> leerUsuarios(){
        File arch = new File(NFS_DIRECTORY+"/usuarios.dat");
        if (!arch.exists()){
            return new ArrayList<>();
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arch))){
            return (List<Usuario>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            return new ArrayList<>();
        }
    }
}
