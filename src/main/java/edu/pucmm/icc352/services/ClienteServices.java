package edu.pucmm.icc352.services;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import edu.pucmm.icc352.modelo.Cliente;
import edu.pucmm.icc352.util.db.MongoDbConexion;
import org.bson.types.ObjectId;


import java.util.List;

public class ClienteServices {
    private Datastore datastore = MongoDbConexion.getInstance().getDatastore();
    private static ClienteServices instancia;

    public static ClienteServices getInstancia(){
        if(instancia==null){
            instancia = new ClienteServices();
        }
        return instancia;
    }

    // CRUD
    public List<Cliente> getAll() {
        return datastore.find(Cliente.class).iterator().toList();
    }

    public Cliente getById(ObjectId id) {
        return datastore.find(Cliente.class).filter(Filters.eq("_id", id)).first();
    }

    public Cliente create(Cliente cliente) {

        // DEBUG
        System.out.println("====== INFORMACION URL [DEBUG] ======");
        System.out.println("ObjectId: " + cliente.getIdCliente());
        System.out.println("Cliente IP: " + cliente.getIpCliente());
        System.out.println("Fecha Acceso: " + cliente.getFechaAcceso());
        System.out.println("Navegador: " + cliente.getNavegador());
        System.out.println("Sistema Operativo: " + cliente.getSistemaOperativo());
        System.out.println("=====================================");
        datastore.save(cliente);
        return cliente;
    }

    public Cliente update(ObjectId id, Cliente clienteActualizado) {
        Cliente existente = getById(id);
        if (existente != null) {
            clienteActualizado.setIdCliente(id); // mantener el ID
            datastore.save(clienteActualizado);
            return clienteActualizado;
        }
        return null;
    }

    public boolean delete(ObjectId id) {
        Cliente cliente = getById(id);
        if (cliente != null) {
            datastore.delete(cliente);
            return true;
        }
        return false;
    }
}
