package edu.pucmm.icc352.util.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import edu.pucmm.icc352.modelo.Usuario;
import edu.pucmm.icc352.services.UrlServices;
import edu.pucmm.icc352.services.UsuarioServices;
import org.bson.Document;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import java.util.concurrent.TimeUnit;

public class MongoDbConexion {
    private static MongoDbConexion instance;
    private MongoClient mongoClient;
    private Datastore datastore;
    private String DB_NOMBRE;

    private MongoDbConexion(){
        getBaseDatos();
    }

    public static MongoDbConexion getInstance(){
        if(instance == null){
            instance = new MongoDbConexion();
        }
        return instance;
    }

    /**
     *
     * @return
     */
    public void getBaseDatos(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        String URL_MONGODB = processBuilder.environment().get("MONGODB_URL");
        DB_NOMBRE = processBuilder.environment().get("DB_NOMBRE");
        mongoClient = MongoClients.create(URL_MONGODB);


        datastore = Morphia.createDatastore(mongoClient, DB_NOMBRE); //datastore.save()
        datastore.getMapper().mapPackage("edu.pucmm.icc352.modelo"); // Escanea las clases con @Entity
        datastore.ensureIndexes(); // Crea índices si es necesario

        MongoDatabase database = mongoClient.getDatabase(DB_NOMBRE);
        MongoCollection<Document> urlCollection = database.getCollection("urls");

        System.out.println("✅ Conectado a MongoDB con Morphia");
    }

    public void inicializarDatos() {
        UsuarioServices.getInstancia().crearUsuarioAdministrador();
    }

    /**
     * Obtiene el Datastore para interactuar con la base de datos.
     */
    public Datastore getDatastore() {
        return datastore;
    }

    /**
     * Cierra la conexión con MongoDB.
     */
    public void cerrar() {
        if (datastore != null) {
            mongoClient.close();
            System.out.println("🔴 Conexión con MongoDB cerrada");
        }
    }
}
