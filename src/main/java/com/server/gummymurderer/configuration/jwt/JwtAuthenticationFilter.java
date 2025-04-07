package com.server.gummymurderer.configuration.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Jwt가 유효성을 검증하는 Filter
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String token = jwtProvider.resolveToken(request);

        log.info("🤖 token : {}", token);

        // JWT 없이 허용할 API 리스트
        List<String> openApis = List.of(
                "/api/v1/members/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/v1/gcs/**",
                "/api/v1/steam/verify"
                );

        // 허용된 API인지 확인
        boolean isOpenApi = openApis.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));

        if (token == null || token.isBlank() || token.equals("Bearer")) {
            if (isOpenApi) {
                // 허용된 API면 필터 통과
                filterChain.doFilter(request, response);
                return;
            }
            // 허용되지 않은 API는 인증 실패
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token Required");
            return;
        }

        if (token.startsWith("Bearer ") && jwtProvider.validateToken(token)) {
            String jwt = token.substring(7).trim();
            Authentication auth = jwtProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}