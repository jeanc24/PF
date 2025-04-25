package edu.pucmm.icc352.services.grpc;

import edu.pucmm.icc352.modelo.URL;
import edu.pucmm.icc352.modelo.Cliente;
import edu.pucmm.icc352.services.UrlServices;
import io.grpc.stub.StreamObserver;
import org.bson.types.ObjectId;
import urlrn.UrlRnGrpc;
import urlrn.UrlRnOuterClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UrlServicesGrpc extends UrlRnGrpc.UrlRnImplBase {

    private final UrlServices urlServices = new UrlServices();

    @Override
    public void getUrl(UrlRnOuterClass.UrlRequest request, StreamObserver<UrlRnOuterClass.UrlResponse> responseObserver) {
        ObjectId id = new ObjectId(request.getIdUrl());
        URL url = urlServices.getById(id);
        if (url != null) {
            responseObserver.onNext(convertir(url));
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new RuntimeException("URL no encontrada"));
        }
    }

    @Override
    public void createUrl(UrlRnOuterClass.UrlCreateRequest request, StreamObserver<UrlRnOuterClass.UrlResponse> responseObserver) {
        URL nueva = new URL();
        nueva.setUrlOriginal(request.getUrlOriginal());

        // Lógica del backend que genera URL acortada, fecha, etc.
        URL creada = urlServices.create(nueva);
        responseObserver.onNext(convertir(creada));
        responseObserver.onCompleted();
    }

    @Override
    public void updateUrl(UrlRnOuterClass.UrlUpdateRequest request, StreamObserver<UrlRnOuterClass.UrlResponse> responseObserver) {
        ObjectId id = new ObjectId(request.getIdUrl());

        URL existente = urlServices.getById(id);
        if (existente == null) {
            responseObserver.onError(new RuntimeException("URL no encontrada para actualizar"));
            return;
        }

        // Solo se actualiza el campo editable
        existente.setUrlOriginal(request.getUrlOriginal());
        URL actualizada = urlServices.update(id, existente);
        responseObserver.onNext(convertir(actualizada));
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUrl(UrlRnOuterClass.UrlBorrado borrado, StreamObserver<UrlRnOuterClass.UrlBorrado> responseObserver) {
        try {
            ObjectId id = new ObjectId(borrado.getIdUrl());
            URL existente = urlServices.getById(id);
            if (existente == null) {
                responseObserver.onError(new RuntimeException("URL no encontrada para eliminar"));
                return;
            }

            // Realizamos el borrado de forma explícita:
            urlServices.delete(id);

            responseObserver.onNext(UrlRnOuterClass.UrlBorrado.newBuilder()
                    .setIdUrl(id.toHexString())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace(); // Ver en la consola del servidor detalles del error
            responseObserver.onError(new RuntimeException("Error al eliminar la URL: " + e.toString()));
        }
    }





    @Override
    public void getAll(UrlRnOuterClass.Empty request, StreamObserver<UrlRnOuterClass.ListaUrl> responseObserver) {
        List<URL> urls = urlServices.getAll();
        List<UrlRnOuterClass.UrlResponse> grpcList = new ArrayList<>();
        for (URL u : urls) {
            grpcList.add(convertir(u));
        }
        responseObserver.onNext(UrlRnOuterClass.ListaUrl.newBuilder().addAllUrls(grpcList).build());
        responseObserver.onCompleted();
    }

    private UrlRnOuterClass.UrlResponse convertir(URL url) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        UrlRnOuterClass.UrlResponse.Builder builder = UrlRnOuterClass.UrlResponse.newBuilder()
                .setIdUrl(url.getIdUrl().toString())
                .setUrlOriginal(url.getUrlOriginal())
                .setUrlAcortada(url.getUrlAcortada())
                .setCantidadAccesos(url.getCantidadAccesos())
                .setImagenBase64(url.getImagenBase64() != null ? url.getImagenBase64() : "")
                .setFechaCreacion(url.getFechaCreacion() != null ? sdf.format(url.getFechaCreacion()) : "");

        if (url.getListaClientes() != null) {
            for (Cliente c : url.getListaClientes()) {
                builder.addListaClientes(UrlRnOuterClass.Cliente.newBuilder()
                        .setIdCliente(c.getIdCliente().toString())
                        .setNavegador(c.getNavegador())
                        .setIpCliente(c.getIpCliente())
                        .setSistemaOperativo(c.getSistemaOperativo())
                        .setFechaAcceso(sdf.format(c.getFechaAcceso()))
                        .build());
            }
        }

        return builder.build();
    }
}
