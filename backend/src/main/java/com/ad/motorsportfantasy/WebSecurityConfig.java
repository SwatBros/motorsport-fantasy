package com.ad.motorsportfantasy;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ad.motorsportfantasy.authentication.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();

        delegate.setCsrfRequestAttributeName("_csrf");

        CsrfTokenRequestHandler requestHandler = delegate::handle;

        http
                .cors(withDefaults())
                .csrf((csrf) -> csrf
                    .csrfTokenRepository(tokenRepository)
                    .csrfTokenRequestHandler(requestHandler)
                    .ignoringRequestMatchers("/process_register"))
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/api/**").hasRole("ADMIN")
                                .requestMatchers("/process_login").permitAll()
                                .requestMatchers("/process_register").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .logout((logout) -> logout
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
					}))
                .sessionManagement(s -> s
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "OPTIONS", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "X-Auth-Token","Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials", "X-XSRF-TOKEN", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Content-Type", "X-Auth-Token","Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials", "X-XSRF-TOKEN", "X-Requested-With"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
