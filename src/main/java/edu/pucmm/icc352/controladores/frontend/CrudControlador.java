package edu.pucmm.icc352.controladores.frontend;

import edu.pucmm.icc352.controladores.backend.UsuarioControlador;
import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.services.UsuarioServices;
import edu.pucmm.icc352.util.BaseControlador;
import edu.pucmm.icc352.util.RolesApp;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.jetbrains.annotations.NotNull;

import javax.management.relation.Role;
import java.util.*;

public class CrudControlador extends BaseControlador {
    private static final UsuarioServices usuarioServices = UsuarioServices.getInstancia();
    private static final UsuarioControlador usuarioControlador = new UsuarioControlador();

    //constructor
    public CrudControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.beforeMatched("/*", this::verificarAcceso);
        app.get("/crud-usuarios", this::getCrud,RolesApp.ROLE_ADMIN);
        app.get("/crud-usuarios/crear", this::getCrear,RolesApp.ROLE_ADMIN);
        app.get("/crud-usuarios/editar/{user-id}", this::getEditar,RolesApp.ROLE_ADMIN);
        app.get("/crud-usuarios/eliminar/{user-id}", this::eliminarGet,RolesApp.ROLE_ADMIN);
        app.post("/crud-usuarios/crear", this::postCrear,RolesApp.ROLE_ADMIN);
        app.post("/crud-usuarios/editar/{user-id}", this::postEditar,RolesApp.ROLE_ADMIN);
    }

    private void verificarAcceso(@NotNull Context ctx) {

        if (ctx.routeRoles().isEmpty() || ctx.routeRoles().contains(RolesApp.NO_REGISTRADO)) return; // Si la ruta no requiere roles, permitir

        Usuario usuario = ctx.sessionAttribute("usuario");
        if (usuario == null) {
            ctx.redirect("/login");
            return;
        }

        if (!(usuario.tieneRol("ROLE_ADMIN"))) {
            ctx.redirect("/");
        }
    }


    public void getCrud(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        Usuario usuario = ctx.sessionAttribute("usuario");
        model.put("usuarioSesion", usuario);
        model.put("usuarios", UsuarioServices.getInstancia().getAll());
        model.put("titulo", "Gestion de Usuarios");
        ctx.render("template/crud/crudUsuario.html", model);
    }


    public void getEditar(Context ctx) {
        Usuario usuario = usuarioControlador.findUsuarioById(ctx);
        Map<String, Object> model = new HashMap<>();
        model.put("titulo", "Editar Usuario");
        model.put("roles", RolesApp.values());
        model.put("usuario", usuario);
        model.put("action", "/crud-usuarios/editar/" + usuario.getIdUsuario());
        ctx.render("template/crud/crearUsuario.html", model);
    }
    public void postEditar(@NotNull Context ctx) {
        usuarioControlador.update(ctx);
        ctx.redirect("/crud-usuarios"); // redirigir a la lista de usuarios
    }

    public void getCrear(Context ctx) {{
        Map<String, Object> model = new HashMap<>();
        model.put("roles", RolesApp.values());
        model.put("titulo", "Crear Usuario");
        model.put("usuario", new Usuario());
        model.put("action","/crud-usuarios/crear");
        ctx.render("template/crud/crearUsuario.html", model);}
    }

    public void postCrear(Context ctx) {
        usuarioControlador.create(ctx);
        ctx.redirect("/crud-usuarios"); // redirigir a la lista de usuarios
    }

    public void eliminarGet(@NotNull Context ctx) {
        usuarioControlador.delete(ctx);
        ctx.redirect("/crud-usuarios");
    }
}
