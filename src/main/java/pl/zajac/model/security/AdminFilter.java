package pl.zajac.model.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import pl.zajac.model.security.configuration.JwtConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Claims claims = null;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String header = httpServletRequest.getHeader("authorization");
        try{
            claims = Jwts.parser()
                    .setSigningKey(JwtConfig.getSecret())
                    .parseClaimsJws(header.substring(7))
                    .getBody();
            if(!claims.get("roles").equals("admin")){
                throw new Exception("User is not a admin");
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }
        catch (Exception e){
            e.printStackTrace();
            httpServletResponse.setStatus(401);
        }
    }
}
