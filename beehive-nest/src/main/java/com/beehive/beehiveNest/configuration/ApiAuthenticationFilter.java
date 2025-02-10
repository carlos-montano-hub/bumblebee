package com.beehive.beehiveNest.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

public class ApiAuthenticationFilter implements Filter {

        private final String API_KEY = System.getenv("API_KEY");

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                        throws IOException, ServletException {

                HttpServletRequest httpRequest = (HttpServletRequest) request;
                String apiKeyHeader = httpRequest.getHeader("X-API-Key");

                if (apiKeyHeader == null || !apiKeyHeader.equals(API_KEY)) {
                        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                }

                UserDetails userDetails = User.builder()
                                .username("service-client")
                                .password("") // No password needed
                                .roles("SERVICE") // Service role for authorization
                                .build();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
        }
}