package edu.pucmm.icc352;

import com.fasterxml.jackson.databind.node.TextNode;
import edu.pucmm.icc352.controladores.backend.ClienteControlador;
import edu.pucmm.icc352.controladores.backend.UrlControlador;
import edu.pucmm.icc352.controladores.backend.UsuarioControlador;
import edu.pucmm.icc352.controladores.frontend.AppControlador;
import edu.pucmm.icc352.controladores.frontend.CrudControlador;
import edu.pucmm.icc352.controladores.frontend.LoginControlador;

import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.services.UsuarioServices;
import edu.pucmm.icc352.util.JwtUtil;

import edu.pucmm.icc352.services.grpc.UrlServicesGrpc;

import edu.pucmm.icc352.util.RolesApp;
import edu.pucmm.icc352.util.db.MongoDbConexion;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.http.staticfiles.Location;
import io.javalin.openapi.JsonSchemaLoader;
import io.javalin.openapi.JsonSchemaResource;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.eclipse.jetty.server.session.SessionHandler;


import java.util.Map;

import java.io.IOException;


import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    private static UsuarioControlador usuarioControlador = new UsuarioControlador();
    private static UrlControlador urlControlador = new UrlControlador();
    private static ClienteControlador clienteControlador = new ClienteControlador();


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Proyecto Final");

        //Iniciando la base de datos
        MongoDbConexion conexion =MongoDbConexion.getInstance();
        conexion.inicializarDatos();

        //Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config ->{
            SessionHandler sessionHandler = new SessionHandler();
            sessionHandler.setMaxInactiveInterval(60*60);
            config.jetty.modifyServletContextHandler(servletContextHandler -> servletContextHandler.setSessionHandler(sessionHandler));
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;

            });

            config.fileRenderer(new JavalinThymeleaf());

            config.router.apiBuilder(() -> {

                //- - - - - - - - - - - - - - -
                //       B A C K E N D
                //- - - - - - - - - - - - - - -
                path("/api", () -> {
                    get("token",usuarioControlador::tokenNoRegistrado);
                    get("lista", urlControlador::listaUrls);
                    get("cut/{shortcode}", urlControlador::redirigirUrlOriginal);
                    post("/login", usuarioControlador::login);
                    path("/users", () -> {
                        get(usuarioControlador::getAll);
                        post(usuarioControlador::create);
                        put(usuarioControlador::update);
                        path("/{user-id}", () -> {
                            get(usuarioControlador::getOne);
                            delete(usuarioControlador::delete);
                        });

                    });

                    path("/urls", () -> {

                        get(urlControlador::getAll);
                        post(urlControlador::create);
                        put(urlControlador::update);
                        get("/{url-id}",urlControlador::getOne);
                        delete("/{shortcode}",urlControlador::delete);
                        get("detalles/{urlAcortada}", urlControlador::getDetallesUrl);
                    });

                    path("/clients", () -> {
                        get(clienteControlador::getAll);
                        post(clienteControlador::create);
                        put(clienteControlador::update);
                        path("/{client-id}", () -> {
                            get(clienteControlador::getOne);
                            delete(clienteControlador::delete);
                        });
                    });
                });
            });




            config.registerPlugin(new OpenApiPlugin(openApiConfig ->
                    openApiConfig
                            .withDocumentationPath("/openapi")
                            .withRoles(RolesApp.NO_REGISTRADO)
                            .withDefinitionConfiguration((version, openApiDefinition) ->
                                    openApiDefinition
                                            .withInfo(openApiInfo ->
                                                    openApiInfo
                                                            .description("Api que se encarga de listar las URL publicadas por un usuario incluyendo las estadísticas asociadas\n" +
                                                                    "Creación de registro de URL para un usuario retornando la estructura básica (url\n" +
                                                                    "completa, url acortada, fecha creación, objeto de estadística y la imagen actual\n" +
                                                                    "del sitio (vista previa) en base64")
                                                            .termsOfService("https://icc352.edu.pucmm.com.do/tos")
                                                            .contact("API Support", "https://icc352.pucmm.edu.do/soporte", "support@example.com")
                                                            .license("Apache 2.0", "https://www.apache.org/licenses/", "Apache-2.0")
                                            )
                                            .withServer(openApiServer ->
                                                    openApiServer
                                                            .description("Informacion sobre servidor")
                                                            .url("http://localhost:{port}/")
                                                            //.url("http://localhost:{port}{basePath}/" + version + "/")
                                                            .variable("port", "Puertos servidor", "7001", "7000", "7070")
                                                            //.variable("basePath", "Base path of the server", "", "", "v1")
                                            )
                                            // Based on official example: https://swagger.io/docs/specification/authentication/oauth2/
                                            .withSecurity(openApiSecurity ->
                                                    openApiSecurity
                                                            .withBasicAuth()
                                                            .withBearerAuth()
                                                            .withApiKeyAuth("ApiKeyAuth", "X-Api-Key")
                                                            .withCookieAuth("CookieAuth", "JSESSIONID")
                                                            .withOpenID("OpenID", "https://example.com/.well-known/openid-configuration")
                                                            .withOAuth2("OAuth2", "This API uses OAuth 2 with the implicit grant flow.", oauth2 ->
                                                                    oauth2
                                                                            .withClientCredentials("https://api.example.com/credentials/authorize")
                                                                            .withImplicitFlow("https://api.example.com/oauth2/authorize", flow ->
                                                                                    flow
                                                                                            .withScope("read_pets", "read your pets")
                                                                                            .withScope("write_pets", "modify pets in your account")
                                                                            )
                                                            )
                                                            .withGlobalSecurity("OAuth2", globalSecurity ->
                                                                    globalSecurity
                                                                            .withScope("write_pets")
                                                                            .withScope("read_pets")
                                                            )
                                            )
                                            .withDefinitionProcessor(content -> { // you can add whatever you want to this document using your favourite json api
                                                content.set("test", new TextNode("Value"));
                                                return content.toPrettyString();
                                            })
                            )));

            config.registerPlugin(new SwaggerPlugin(swaggerConfiguration -> {
                //swaggerConfiguration.setDocumentationPath(deprecatedDocsPath);
            }));

            config.registerPlugin(new ReDocPlugin(reDocConfiguration -> {
                //reDocConfiguration.setDocumentationPath(deprecatedDocsPath);
            }));

            for (JsonSchemaResource generatedJsonSchema : new JsonSchemaLoader().loadGeneratedSchemes()) {
                System.out.println(generatedJsonSchema.getName());
                System.out.println(generatedJsonSchema.getContentAsString());
            }

        }).start(getHerokuAssignedPort());

        app.before("/api/urls/detalles/{urlAcortada}", ctx -> {
            String header = ctx.header("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new UnauthorizedResponse("Se requiere autenticación JWT");
            }

            String token = header.replace("Bearer ", "").trim();

            try {
                Claims claims = JwtUtil.verificarToken(token);
                String rol = claims.get("role", String.class);
                String username = claims.get("username", String.class);

                ctx.attribute("rol", rol);

                if ("NO_REGISTRADO".equals(rol)) {
                    ctx.attribute("sessionId", username); // usamos "username" como sessionId temporal
                } else {
                    ctx.attribute("usuario", username);
                }

            } catch (JwtException e) {
                throw new ForbiddenResponse("Token inválido o expirado: " + e.getMessage());
            }
        });
        app.before("/api/lista", ctx -> {
            String header = ctx.header("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                throw new UnauthorizedResponse("Se requiere autenticación JWT");
            }

            String token = header.replace("Bearer ", "").trim();

            try {
                Claims claims = JwtUtil.verificarToken(token);
                String rol = claims.get("role", String.class);
                String username = claims.get("username", String.class);

                ctx.attribute("rol", rol);

                if ("NO_REGISTRADO".equals(rol)) {
                    ctx.attribute("sessionId", username); // usamos "username" como sessionId temporal
                } else {
                    ctx.attribute("usuario", username);
                }

            } catch (JwtException e) {
                throw new ForbiddenResponse("Token inválido o expirado: " + e.getMessage());
            }
        });

        //FrontEnd
        new CrudControlador(app).aplicarRutas();
        new AppControlador(app).aplicarRutas();
        new LoginControlador(app).aplicarRutas();


        // ------------------------
        //         G R P C
        //        INICIO
        // ------------------------

        //Puerto del servidor.
        int port = 50051;

        //Inicializando el servidor
        Server server = ServerBuilder.forPort(port)
                .addService(new UrlServicesGrpc())// indicando el servicio registrado.
                .addService(ProtoReflectionService.newInstance())
                .build()
                .start();
        System.out.println("Servidor gRPC iniciando y escuchando en " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Cerrando servidor por la JVM ");
            if (server != null) {
                server.shutdown();
            }
            System.err.println("Servidor abajo!...");
        }));
        server.awaitTermination();

        // ------------------------
        //         G R P C
        //        F I N
        // ------------------------

    }

  
    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7001; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

}