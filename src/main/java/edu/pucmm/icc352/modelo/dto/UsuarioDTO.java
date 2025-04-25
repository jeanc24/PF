package edu.pucmm.icc352.modelo.dto;

import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.util.RolesApp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {
    public String username;
    public String nombre;
    public String password;
    public String rol; // solo uno

    public Usuario toUsuario() {
        Set<RolesApp> rolesSet = new HashSet<>();
        if (rol != null) {
            rolesSet.add(RolesApp.valueOf(rol));
        }

        return new Usuario(username, nombre, password, rolesSet, new ArrayList<>());
    }

    public void updateUsuario(Usuario usuario) {
        usuario.setUsername(username);
        usuario.setNombre(nombre);
        usuario.setPassword(password);

        Set<RolesApp> rolesSet = new HashSet<>();
        if (rol != null) {
            rolesSet.add(RolesApp.valueOf(rol));
        }

        usuario.setListaRoles(rolesSet);
    }
}
