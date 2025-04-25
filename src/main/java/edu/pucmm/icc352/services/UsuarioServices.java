package edu.pucmm.icc352.services;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import edu.pucmm.icc352.modelo.URL;
import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.util.db.MongoDbConexion;
import edu.pucmm.icc352.util.RolesApp;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static dev.morphia.query.filters.Filters.*;

public class UsuarioServices {
    private static Datastore datastore = MongoDbConexion.getInstance().getDatastore();
    private static UsuarioServices instancia;

    public static UsuarioServices getInstancia(){
        if(instancia == null){

            instancia = new UsuarioServices();
        }
        return instancia;
    }

    public static Usuario findByUsername(String username) {
        return datastore.find(Usuario.class)
                .filter(Filters.eq("username", username))
                .first();
    }


    // Verifica si existe el usuario administrador, si no lo crea
    public void crearUsuarioAdministrador() {
        // Verificar si el usuario administrador ya existe
        Usuario usuarioAdmin = datastore.find(Usuario.class).filter(Filters.eq("username", "admin")).first();

        if (usuarioAdmin == null) {
            // Crear un nuevo usuario administrador
            Set<RolesApp> roles = new HashSet<>();
            roles.add(RolesApp.ROLE_ADMIN);  // Asume que tienes un rol ADMIN en RolesApp

            Usuario nuevoAdmin = new Usuario("admin", "admin", "admin", roles, null);
            datastore.save(nuevoAdmin);
            System.out.println("Usuario administrador creado con éxito.");
        }
    }

    // CRUD
    public List<Usuario> getAll() {
        return datastore.find(Usuario.class).iterator().toList();
    }

    public Usuario getById(ObjectId id) {
        return datastore.find(Usuario.class).filter(Filters.eq("_id", id)).first();
    }

    public Usuario create(Usuario usuario) {
        datastore.save(usuario);
        return usuario;
    }

    public Usuario update(ObjectId id, Usuario usuarioActualizado) {
        Usuario existente = getById(id);
        if (existente != null) {
            usuarioActualizado.setIdUsuario(id); // mantener el ID
            datastore.save(usuarioActualizado);
            return usuarioActualizado;
        }
        return null;
    }

    public boolean delete(ObjectId id) {
        Usuario usuario = getById(id);
        if (usuario != null) {
            datastore.delete(usuario);
            return true;
        }
        return false;
    }
    public static Usuario buscarUsuarioPorIdUrl(ObjectId idUrl) {
        List<Usuario> usuarios = UsuarioServices.getInstancia().getAll(); // obtén todos los usuarios

        for (Usuario usuario : usuarios) {
            if (usuario.getListaUrls() != null) {
                for (URL url : usuario.getListaUrls()) {
                    if (url.getIdUrl().equals(idUrl)) {
                        System.out.println("usuario dueño es "+usuario.getUsername());
                        return usuario; // Encontrado
                    }
                }
            }
        }

        return null; // No se encontró ningún usuario que haya creado esa URL
    }


}
