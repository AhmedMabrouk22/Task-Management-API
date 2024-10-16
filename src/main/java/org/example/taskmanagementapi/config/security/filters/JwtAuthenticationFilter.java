package org.example.taskmanagementapi.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.taskmanagementapi.config.security.user.CustomUserDetails;
import org.example.taskmanagementapi.config.security.user.CustomUserDetailsService;
import org.example.taskmanagementapi.exceptions.auth.AuthException;
import org.example.taskmanagementapi.utils.ErrorResponse;
import org.example.taskmanagementapi.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
//        1) Get token from header and Check if it valid or not

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = authHeader.substring(7);
            String userEmail = jwtUtils.extractClaim(jwt, (claims -> claims.get("email"))).toString();

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            load user from db
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);

                if (userDetails.getLastPasswordChange() != null) {
                    long iat = jwtUtils.extractClaim(jwt, claims -> claims.get("iat", Long.class));
                    Instant instant = Instant.ofEpochMilli(iat * 1000);
                    LocalDateTime iatTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    if (iatTime.isBefore(userDetails.getLastPasswordChange())) {
                        throw new AuthException("password changed, please login again", HttpStatus.UNAUTHORIZED);
                    }
                }

//            check token is valid or not
                if (jwtUtils.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            ErrorResponse err = new ErrorResponse();
            err.setMessage("Access token is expired, login again");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(err));
        } catch (JwtException ex) {
            ErrorResponse err = new ErrorResponse();
            err.setMessage("Invalid token");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(err));
        } catch (AuthException ex) {
            ErrorResponse err = new ErrorResponse();
            err.setMessage(ex.getMessage());
            response.setStatus(ex.getStatusCode().value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(err));
        }
    }
}
