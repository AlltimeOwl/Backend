package com.owl.payrit.domain.auth.filter;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.service.UserDetailServiceImpl;
import com.owl.payrit.domain.auth.util.JwtProvider;
import com.owl.payrit.domain.member.entity.OauthInformation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Pattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailServiceImpl userDetailService;
    private final JwtProvider jwtProvider;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private static final Pattern PUBLIC_ENDPOINTS = Pattern.compile("/api/v1/oauth/.*|/h2-console/.*|/swagger-ui/.*|/");

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String requestURI = request.getRequestURI();

        log.debug("authorization : '{}'", authorization);

        if (isPublicEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 유효성 검사
        if (isEmptyOrInvalidAuthorization(authorization)) {
            log.error("authentication is null or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        if (authorization.startsWith("Bearer ")) {
            handleAccessToken(token, request, response, filterChain);
        } else if (authorization.startsWith("Refresh ")) {
            handleRefreshToken(token, response);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isEmptyOrInvalidAuthorization(String authorization) {
        return authorization == null || !(authorization.startsWith("Bearer ") || authorization.startsWith("Refresh "));
    }

    // Access Token 처리, 권한 부여하기
    private void handleAccessToken(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (jwtProvider.isExpired(token, secretKey)) {
            log.error("access token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        OauthInformation oauthInformation = jwtProvider.getOauthInformation(token, secretKey);
        Long id = jwtProvider.getId(token, secretKey);
        UserDetails user = userDetailService.loadUserByOauthInformation(oauthInformation);
        LoginUser loginUser = new LoginUser(id, oauthInformation);
        authorizeUser(loginUser, user.getAuthorities(), request);
    }

    private void authorizeUser(LoginUser loginUser, Collection<? extends GrantedAuthority> authorities, HttpServletRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, authorities);

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void handleRefreshToken(String token, HttpServletResponse response) {
        try {
            String newAccessToken = jwtProvider.refreshAccessToken(token.substring(8), secretKey);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
        } catch (Exception e) {
            log.error("Failed to refresh access token", e);
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        return PUBLIC_ENDPOINTS.matcher(requestURI).matches();
    }

}
