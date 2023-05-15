package ru.shuffle.clining.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
//    @Autowired
//    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers(HttpMethod.POST,"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//                .requestMatchers(HttpMethod.GET, "/janitor/images/*").permitAll()
//                .requestMatchers(HttpMethod.POST, "/janitor/add").hasAuthority("janitor.create")
//                .requestMatchers(HttpMethod.GET, "/janitor/*").authenticated()
////                .requestMatchers(HttpMethod.GET, "/upcomingTournaments/enroll/{{playerId}}/{{tournamentId}}").hasRole("USER")
////                .requestMatchers(HttpMethod.PUT, "/upcomingTournaments/disenroll/{playerId}/{tournamentId}").permitAll()
//                .requestMatchers(HttpMethod.GET, "/auth/registration").anonymous()
//                .requestMatchers(HttpMethod.POST, "/auth/register").anonymous()
//                .requestMatchers(HttpMethod.POST, "/auth/invalid-confirmation").anonymous()
//                .requestMatchers(HttpMethod.POST, "/auth/changeUsername").anonymous()
//                .requestMatchers(HttpMethod.POST, "/auth/confirmation").anonymous()
////                .requestMatchers(HttpMethod.POST, "http://localhost:8093/swagger-ui/index.html").anonymous()
//                .requestMatchers(HttpMethod.GET, "/*").authenticated()
//                .requestMatchers(HttpMethod.POST, "/*").authenticated()
                .and().csrf().disable();
        http.exceptionHandling().accessDeniedPage("/access-denied");
        http.formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
//                .successHandler(customAuthenticationSuccessHandler)
                .permitAll();
        http.logout()
                .logoutSuccessUrl("/janitor/all")
                .permitAll();
        http.httpBasic();
        return http.build();
}





}

