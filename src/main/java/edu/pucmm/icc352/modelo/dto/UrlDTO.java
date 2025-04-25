package edu.pucmm.icc352.modelo.dto;

import edu.pucmm.icc352.modelo.Cliente;
import edu.pucmm.icc352.modelo.URL;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UrlDTO {
    private String urlOriginal;
    private String urlAcortada;
    private int cantidadAccesos;
    private String imagenBase64;
    private List<Cliente> listaClientes;

    public UrlDTO(URL url) {
        this.urlOriginal = url.getUrlOriginal();
        this.urlAcortada = url.getUrlAcortada();
        this.cantidadAccesos = url.getCantidadAccesos();
        this.listaClientes = url.getListaClientes();
        if (url.getImagenBase64() != null) {
            this.imagenBase64 = "data:image/jpeg;base64," + url.getImagenBase64();
        }
    }
}
