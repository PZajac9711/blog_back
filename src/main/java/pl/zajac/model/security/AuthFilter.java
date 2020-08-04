package pl.zajac.model.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import pl.zajac.model.security.configuration.JwtConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String header = httpServletRequest.getHeader("authorization");
        try{
            if(header == null){
                throw new ServletException("No auth field token is empty");
            }
            if(!header.startsWith("Bearer ")) {
                throw new ServletException("Token didnt start with Bearer ");
            }
            else{
                String token = header.substring(7);
                try{
                    Claims claims = Jwts.parser()
                            .setSigningKey(JwtConfig.getSecret())
                            .parseClaimsJws(token)
                            .getBody();
                    servletRequest.setAttribute("claims",claims);
                    filterChain.doFilter(servletRequest,servletResponse);
                }
                catch (Exception e){
                    logger.error(e.getMessage());
                    httpServletResponse.setStatus(401);
                }
            }
        }
        catch (NullPointerException e){
            logger.error(e.getMessage());
            httpServletResponse.setStatus(400);
        }
    }
}
