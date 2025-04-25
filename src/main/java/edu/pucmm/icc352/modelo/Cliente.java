package edu.pucmm.icc352.modelo;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cliente {
    @Id
    private ObjectId idCliente;
    private String navegador;
    private String ipCliente;
    private String sistemaOperativo;
    private Date fechaAcceso;

    public Cliente(String navegador, String ipCliente, String sistemaOperativo, Date fechaAcceso) {
        this.navegador = navegador;
        this.ipCliente = ipCliente;
        this.sistemaOperativo = sistemaOperativo;
        this.fechaAcceso = fechaAcceso;
    }


}
