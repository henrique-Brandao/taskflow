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
import java.io.InputStream;
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
        return NimbusJwtDecoder.withPublicKey(loadPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws IOException {
        JWK jwk = new RSAKey.Builder(loadPublicKey()).privateKey(loadPrivateKey()).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    private RSAPublicKey loadPublicKey() throws IOException {
        return RsaKeyConverters.x509().convert(openKey(publicKeyResource, publicKeyBase64));
    }

    private RSAPrivateKey loadPrivateKey() throws IOException {
        return RsaKeyConverters.pkcs8().convert(openKey(privateKeyResource, privateKeyBase64));
    }

    private InputStream openKey(Resource keyResource, String keyBase64) throws IOException {
        if (!keyBase64.isBlank()) {
            return new ByteArrayInputStream(Base64.getMimeDecoder().decode(keyBase64));
        }

        return keyResource.getInputStream();
    }
}
