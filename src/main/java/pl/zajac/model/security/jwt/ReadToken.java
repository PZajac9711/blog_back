package pl.zajac.model.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import pl.zajac.model.security.configuration.JwtConfig;

public class ReadToken {
    public static String getRole(String token){
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(JwtConfig.getSecret())
                .parseClaimsJws(token.substring(7))
                .getBody();
        return claims.get("roles").toString();
    }
    public static String getLogin(String token){
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(JwtConfig.getSecret())
                .parseClaimsJws(token.substring(7))
                .getBody();
        return claims.get("login").toString();
    }
}
