package edu.pucmm.icc352.controladores.backend;

import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.modelo.dto.UsuarioDTO;
import edu.pucmm.icc352.services.UsuarioServices;
import edu.pucmm.icc352.util.CrudOpenApi;
import edu.pucmm.icc352.util.JwtUtil;
import edu.pucmm.icc352.util.RolesApp;
import io.javalin.http.Context;
import io.javalin.openapi.*;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class UsuarioControlador implements CrudOpenApi<Usuario> {
    private static final UsuarioServices usuarioServices = UsuarioServices.getInstancia();

    @OpenApi(
            path = "/api/users",
            methods = HttpMethod.GET,
            responses = {@OpenApiResponse(status = "200", description = "Listado de users", content = {@OpenApiContent(from = Usuario.class)})}
    )
    public void getAll(Context ctx) {
        ctx.json(usuarioServices.getAll());
    }

    @OpenApi(
            path = "/api/users/{user-id}",
            methods = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "user-id", required = true, type = ObjectId.class )},
            responses = {@OpenApiResponse(status = "200", description = "URL por id", content = {@OpenApiContent(from = Usuario.class)})}
    )
    public void getOne(@NotNull Context ctx) {
        try {
            Usuario usuario = findUsuarioById(ctx);
            if (usuario != null) {
                ctx.json(usuario);
            } else {
                ctx.status(404);
            }
        } catch (IllegalArgumentException e) {
            // Si el id no es válido, retornamos un error
            ctx.status(400).json("ID inválido");
        }
    }

    @OpenApi(
            path = "/api/users",
            methods = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = Usuario.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Crear usuario", content = {@OpenApiContent(from = Usuario.class)})}
    )
    public void create(Context ctx) {
        UsuarioDTO usuarioDTO = getDTO(ctx);
        Usuario nuevo = usuarioDTO.toUsuario();
        Set<RolesApp> roles = new HashSet<>();
        if (nuevo.getListaRoles().stream().findFirst().isPresent()) {
            roles.add(RolesApp.valueOf(nuevo.getListaRoles().stream().findFirst().get().toString()));
        }
        nuevo.setListaRoles(roles);
        ctx.status(201).json(usuarioServices.create(nuevo));
    }

    @OpenApi(
            path = "/api/users",
            methods = HttpMethod.PUT,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = Usuario.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Actualizar user", content = {@OpenApiContent(from = Usuario.class)})}
    )
    public void update(Context ctx) {
        try {
            ObjectId id = new ObjectId(ctx.pathParam("user-id"));
            Usuario original = usuarioServices.getById(id);

            if (original == null) {
                ctx.status(404).result("Usuario no encontrado");
                return;
            }

            UsuarioDTO dto = getDTO(ctx);
            dto.updateUsuario(original);
            Usuario actualizado = usuarioServices.update(id, original);

            ctx.json(actualizado);
        } catch (Exception e) {
            ctx.status(400).result("Error actualizando usuario: " + e.getMessage());
        }
    }


    @OpenApi(
            path = "/api/users/{user-id}",
            methods = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "user-id", required = true, type = ObjectId.class )},
            responses = {@OpenApiResponse(status = "200", description = "Eliminar Usuario por id", content = {@OpenApiContent(from = Usuario.class)})}
    )
    public void delete(@NotNull Context ctx) {
        try {
            // Convertir el id de String a ObjectId
            ObjectId objectId = new ObjectId(ctx.pathParam("user-id"));
            if (usuarioServices.delete(objectId)) {
                ctx.status(204);
            } else {
                ctx.status(404);
            }
        } catch (IllegalArgumentException e) {
            // Si el id no es válido, retornamos un error
            ctx.status(400).json("ID inválido");
        }
    }
    @OpenApi(
            path = "/api/login",
            methods = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(content = {}, required = true),
            responses = {@OpenApiResponse(status = "200", description = "Login JWT")}
    )
    public void login(Context ctx) {
        String username = ctx.formParam("username");
        Usuario user = usuarioServices.findByUsername(username);
        System.out.println("Rol generado: " + user.getRolesSplitted(user));

        if (user != null) {
            String token = JwtUtil.generarToken(user.getIdUsuario().toHexString(), user.getUsername(), user.getRolesSplitted(user));
            // Guardar el usuario en la sesión
            ctx.sessionAttribute("usuario", user);

            // Devolver el token
            ctx.json(Map.of("jwt", token));
            ctx.cookie("jwt", token);
            System.out.println("TOKEN GENERADO: " + token); // 👈 Asegúrate de imprimirlo aquí

        } else {
            ctx.status(401).json(Map.of("error", "Credenciales inválidas"));
        }
    }
    @OpenApi(
            path = "/api/token",
            methods = HttpMethod.GET,
            requestBody = @OpenApiRequestBody(content = {}, required = true),
            responses = {@OpenApiResponse(status = "200", description = "Crea JWT")}
    )
    public void tokenNoRegistrado(Context ctx) {
        String sessionId = java.util.UUID.randomUUID().toString();
        String token = JwtUtil.generarToken(sessionId,null, RolesApp.NO_REGISTRADO.toString());
        ctx.json(Map.of("jwt", token));
        System.out.println("TOKEN GENERADO: " + token); // 👈 Asegúrate de imprimirlo aquí

    }


    public Usuario findUsuarioById(Context ctx) {
        try {
            ObjectId objectId = new ObjectId(ctx.pathParam("user-id"));
            return usuarioServices.getById(objectId);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private UsuarioDTO getDTO(Context ctx) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.username = ctx.formParam("username");
        dto.nombre = ctx.formParam("nombre");
        dto.password = ctx.formParam("password");
        dto.rol = ctx.formParam("role");
        return dto;
    }

}
