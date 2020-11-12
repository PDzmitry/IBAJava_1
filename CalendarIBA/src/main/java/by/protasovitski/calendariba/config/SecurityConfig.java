package by.protasovitski.calendariba.config;

import by.protasovitski.calendariba.security.jwt.JwtConfigurer;
import by.protasovitski.calendariba.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/api/v1";
    private static final String MANAGER_ENDPOINT= "api/v1";
    private static final String VIEW_ENDPOINT= "/api/v1";
    private static final String USER_ENDPOINT = "/api/v1/report";
    private static final String GROUP_ENDPOINT = "/api/v1/report";
    private static final String [] LOGIN_ENDPOINT = {"/assets/*" , "/*", "/api/v1/auth/login"};
    private static final String [] DOCUMENTATION = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };


    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic().disable()
                .csrf().disable()
//                .httpBasic()
//                .and()
//                .csrf()
//                .ignoringAntMatchers(LOGIN_ENDPOINT)
//                .ignoringAntMatchers(DOCUMENTATION)
//                .and()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .anyRequest().permitAll()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(DOCUMENTATION).permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .antMatchers(MANAGER_ENDPOINT).hasRole("MANAGER")
                .antMatchers(USER_ENDPOINT).hasRole("USER")
                .antMatchers(VIEW_ENDPOINT).hasRole("VIEW")
                .antMatchers(GROUP_ENDPOINT).hasRole("GROUP")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

}
