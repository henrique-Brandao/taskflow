package com.henrique.taskflow.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Value("${jwt.public.key}")
    private Resource publicKeyResource;
    @Value("${jwt.private.key}")
    private Resource privateKeyResource;
    @Value("${JWT_PUBLIC_KEY_BASE64:}")
    private String publicKeyBase64;
    @Value("${JWT_PRIVATE_KEY_BASE64:}")
    private String privateKeyBase64;

    @Bean
    public JwtDecoder jwtDecoder() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey publicKey = getPublicKey();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        JWK jwk = new RSAKey.Builder(getPublicKey()).privateKey(getPrivateKey()).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    private RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (!publicKeyBase64.isBlank()) {
            byte[] keyBytes = decodeBase64(publicKeyBase64);

            if (isPem(keyBytes)) {
                return RsaKeyConverters.x509().convert(new ByteArrayInputStream(keyBytes));
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        }

        return RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
    }

    private RSAPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (!privateKeyBase64.isBlank()) {
            byte[] keyBytes = decodeBase64(privateKeyBase64);

            if (isPem(keyBytes)) {
                return RsaKeyConverters.pkcs8().convert(new ByteArrayInputStream(keyBytes));
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        }

        return RsaKeyConverters.pkcs8().convert(privateKeyResource.getInputStream());
    }

    private byte[] decodeBase64(String value) {
        return Base64.getMimeDecoder().decode(value);
    }

    private boolean isPem(byte[] keyBytes) {
        return new String(keyBytes, StandardCharsets.UTF_8).contains("-----BEGIN");
    }
}
