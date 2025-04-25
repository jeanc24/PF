package edu.pucmm.icc352.controladores.backend;

import edu.pucmm.icc352.modelo.Cliente;
import edu.pucmm.icc352.modelo.URL;
import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.modelo.dto.UrlDTO;
import edu.pucmm.icc352.services.ClienteServices;
import edu.pucmm.icc352.services.UrlServices;
import edu.pucmm.icc352.services.UsuarioServices;
import edu.pucmm.icc352.util.CrudOpenApi;
import io.javalin.http.Context;
import io.javalin.openapi.*;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import ua_parser.Client;
import ua_parser.OS;
import ua_parser.Parser;
import ua_parser.UserAgent;

import java.time.ZoneId;
import java.util.*;

public class UrlControlador implements CrudOpenApi<URL> {
    private static final UrlServices urlServices = UrlServices.getInstancia();
    private static final ClienteServices clienteServices = ClienteServices.getInstancia();
    private static final UsuarioServices usuarioServices = UsuarioServices.getInstancia();

    @OpenApi(
            path = "/api/urls",
            methods = HttpMethod.GET,
            responses = {@OpenApiResponse(status = "200", description = "Listado de Urls", content = {@OpenApiContent(from = URL.class)})}
    )
    public void getAll(Context ctx) {
        ctx.json(urlServices.getAll());
    }

