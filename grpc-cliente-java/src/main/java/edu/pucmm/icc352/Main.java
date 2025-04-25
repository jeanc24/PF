package edu.pucmm.icc352;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import urlrn.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        UrlRnGrpc.UrlRnBlockingStub urlStub = UrlRnGrpc.newBlockingStub(channel);

        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        int opcion;

        while (!salir) {
            System.out.println("\n========= Cliente gRPC - URL SHORTENER =========");
            System.out.println("1. Listar URLs");
            System.out.println("2. Consultar URL por ID");
            System.out.println("3. Crear nueva URL");
            System.out.println("4. Actualizar URL");
            System.out.println("5. Eliminar URL");
            System.out.println("6. Salir");

            try {
                System.out.println("Escribe una de las opciones:");
                opcion = sn.nextInt();
                sn.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        UrlRnOuterClass.ListaUrl lista = urlStub.getAll(UrlRnOuterClass.Empty.newBuilder().build());
                        lista.getUrlsList().forEach(Main::imprimirUrl);
                        break;
                    case 2:
                        System.out.print("ID de la URL: ");
                        String id = sn.nextLine();
                        UrlRnOuterClass.UrlResponse respuesta = urlStub.getUrl(
                                UrlRnOuterClass.UrlRequest.newBuilder().setIdUrl(id).build()
                        );
                        imprimirUrl(respuesta);
                        break;
                    case 3:
                        System.out.print("URL original: ");
                        //sn.nextLine(); // Limpiar buffer
                        String urlOriginal = sn.nextLine();
                        System.out.println(urlOriginal);
                        UrlRnOuterClass.UrlCreateRequest createRequest = UrlRnOuterClass.UrlCreateRequest.newBuilder()
                                .setUrlOriginal(urlOriginal)
                                .build();
                        UrlRnOuterClass.UrlResponse creada = urlStub.createUrl(createRequest);
                        System.out.println("✅ URL creada:");
                        imprimirUrl(creada);
                        break;

                    case 4:
                        System.out.print("ID de la URL a actualizar: ");
                        //sn.nextLine(); // Limpiar buffer
                        String idEdit = sn.nextLine();
                        System.out.print("Nueva URL original: ");
                        String nuevaOriginal = sn.nextLine();
                        UrlRnOuterClass.UrlUpdateRequest updateRequest = UrlRnOuterClass.UrlUpdateRequest.newBuilder()
                                .setIdUrl(idEdit)
                                .setUrlOriginal(nuevaOriginal)
                                .build();
                        UrlRnOuterClass.UrlResponse actualizada = urlStub.updateUrl(updateRequest);
                        System.out.println("✅ URL actualizada:");
                        imprimirUrl(actualizada);
                        break;

                    case 5:
                        System.out.print("ID de la URL a eliminar: ");
                       // sn.nextLine(); // limpiar buffer
                        String idDelete = sn.nextLine();
                        try {
                            UrlRnOuterClass.UrlBorrado borrado = urlStub.deleteUrl(
                                    UrlRnOuterClass.UrlBorrado.newBuilder().setIdUrl(idDelete).build()
                            );
                            System.out.println("🗑️ URL eliminada: " + borrado.getIdUrl());
                        } catch (Exception e) {
                            System.err.println("❌ Error al eliminar la URL: " + e.getMessage());
                        }
                        break;
                    case 6:
                        salir = true;
                        channel.shutdown();
                        System.out.println("👋 Finalizado.");
                        break;
                    default:
                        System.out.println("❌ Opción inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Entrada inválida. Usa un número.");
                sn.next();
            } catch (Exception ex) {
                System.out.println("⚠️ Error: " + ex.getMessage());
            }
        }
    }

    private static void imprimirUrl(UrlRnOuterClass.UrlResponse url) {
        System.out.println("🧾 ============================");
        System.out.println("🔗 ID: " + url.getIdUrl());
        System.out.println("🌐 Original: " + url.getUrlOriginal());
        System.out.println("🪄 Acortada: " + url.getUrlAcortada());
        System.out.println("📈 Accesos: " + url.getCantidadAccesos());
        System.out.println("🕒 Creación: " + url.getFechaCreacion());
        System.out.println("🖼️ Imagen (base64): " + (url.getImagenBase64().isEmpty() ? "(vacía)" : "sí"));
        System.out.println("================================");
    }
}
