package edu.pucmm.icc352.controladores.frontend;

import edu.pucmm.icc352.modelo.URL;
import edu.pucmm.icc352.services.ClienteServices;
import edu.pucmm.icc352.services.UrlServices;

import edu.pucmm.icc352.util.BaseControlador;
import io.javalin.Javalin;
import io.javalin.http.Context;

import io.jsonwebtoken.Claims;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;


import java.util.*;

public class AppControlador extends BaseControlador {
    private final UrlServices urlServices = UrlServices.getInstancia();
    private final ClienteServices clienteServices = ClienteServices.getInstancia();

    //constructor
    public AppControlador(Javalin app){
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.get("/", this::mostrarPaginaInicio);
        app.get("/listar", this::listado);
        app.get("/detalles/{urlAcortada}", this::mostrarDetallesUrl);
    }

    public void mostrarPaginaInicio(Context ctx) {
        Claims jwtPayload = ctx.attribute("jwt-user");

        if (jwtPayload != null) {
            // Usuario autenticado
            ctx.render("template/index.html", Map.of(
                    "username", jwtPayload.get("username", String.class),
                    "roles", jwtPayload.get("role", String.class)
            ));
        } else {
            // Usuario no autenticado
            ctx.render("template/index.html", Map.of("noRegistrado", true));
        }
    }

    public void listado(@NotNull Context context) {
        context.render("template/crud/url/listarUrl.html");
    }

    public void mostrarDetallesUrl(Context ctx) {
        ctx.render("template/crud/url/detallesUrl.html");
    }
}
