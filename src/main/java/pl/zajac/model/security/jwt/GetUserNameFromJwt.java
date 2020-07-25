package pl.zajac.model.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import pl.zajac.model.security.configuration.JwtConfig;

public class GetUserNameFromJwt {
    public static String getUserName(String token){
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(JwtConfig.getSecret())
                .parseClaimsJws(token.substring(7))
                .getBody();
        return claims.get("roles").toString();
    }
}
