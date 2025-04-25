package edu.pucmm.icc352.services;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import edu.pucmm.icc352.modelo.URL;
import edu.pucmm.icc352.util.Base62;
import edu.pucmm.icc352.util.MicrolinkUtil;
import edu.pucmm.icc352.util.db.MongoDbConexion;
import org.bson.types.ObjectId;


import java.util.Date;
import java.util.List;

public class UrlServices {
    private Datastore datastore = MongoDbConexion.getInstance().getDatastore();
    private static UrlServices instancia;

    public static UrlServices getInstancia(){
        if(instancia==null){
            instancia = new UrlServices();
        }
        return instancia;
    }

    // CRUD
    public List<URL> getAll() {
        return datastore.find(URL.class).iterator().toList();
    }

    public URL getById(ObjectId id) {
        return datastore.find(URL.class).filter(Filters.eq("_id", id)).first();
    }

    public URL create(URL url) {
        url.setIdUrl(new ObjectId());
        String urlAcortada = generarUrlAcortada(url.getIdUrl());

        url.setUrlAcortada(urlAcortada);  // Asignamos la URL acortada a la URL
        url.setCantidadAccesos(0);
        url.setImagenBase64(null);
        url.setFechaCreacion(new Date());

        // Obtener preview en segundo plano
        new Thread(() -> {
            String imagenBase64 = MicrolinkUtil.obtenerPreviewBase64(url.getUrlOriginal());
            url.setImagenBase64(imagenBase64);
            datastore.save(url); // Actualizamos el registro
        }).start();

        // DEBUG
        System.out.println("====== INFORMACION URL [DEBUG] ======");
        System.out.println("ObjectId: " + url.getIdUrl());
        System.out.println("Url Original: " + url.getUrlOriginal());
        System.out.println("Url Acortada: " + url.getUrlAcortada());
        System.out.println("Cantidad Accesos: " + url.getCantidadAccesos());
        System.out.println("Lista clientes: " + url.getListaClientes());
        System.out.println("=====================================");

        // Guardamos la URL en la base de datos
        datastore.save(url);
        return url;
    }


    public URL update(ObjectId id, URL urlActualizada) {
        URL existente = getById(id);
        if (existente != null) {
            urlActualizada.setIdUrl(id); // mantener el ID
            datastore.save(urlActualizada);
            return urlActualizada;
        }
        return null;
    }

    public boolean delete(ObjectId id) {
        URL url = getById(id);
        if (url != null) {
            datastore.delete(url);
            return true;
        }
        return false;
    }

    public boolean deleteByShortcode(String shortcode) {
        URL url = findByShortCode(shortcode);

        if (url != null) {
            datastore.delete(url);
            return true;
        }
        return false;
    }

    public String generarUrlAcortada(ObjectId objectId) {
        long idLong = objectId.getTimestamp();  // Usamos el timestamp del ObjectId para generar una base62 única
        return "http://pf-production.up.railway.app/api/cut/" + Base62.encode(idLong);
    }

    public URL findByShortCode(String shortCode) {
        System.out.println("aqui en findByShortCode");
        return datastore.find(URL.class)
                .filter(Filters.eq("urlAcortada", shortCode))
                .first();
    }

}
