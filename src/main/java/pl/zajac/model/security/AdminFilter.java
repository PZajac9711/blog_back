package pl.zajac.model.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.zajac.model.security.configuration.JwtConfig;

import javax.security.auth.message.AuthException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(AdminFilter.class);
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
                throw new AuthException("user is not a admin");
            }
            filterChain.doFilter(servletRequest,servletResponse);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            httpServletResponse.setStatus(401);
        }
    }
}
