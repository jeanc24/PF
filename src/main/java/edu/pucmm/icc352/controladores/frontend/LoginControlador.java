package edu.pucmm.icc352.controladores.frontend;

import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.services.UsuarioServices;
import edu.pucmm.icc352.util.BaseControlador;
import edu.pucmm.icc352.util.RolesApp;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginControlador extends BaseControlador {
    private final UsuarioServices usuarioServices = UsuarioServices.getInstancia();

    public LoginControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.get("/signup", this::signup);
        app.get("/login", this::login);

        app.post("signup", this::signupPost);
        //app.post("/login", this::loginPost);
        app.get("/logout", this::logout);
    }

    public void signup(Context ctx) {
        System.out.println("Aqui en signup");
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("titulo", "Registro");
        modelo.put("modo", "register");
        ctx.render("/template/signup.html", modelo);
    }

    public void login(Context ctx) {
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("titulo", "Login");
        modelo.put("modo", "login");
        ctx.render("/template/login.html", modelo);
    }

    public void loginPost(Context ctx) {
        try {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            Usuario usuario = usuarioServices.findByUsername(username);
            if (usuario != null && usuario.getPassword().equals(password)) {


                ctx.sessionAttribute("usuario", usuario);
                ctx.redirect("/"); // Redirigir al home

            } else {
                // Volver al login con error
                ctx.render("/template/login.html", Map.of(
                        "titulo", "Login",
                        "modo", "login",
                        "error", "Credenciales incorrectas"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.render("/template/login.html", Map.of(
                    "titulo", "Login",
                    "modo", "login",
                    "error", "Error al procesar el login"
            ));
        }
    }

    public void logout( Context ctx) {
        ctx.req().getSession().invalidate();
        // Elimina la cookie del JWT
        ctx.removeCookie("jwt");
        ctx.redirect("/");
    }

    public void signupPost(Context ctx) {
        try {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            String nombre = ctx.formParam("nombre");

            if (username == null || password == null || nombre == null ||
                    username.isBlank() || password.isBlank() || nombre.isBlank()) {
                ctx.render("/template/signup.html", Map.of(
                        "titulo", "Registro",
                        "modo", "register",
                        "error", "Todos los campos son obligatorios"
                ));
                return;
            }

            Usuario usuarioExistente = UsuarioServices.findByUsername(username);
            if (usuarioExistente != null) {
                ctx.render("/template/signup.html", Map.of(
                        "titulo", "Registro",
                        "modo", "register",
                        "error", "El nombre de usuario ya está en uso"
                ));
                return;
            }

            Set<RolesApp> roles = new HashSet<>();
            roles.add(RolesApp.USUARIO);

            Usuario nuevoUsuario = new Usuario(username, nombre, password, roles, null);
            usuarioServices.create(nuevoUsuario);
            ctx.sessionAttribute("usuario", nuevoUsuario);
            ctx.redirect("/");

        } catch (Exception e) {
            e.printStackTrace();
            ctx.render("/template/signup.html", Map.of(
                    "titulo", "Registro",
                    "modo", "register",
                    "error", "Error al registrar el usuario"
            ));
        }
    }


}

