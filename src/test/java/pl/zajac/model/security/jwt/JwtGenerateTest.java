package pl.zajac.model.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.zajac.model.security.configuration.JwtConfig;

import static org.junit.jupiter.api.Assertions.*;

public class JwtGenerateTest {
    private JwtGenerate jwtGenerate = new JwtGenerate();

    @ParameterizedTest
    @CsvSource({
            "admin, admin",
            "user, user",
            "adas, user",
            "eugienia123, user",
            "gieniek997, admin"
    })
    public void shouldGenerateProperTokensForUsers(String userName, String role){
        //when
        String token = jwtGenerate.generateToken(userName, role);
        //then
        Claims claims;
        claims = Jwts.parser()
                .setSigningKey(JwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();
        System.out.println(token);
        assertEquals(userName, claims.get("login").toString());
        assertEquals(role, claims.get("roles").toString());
    }
}
