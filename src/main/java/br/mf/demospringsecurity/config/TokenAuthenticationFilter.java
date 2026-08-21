package br.mf.demospringsecurity.config;

import br.mf.demospringsecurity.service.TokenService;
import br.mf.demospringsecurity.service.TokenService.TokenValidationResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(TokenAuthenticationFilter.class);
    private final TokenService tokenService;

    public TokenAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Rotas públicas: o filtro JWT nem roda (não exige Bearer).
     * Inclui H2 Console (todas as subrotas: /h2-console, /h2-console/, /h2-console/login.do, etc.).
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return "/auth/login".equals(path)
                || "/auth/validate".equals(path)
                || path.startsWith("/h2-console")
                || path.startsWith("/swagger-ui")
                || "/swagger-ui.html".equals(path)
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            TokenValidationResult result = tokenService.validateToken(token);
            if (result.isValid()) {
                logger.debug("Token autorizado path={}", path);
                String role = result.getProfile() != null ? "ROLE_" + result.getProfile() : null;
                List<SimpleGrantedAuthority> authorities = (role != null) ? List.of(new SimpleGrantedAuthority(role)) : List.of();
                Authentication auth = new UsernamePasswordAuthenticationToken(result.getLogin(), null, authorities);
                ((UsernamePasswordAuthenticationToken) auth).setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
                return;
            }
            logger.debug("Token nao autorizado path={}", path);
        } else {
            logger.debug("Token nao localizado path={}", path);
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}