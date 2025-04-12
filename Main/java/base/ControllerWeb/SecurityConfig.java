package base.ControllerWeb;

import base.Service.SecurityService.MyClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig{

    @Bean
    public UserDetailsService userDetailsService(){
        return new MyClientService();
    }

    private final JwtTokenProvider jwtTokenProvider;
    private final MyClientService myClientService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          MyClientService myClientService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myClientService = myClientService;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                                           MyClientService myClientService) {
        return new JwtAuthenticationFilter(jwtTokenProvider, myClientService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/").permitAll()
                                .requestMatchers("/index").authenticated()
                                .requestMatchers("/api/auth/login").permitAll()
                                .requestMatchers("/api/clients").hasAuthority("ROLE_ADMIN")
//                                .requestMatchers("/api/categories/**").permitAll()
//                                .requestMatchers("/api/clients/**").permitAll()
//                                .requestMatchers("/api/manufacturers/**").permitAll()
//                                .requestMatchers("/api/orders/**").permitAll()
//                                .requestMatchers("/api/products/**").permitAll()
//                                .requestMatchers("/api/shippings/**").permitAll()
                ).addFilterBefore(jwtAuthenticationFilter(
                                jwtTokenProvider,
                                myClientService),
                        UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll
                ).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}
