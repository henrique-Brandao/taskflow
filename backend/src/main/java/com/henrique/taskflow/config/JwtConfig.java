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
    public JwtDecoder jwtDecoder() throws IOException {
        RSAPublicKey publicKey = getPublicKey();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws IOException {
        JWK jwk = new RSAKey.Builder(getPublicKey()).privateKey(getPrivateKey()).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    private RSAPublicKey getPublicKey() throws IOException {
        if (!publicKeyBase64.isBlank()) {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
            return RsaKeyConverters.x509().convert(new ByteArrayInputStream(keyBytes));
        }

        return RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
    }

    private RSAPrivateKey getPrivateKey() throws IOException {
        if (!privateKeyBase64.isBlank()) {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
            return RsaKeyConverters.pkcs8().convert(new ByteArrayInputStream(keyBytes));
        }

        return RsaKeyConverters.pkcs8().convert(privateKeyResource.getInputStream());
    }
}
