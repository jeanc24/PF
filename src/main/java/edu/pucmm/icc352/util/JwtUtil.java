package edu.pucmm.icc352.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "a-string-secret-at-least-256-bits-long";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generar token JWT
    public static String generarToken(String idUsuario, String username, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 15); // 15 minutos

        return Jwts.builder()
                .setSubject(idUsuario)
                .claim("username", username)
                .claim("idUsuario", idUsuario)
                .claim("role", rol)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();

    }

    // Verificar y obtener claims
    public static Claims verificarToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Devuelve true si el token es válido
    public static boolean validarToken(String token) {
        try {
            verificarToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    // Extrae el username del token (subject)
    public static String extractUsername(String token) {
        return verificarToken(token).getSubject();
    }


    // Devuelve los Claims si el token es válido, o null si no
    public static Claims decodeJWT(String token) {
        try {
            return verificarToken(token);
        } catch (JwtException e) {
            return null;
        }
    }

}
