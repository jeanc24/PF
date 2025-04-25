package edu.pucmm.icc352.modelo;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import edu.pucmm.icc352.util.RolesApp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Usuario {
    @Id
    private ObjectId idUsuario;
    private String username;
    private String nombre;
    private String password;
    Set<RolesApp> listaRoles;
    @Reference
    List<URL> listaUrls;

    public Usuario(String username, String nombre, String password, Set<RolesApp> listaRoles, List<URL> listaUrls) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.listaRoles = listaRoles;
        this.listaUrls = listaUrls;
    }

    public boolean tieneRol(String rol) {
        return listaRoles.stream().anyMatch(r -> r.name().equalsIgnoreCase(rol));
    }

    public String getRolesSplitted(Usuario usuario) {
        // Comprobamos si la lista de roles del usuario es nula o vacía
        if (usuario != null && usuario.getListaRoles() != null) {
            // Creamos un StringBuilder para concatenar los roles
            StringBuilder rolesStr = new StringBuilder();
            // Recorremos la lista de roles y los añadimos al StringBuilder
            for (RolesApp rol : usuario.getListaRoles()) {
                if (rolesStr.length() > 0) {
                    rolesStr.append(", "); // Añadimos una coma y un espacio entre los roles
                }
                rolesStr.append(rol.name()); // Añadimos el nombre del rol
            }
            return rolesStr.toString(); // Devolvemos la cadena concatenada
        }
        return ""; // Si no hay roles, devolvemos una cadena vacía
    }
}
