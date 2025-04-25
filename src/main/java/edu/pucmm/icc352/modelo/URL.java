package edu.pucmm.icc352.modelo;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity("urls")
public class URL {
    @Id
    private ObjectId idUrl;
    private String urlOriginal;
    private String urlAcortada;
    private int cantidadAccesos;
    @Reference
    private ArrayList<Cliente> listaClientes;
    private String imagenBase64;
    private Date fechaCreacion;

    public URL(String urlOriginal, String urlAcortada, String imagenBase64, Date fechaCreacion) {
        this.urlOriginal = urlOriginal;
        this.urlAcortada = urlAcortada;
        this.imagenBase64 = imagenBase64;
        this.fechaCreacion = fechaCreacion;
    }
}
