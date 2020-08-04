package pl.zajac.model.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.jupiter.api.Assertions.*;

public class ReadTokenTest {
    private ReadToken readToken = new ReadToken();
    private final String TOKEN_PREFIX = "Bearer ";

    @Test(expected = ExpiredJwtException.class)
    public void readTokenTestFailExpiredToken(){
        //when
        String userName = readToken.getLogin(TOKEN_PREFIX + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiIsInJvbGVzIjoiYWRtaW4iLCJsb2dpbiI6ImFkbWluIiwiaWF0IjoxNTk2NDY2Nzc0LCJleHAiOjE1OTY0Njc3NzR9.DGUm5td89saz-dypw916UG5VeG4yAjHmIUJKWsLv1_A");
    }
    @Test
    public void readTokenTestSuccess(){
        //given
        JwtGenerate jwtGenerate = new JwtGenerate();
        String token = jwtGenerate.generateToken("admin","test");
        //when
        String userName = readToken.getLogin(TOKEN_PREFIX + token);
        //then
        assertEquals("admin", userName);
    }
}
