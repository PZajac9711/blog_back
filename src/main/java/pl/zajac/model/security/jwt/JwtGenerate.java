package pl.zajac.model.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import pl.zajac.model.security.configuration.JwtConfig;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerate {
    public String generateToken(String userName,String role) {
        long currentTime = System.currentTimeMillis();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JwtConfig.getSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject("Login")
                .claim("roles",role)
                .claim("login",userName)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + JwtConfig.getExpirationTime()))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }
    public String generateForgotPasswordToken(String email){
        long currentTime = System.currentTimeMillis();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JwtConfig.getSecretResetPassword());
        Key signinKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject("token")
                .claim("email",email)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + JwtConfig.getExpirationTimeResetPassword()))
                .signWith(signatureAlgorithm, signinKey)
                .compact();
    }
}
