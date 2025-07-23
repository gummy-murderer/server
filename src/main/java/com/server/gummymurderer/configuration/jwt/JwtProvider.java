package com.server.gummymurderer.configuration.jwt;

import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.JpaUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;

    // 만료시간 : 1Hour
    private final long exp = 1000L * 60 * 60;

    private final JpaUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createToken(String steamId) {
        Claims claims = Jwts.claims().setSubject(steamId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        String steamId = this.getSteamId(token);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(steamId);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    // 토큰에 담겨있는 유저 account 획득
    public String getSteamId(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Authorization Header를 통해 인증을 한다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                log.error("🐻 Token 검증 실패: 잘못된 토큰 형식");
                return false;
            }

            // "Bearer " 이후의 토큰만 추출
            String jwt = token.substring(7).trim();
            if (jwt.isEmpty()) {
                log.error("🐻 Token 검증 실패: 토큰이 비어 있음");
                return false;
            }

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.error("🐻 Token 검증 실패: 만료된 토큰");
            return false;
        } catch (JwtException e) {
            log.error("🐻 Token 검증 실패: {}", e.getMessage());
            return false;
        }
    }
}