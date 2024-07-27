package com.example.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth-service/register",
            "/api/auth-service/login",
            "/eureka/web",
            "/api/auth-service/send-code",
            "/api/information-service/user/reset-password",
            "/api/product-service/guest/category/get-all-category",
            "/api/product-service/guest/product/get-all-product",
            "/api/product-service/guest/product/get-product-detail",
            "/api/product-service/guest/product/get-product-by-category",
            "/api/product-service/guest/product/get-all-product-by-query"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
