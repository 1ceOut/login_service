package com.icebuckwheat.oauthserver.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class jwt {

    private String secret;

    private long expiration;

    private long expiration_refresh;

    private String header;

    private String prefix;

    public String MakeAccessJwtToken(String id, String role, String name, String photo) {
        Date now = new Date();

        return Jwts.builder()
                .subject(id)
                .claim("name", name)
                .claim("role", role)
                .claim("photo", photo)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String MakeRefreshJwtToken(String id, String role, String name) {
        Date now = new Date();

        return Jwts.builder()
                .subject(id)
                .claim("name", name)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiration_refresh))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isJwtValid(String token) {
        try {
            Claims claims = getClaims(token);
            if (claims == null) {
                return false;
            }

            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            Date now = new Date();

            return subject != null && expiration != null && now.before(expiration);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        String subject = claims.getSubject();
        return subject;
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
