package edu.pucmm.icc352.util;

public class Base62 {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // Codificar un número largo en base62
    public static String encode(long num) {
        StringBuilder encoded = new StringBuilder();
        while (num > 0) {
            encoded.append(ALPHABET.charAt((int) (num % 62)));
            num = num / 62;
        }
        return encoded.reverse().toString();
    }

    // Decodificar una cadena base62 a un número largo
    public static long decode(String str) {
        long decoded = 0;
        for (int i = 0; i < str.length(); i++) {
            decoded = decoded * 62 + ALPHABET.indexOf(str.charAt(i));
        }
        return decoded;
    }
}
