package edu.pucmm.icc352.controladores.backend;

import edu.pucmm.icc352.modelo.Cliente;
import edu.pucmm.icc352.services.ClienteServices;
import edu.pucmm.icc352.util.CrudOpenApi;

import io.javalin.http.Context;
import io.javalin.openapi.*;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

public class ClienteControlador implements CrudOpenApi<Cliente> {
    private final ClienteServices clienteServices = ClienteServices.getInstancia();

    @OpenApi(
            path = "/api/clients",
            methods = HttpMethod.GET,
            responses = {@OpenApiResponse(status = "200", description = "Listado de Clientes", content = {@OpenApiContent(from = Cliente.class)})}
    )
    public void getAll(Context ctx) {
        ctx.json(clienteServices.getAll());
    }

    @OpenApi(
            path = "/api/clients/{client-id}",
            methods = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "client-id", required = true, type = ObjectId.class )},
            responses = {@OpenApiResponse(status = "200", description = "Cliente por id", content = {@OpenApiContent(from = Cliente.class)})}
    )
    public void getOne(@NotNull Context ctx) {
        try {
            // Convertir el id de String a ObjectId
            ObjectId objectId = new ObjectId(ctx.pathParam("client-id"));
            Cliente cliente = clienteServices.getById(objectId);
            if (cliente != null) {
                ctx.json(cliente);
            } else {
                ctx.status(404);
            }
        } catch (IllegalArgumentException e) {
            // Si el id no es válido, retornamos un error
            ctx.status(400).json("ID inválido");
        }
    }

    @OpenApi(
            path = "/api/clients",
            methods = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = Cliente.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Crear Clientes", content = {@OpenApiContent(from = Cliente.class)})}
    )
    public void create(Context ctx) {
        Cliente nueva = ctx.bodyAsClass(Cliente.class);
        ctx.status(201).json(clienteServices.create(nueva));
    }

    @OpenApi(
            path = "/api/clients",
            methods = HttpMethod.PUT,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = Cliente.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Actualizar Cliente", content = {@OpenApiContent(from = Cliente.class)})}
    )
    public void update(Context ctx) {
        try {
            Cliente actualizada = ctx.bodyAsClass(Cliente.class);
            Cliente cliente = clienteServices.update(actualizada.getIdCliente(), actualizada);
            if (cliente != null) {
                ctx.json(cliente);
            } else {
                ctx.status(404);
            }
        } catch (IllegalArgumentException e) {
            // Si el id no es válido, retornamos un error
            ctx.status(400).json("ID inválido");
        }
    }

    @OpenApi(
            path = "/api/clients/{client-id}",
            methods = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "client-id", required = true, type = ObjectId.class )},
            responses = {@OpenApiResponse(status = "200", description = "Eliminar Cliente por id", content = {@OpenApiContent(from = Cliente.class)})}
    )
    public void delete(@NotNull Context ctx) {
        try {
            // Convertir el id de String a ObjectId
            ObjectId objectId = new ObjectId(ctx.pathParam(ctx.pathParam("client-id")));
            if (clienteServices.delete(objectId)) {
                ctx.status(204);
            } else {
                ctx.status(404);
            }
        } catch (IllegalArgumentException e) {
            // Si el id no es válido, retornamos un error
            ctx.status(400).json("ID inválido");
        }
    }
}
