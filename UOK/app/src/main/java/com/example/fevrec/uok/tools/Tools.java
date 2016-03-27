package com.example.fevrec.uok.tools;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Created by badetitou on 27/03/16.
 */
public class Tools {
    public static String toBase64(String s){
        final byte[] authBytes = s.getBytes(StandardCharsets.UTF_8);
        final String encoded = Base64.encodeToString(authBytes, Base64.DEFAULT);
        return encoded;
    }
}
