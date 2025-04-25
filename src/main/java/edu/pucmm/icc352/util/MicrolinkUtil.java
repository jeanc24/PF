package edu.pucmm.icc352.util;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.InputStream;
import java.net.URL;

public class MicrolinkUtil {

    public static String obtenerPreviewBase64(String urlOriginal) {
        try {
            String apiUrl = "https://api.microlink.io?url=" + URLEncoder.encode(urlOriginal, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            // Verificar si la respuesta es exitosa y contiene el objeto data.image
            if (json.has("status") && json.get("status").getAsString().equals("success")) {
                JsonObject data = json.getAsJsonObject("data");
                if (data.has("image")) {
                    JsonObject image = data.getAsJsonObject("image");
                    if (image.has("url")) {
                        String imageUrl = image.get("url").getAsString();
                        System.out.println("IMAGE URL: " + imageUrl);

                        // Descargar la imagen y convertirla a base64
                        try (InputStream in = new URL(imageUrl).openStream()) {
                            byte[] imageBytes = in.readAllBytes();
                            return Base64.getEncoder().encodeToString(imageBytes);
                        }
                    }
                }
            }

            System.out.println("No se encontró la URL de la imagen en la respuesta.");
            return null;

        } catch (Exception e) {
            System.out.println("Error obteniendo imagen desde Microlink: " + e.getMessage());
            return null;
        }
    }

}
