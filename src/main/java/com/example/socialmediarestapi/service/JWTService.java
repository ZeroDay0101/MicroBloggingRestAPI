package com.example.socialmediarestapi.service;

import com.example.socialmediarestapi.security.authentication.jwt.TokenType;
import com.example.socialmediarestapi.utills.RsaKeyUtills;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JWTService {
    private final long acessTokenExpiration = TimeUnit.MINUTES.toMillis(30);
    private final long refreshTokenExpiration = TimeUnit.DAYS.toMillis(7);

    public String extractUsername(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, TokenType tokenType) throws Exception {
        return generateToken(new HashMap<>(), userDetails, tokenType);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, TokenType tokenType) throws Exception {
        return buildToken(extraClaims, userDetails, tokenType == TokenType.ACCESS_TOKEN ? acessTokenExpiration : refreshTokenExpiration);
    }

    public long getExpirationTime() {
        return acessTokenExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) throws Exception {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getPrivateKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return extractClaim(token, Claims::getExpiration);
    }

    public long extractUserId(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Claims claims = extractAllClaims(token);
        Object userId = claims.get("userId");

        if (!(userId instanceof Long))
            return ((Integer) userId).longValue();
        else
            return ((Long) userId);
    }

    public Claims extractAllClaims(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return Jwts.
                parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public void checkJWTValidity(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        NimbusJwtDecoder
                .withPublicKey((RSAPublicKey) getPublicKey())
                .build().decode(token);
    }

    private PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return RsaKeyUtills.readX509PublicKey(new File("src/main/resources/encryptionKeys/jwtPublicKey.key"));
    }

    private PrivateKey getPrivateKey() throws Exception {
        return RsaKeyUtills.readPKCS8PrivateKey(new File("src/main/resources/encryptionKeys/jwtPrivateKey.key"));

    }
}
