package pl.zajac.model.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import pl.zajac.model.security.configuration.JwtConfig;

@Component
public class ReadToken {
    public String getLogin(String token){
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(JwtConfig.getSecret())
                .parseClaimsJws(token.substring(7))
                .getBody();
        return claims.get("login").toString();
    }

    public String getEmailFromResetToken(String token){
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(JwtConfig.getSecretResetPassword())
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email").toString();
    }
}
