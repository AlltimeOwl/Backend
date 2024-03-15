package com.owl.payrit.domain.auth.configuration;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.owl.payrit.domain.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                           .httpBasic(AbstractHttpConfigurer::disable)
                           .cors(Customizer.withDefaults())
                           .headers(headers -> headers.frameOptions(
                               HeadersConfigurer.FrameOptionsConfig::disable))
                           .authorizeHttpRequests(
                               authorize -> authorize.requestMatchers(
                                                         antMatcher("/api/v1/oauth/**")
                                                        ,antMatcher("/swagger-ui/**")
                                                        ,antMatcher("/swagger-resources/**")
                                                        ,antMatcher("/v3/api-docs/**")
                                                        ,antMatcher("/h2-console/**")
                                                        ,antMatcher("/img/**")
                                                        ,antMatcher("/css/**")
                                                        ,antMatcher("/")
                                                     )
                                                     .permitAll()
                                                     .anyRequest()
                                                     .authenticated())
                           .sessionManagement(session -> session.sessionCreationPolicy(
                               SessionCreationPolicy.STATELESS))
                           .addFilterBefore(jwtAuthenticationFilter,
                               UsernamePasswordAuthenticationFilter.class)
                           .getOrBuild();

    }
}
