package pl.zajac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import pl.zajac.model.security.AdminFilter;
import pl.zajac.model.security.AuthFilter;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    FilterRegistrationBean<AuthFilter> authFilterFilterRegistrationBean(){
        FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthFilter());
        filterRegistrationBean.addUrlPatterns(
                "/api/posts/asd"
        );
        return filterRegistrationBean;
    }
    @Bean
    FilterRegistrationBean<AdminFilter> adminFilterRegistrationBean(){
        FilterRegistrationBean<AdminFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AdminFilter());
        filterRegistrationBean.addUrlPatterns(
               "/api/posts/asd"
        );
        return filterRegistrationBean;
    }
}
