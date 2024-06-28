package com.example.apigateway.filter;

import com.example.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtil;
    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Retrieve token from request header
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new RuntimeException("Missing or invalid authorization header");
                }
                String token = authHeader.substring(7); // Extract token from header
                try {
                    jwtUtil.validateToken(token); // Validate the token
                } catch (Exception e) {
                    throw new RuntimeException("Invalid token: " + e.getMessage());
                }

                // Add role or user information to headers if needed
                String role = jwtUtil.extractRole(token);
                exchange.getRequest().mutate()
                        .header("X-Role", role)
                        .build();
                System.out.println(role);
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }
}
