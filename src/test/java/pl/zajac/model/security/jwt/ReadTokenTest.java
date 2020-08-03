package pl.zajac.model.security.jwt;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.jupiter.api.Assertions.*;

public class ReadTokenTest {
    private ReadToken readToken = new ReadToken();
    private final String TOKEN_PREFIX = "Bearer ";
    @ParameterizedTest
    @CsvSource({
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiIsInJvbGVzIjoiYWRtaW4iLCJsb2dpbiI6ImFkbWluIiwiaWF0IjoxNTk2NDY2Nzc0LCJleHAiOjE1OTY0Njc3NzR9.DGUm5td89saz-dypw916UG5VeG4yAjHmIUJKWsLv1_A, admin",
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiIsInJvbGVzIjoidXNlciIsImxvZ2luIjoidXNlciIsImlhdCI6MTU5NjQ2Njc3NSwiZXhwIjoxNTk2NDY3Nzc1fQ.Wt4ouxzsD15e6ogPiXN3Cgrc2QQzQji6-TPvRltVEL0, user",
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiIsInJvbGVzIjoidXNlciIsImxvZ2luIjoiYWRhcyIsImlhdCI6MTU5NjQ2Njc3NSwiZXhwIjoxNTk2NDY3Nzc1fQ.HRd7djQ4e9FwMGmTDPuypRrLVJZjDKY8Rdo5fn7gB50, adas",
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiIsInJvbGVzIjoidXNlciIsImxvZ2luIjoiZXVnaWVuaWExMjMiLCJpYXQiOjE1OTY0NjY3NzUsImV4cCI6MTU5NjQ2Nzc3NX0.vd27JJFS2hNi6ZU0TSdhAAvxYNNnDxUm2i2sdqmUlMA, eugienia123"
    })
    public void readTokenTest(String token, String userNameFromToken){
        //when
        String userName = readToken.getLogin(TOKEN_PREFIX + token);
        //then
        assertEquals(userNameFromToken, userName);
    }
}
