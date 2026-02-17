package uasz.sn.Universite_Stage.Universite_Stage.Authentification.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] FOR_ADMINISTRATEUR = {"/Administrateur/**"};
    private static final String[] FOR_ENSEIGNANT = {"/Enseignant/**"};
    private static final String[] FOR_ETUDIANT = {"/Etudiant/**"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
                                .requestMatchers("/login**", "/logout**").permitAll()
                                .requestMatchers("/h2/**").permitAll()
                                .requestMatchers(FOR_ADMINISTRATEUR).hasRole("Administrateur")
                                .requestMatchers(FOR_ETUDIANT).hasRole("Etudiant")
                                .requestMatchers(FOR_ENSEIGNANT).hasRole("Enseignant")
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Page de connexion personnalisée
                                .defaultSuccessUrl("/") // Redirection après une connexion réussie
                                .failureUrl("/login?error=true") // Redirection en cas d'échec de connexion
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // URL de déconnexion
                                .logoutSuccessUrl("/login?logout=true") // Redirection après déconnexion
                                .permitAll()
                );

        return http.build();
    }
}