    @OpenApi(
            path = "/api/urls/{url-id}",
            methods = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "url-id", required = true, type = ObjectId.class )},
            responses = {@OpenApiResponse(status = "200", description = "URL por id", content = {@OpenApiContent(from = URL.class)})}
    )
    public void getOne(@NotNull Context ctx) throws Exception {
        try {
            // Convertir el id de String a ObjectId
            ObjectId objectId = new ObjectId(ctx.pathParam("url-id"));
            URL url = urlServices.getById(objectId);
            if (url != null) {
                ctx.json(url);
            } else {
                ctx.status(404).json(Map.of("error", "URL no encontrada")); // Respuesta JSON
            }
        } catch (IllegalArgumentException e) {
            // Si el id no es válido, retornamos un error
            ctx.status(400).json(Map.of("error", "ID inválido")); // Respuesta JSON
        }
    }

    @OpenApi(
            path = "/api/urls",
            methods = HttpMethod.POST,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = URL.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Crear Urls", content = {@OpenApiContent(from = URL.class)})}
    )
    public void create(Context ctx) {
        URL temp = ctx.bodyAsClass(URL.class);

        URL creada = urlServices.create(temp);
        if(ctx.sessionAttribute("usuario") != null) {
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario.getListaUrls() == null) {
                usuario.setListaUrls(new ArrayList<>());
            }
            usuario.getListaUrls().add(creada);
            UsuarioServices.getInstancia().update(usuario.getIdUsuario(), usuario);
        }else {
            List<URL> lista = ctx.sessionAttribute("urlsNoRegistrado");
            if(lista == null) {
                lista = new ArrayList<>();
            }
            lista.add(creada);
            ctx.sessionAttribute("urlsNoRegistrado", lista);
        }

        ctx.status(201).json(creada);
    }

    @OpenApi(
            path = "/api/urls",
            methods = HttpMethod.PUT,
            requestBody = @OpenApiRequestBody(required = true, content = {@OpenApiContent(from = URL.class)}),
            responses = {@OpenApiResponse(status = "200", description = "Actualizar Url", content = {@OpenApiContent(from = URL.class)})}
    )
    public void update(Context ctx) {
        try {
            URL actualizada = ctx.bodyAsClass(URL.class);
            URL url = urlServices.update(actualizada.getIdUrl(), actualizada);
            if (url != null) {
                ctx.json(url);
            } else {
                ctx.status(404);
            }
        } catch (IllegalArgumentException e) {
            // Si el id no es válido, retornamos un error
            ctx.status(400).json("ID inválido");
        }
    }

    @OpenApi(
            path = "/api/urls/{shortcode}",
            methods = HttpMethod.DELETE,
            pathParams = {@OpenApiParam(name = "shortcode", required = true, type = String.class)},
            responses = {@OpenApiResponse(status = "204", description = "Eliminar URL por shortcode")}
    )
    public void delete(@NotNull Context ctx) {
        try {
            String shortcode = "http://localhost:7001/api/cut/" + ctx.pathParam("shortcode");
            URL url = urlServices.findByShortCode(shortcode);

            if (url == null) {
                ctx.status(404).json("URL no encontrada");
                return;
            }

            // Buscar el usuario dueño
            Usuario usuarioURL= usuarioServices.buscarUsuarioPorIdUrl(url.getIdUrl());

            if (usuarioURL != null && usuarioURL.getListaUrls() != null) {
                System.out.println("informaciones:");
                System.out.println(url.getUrlAcortada());
                System.out.println(usuarioURL.getUsername());
                System.out.println("lista url: "+usuarioURL.getListaUrls());
                usuarioURL.getListaUrls().removeIf(u -> u.getIdUrl().equals(url.getIdUrl()));
                usuarioServices.update(usuarioURL.getIdUsuario(), usuarioURL);
            }

            if (urlServices.deleteByShortcode(shortcode)) {
                Usuario usuarioSesion = ctx.sessionAttribute("usuario");
                ctx.sessionAttribute("usuario", usuarioSesion);
                ctx.status(204); // No Content
            } else {
                ctx.status(500).json("Error al eliminar la URL");
            }

        } catch (IllegalArgumentException e) {
            ctx.status(400).json("Shortcode inválido");
        } catch (Exception e) {
            ctx.status(500).json("Error inesperado: " + e.getMessage());
        }
    }



    @OpenApi(
            path = "/api/lista",
            methods = HttpMethod.GET,
            responses = {@OpenApiResponse(status = "200", description = "Listado de Urls para user", content = {@OpenApiContent(from = URL.class)})}
    )
    public void listaUrls(@NotNull Context context) {
        boolean estaUsuario = verificarUsuarioAutenticado(context);
        List<UrlDTO> listaDTO;

        if (estaUsuario) {
            Usuario usuario = context.sessionAttribute("usuario");

            if (usuario.tieneRol("ROLE_ADMIN")) {
                // Si es ADMIN, mostrar todas las URLs
                List<URL> todas = urlServices.getAll();
                listaDTO = todas.stream().map(UrlDTO::new).toList();
            } else if (usuario.getListaUrls() != null) {
                listaDTO = usuario.getListaUrls().stream().map(UrlDTO::new).toList();
            } else {
                listaDTO = new ArrayList<>();
            }

        } else {
            List<URL> listaUrls = context.sessionAttribute("urlsNoRegistrado");
            listaDTO = listaUrls != null ? listaUrls.stream().map(UrlDTO::new).toList() : new ArrayList<>();
        }

        context.status(200).json(listaDTO);
    }


    @OpenApi(
            path = "/api/cut/{shortcode}",
            methods = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "shortcode", required = true, type = String.class )},
            responses = {@OpenApiResponse(status = "200", description = "Redirigir por url acortada", content = {@OpenApiContent(from = URL.class)})}
    )
    public void redirigirUrlOriginal(Context ctx) {
        String codigoUrl = "http://localhost:7001/api/cut/" + ctx.pathParam("shortcode");
        System.out.println("primera parte");
        URL url = urlServices.findByShortCode(codigoUrl);
        if (url != null) {
            System.out.println("adentro del if");
            // Obtener información del cliente
            String userAgentString = ctx.header("User-Agent");
            String ipCliente = ctx.ip();
            Date fechaAcceso = new Date();

            // Analizar el User-Agent
            Parser uaParser = new Parser();
            Client client = uaParser.parse(userAgentString);
            UserAgent userAgent = client.userAgent;
            OS os = client.os;

            String navegador = userAgent.family + " " + userAgent.major;
            String sistemaOperativo = os.family;

            // Crear objeto Cliente
            Cliente cliente = new Cliente(navegador, ipCliente, sistemaOperativo, fechaAcceso);
            clienteServices.create(cliente);

            // Agregar cliente a la lista de clientes de la URL
            if (url.getListaClientes() == null) {
                url.setListaClientes(new ArrayList<>());
            }
            url.getListaClientes().add(cliente);
            // Aumentar contador de accesos
            url.setCantidadAccesos(url.getCantidadAccesos() + 1);
            // Guardar actualización de la URL
            urlServices.update(url.getIdUrl(), url);
            ctx.redirect(url.getUrlOriginal());
        } else {
            ctx.status(404).json(Map.of("error", "URL no encontrada")); // Respuesta JSON
        }
    }


    @OpenApi(
            path = "/api/urls/detalles/{urlAcortada}",
            methods = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "urlAcortada", required = true, type = ObjectId.class)},
            responses = {@OpenApiResponse(status = "200", description = "Detalles completos de URL con estadísticas", content = {@OpenApiContent(from = URL.class)})}
    )
    public void getDetallesUrl(Context ctx) {
        try {
            System.out.println("estoy aqui en getDetallesUrl");
            URL url = urlServices.findByShortCode(ctx.pathParam("urlAcortada"));

            if (url != null) {
                // Podemos procesar los datos aquí si necesitamos cálculos especiales
                System.out.println("estoy aqui en url diferente de null");
                Map<String, Object> response = new HashMap<>();
                response.put("url", url);
                response.put("estadisticas", calcularEstadisticas(url));

                ctx.json(response);
            } else {
                System.out.println("es nula");
                ctx.status(404).json(Map.of("error", "URL no encontrada"));
            }
        } catch (IllegalArgumentException e) {
            ctx.status(400).json(Map.of("error", "ID inválido"));
        }
    }

    private Map<String, Object> calcularEstadisticas(URL url) {
        Map<String, Object> stats = new HashMap<>();

        // Visitas por día
        Map<String, Integer> visitasPorDia = new LinkedHashMap<>();
        // Navegadores
        Map<String, Integer> navegadores = new HashMap<>();
        // Sistemas operativos
        Map<String, Integer> sistemas = new HashMap<>();

        if(url.getListaClientes() != null) {
            for(Cliente cliente : url.getListaClientes()) {
                // Visitas por día
                String fecha = cliente.getFechaAcceso().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString();
                visitasPorDia.put(fecha, visitasPorDia.getOrDefault(fecha, 0) + 1);

                // Navegadores
                navegadores.put(cliente.getNavegador(),
                        navegadores.getOrDefault(cliente.getNavegador(), 0) + 1);

                // Sistemas operativos
                sistemas.put(cliente.getSistemaOperativo(),
                        sistemas.getOrDefault(cliente.getSistemaOperativo(), 0) + 1);
            }
        }

        stats.put("visitasPorDia", visitasPorDia);
        stats.put("navegadores", navegadores);
        stats.put("sistemasOperativos", sistemas);

        return stats;
    }

    //- - - - - - - - - - - - - - -
    //          U T I L S
    //- - - - - - - - - - - - - - -

    private boolean verificarUsuarioAutenticado(Context ctx) {
        // Verificar si hay un usuario autenticado
        Usuario usuario = ctx.sessionAttribute("usuario");
        if (usuario != null) {
            System.out.println("Usuario autenticado: " + usuario);  // Verifica si el usuario está autenticado
            return true;
        } else {
            // Usuario no autenticado: manejar las URLs en la sesión
            List<URL> urlsNoRegistrado = ctx.sessionAttribute("urlsNoRegistrado");
            if (urlsNoRegistrado == null) {
                urlsNoRegistrado = new ArrayList<>();
                ctx.sessionAttribute("urlsNoRegistrado", urlsNoRegistrado);
            }
            return false;
        }
    }

}
