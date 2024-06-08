package com.example.socialmediarestapi.utills;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaKeyUtills {
    public static RSAPublicKey readX509PublicKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyString = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        // Remove the "BEGIN" and "END" lines and any whitespace
        String publicKeyPEM = publicKeyString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        // Decode the Base64 encoded key
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        // Create a key specification
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        // Initialize the KeyFactory
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Generate the public key
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return (RSAPublicKey) publicKey;
    }

    public static RSAPrivateKey readPKCS8PrivateKey(File file) throws Exception {
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        String privateKeyPEM = key
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
