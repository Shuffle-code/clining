package ru.shuffle.clining.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.shuffle.clining.security.jwt.JwtConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtConfigurer jwtConfigurer;
    @Autowired
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
//                .antMatchers(HttpMethod.POST,"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
////                .antMatchers(HttpMethod.POST, "/api/v1/auth/login/").permitAll()
//                .antMatchers(HttpMethod.GET, "/janitor/images/*").permitAll()
//                .antMatchers(HttpMethod.POST, "/janitor/add").hasAuthority("janitor.create")
////                .antMatchers(HttpMethod.POST, "/api/v1/janitor/all").hasAuthority("janitor.read")
//                .antMatchers(HttpMethod.GET, "/janitor/all").hasAuthority("janitor.read")
////                .requestMatchers(HttpMethod.GET, "/upcomingTournaments/enroll/{{playerId}}/{{tournamentId}}").hasRole("USER")
////                .requestMatchers(HttpMethod.PUT, "/upcomingTournaments/disenroll/{playerId}/{tournamentId}").permitAll()
//                .antMatchers(HttpMethod.GET, "/auth/registration").permitAll()
//                .antMatchers(HttpMethod.POST, "/auth/register").permitAll()
//                .antMatchers(HttpMethod.POST, "/auth/invalid-confirmation").permitAll()
//                .antMatchers(HttpMethod.POST, "/auth/changeUsername").permitAll()
//                .antMatchers(HttpMethod.POST, "/auth/confirmation").permitAll()
////                .requestMatchers(HttpMethod.POST, "http://localhost:8093/swagger-ui/index.html").anonymous()
//                .antMatchers(HttpMethod.GET, "/*").authenticated()
//                .antMatchers(HttpMethod.POST, "/*").authenticated()
                .and().csrf().disable();
        http.exceptionHandling().accessDeniedPage("/access-denied");
        http.apply(jwtConfigurer);
        http.formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll();
        http.logout()
                .logoutSuccessUrl("/janitor/all")
                .permitAll();
        http.httpBasic();
        return http.build();
}





}

