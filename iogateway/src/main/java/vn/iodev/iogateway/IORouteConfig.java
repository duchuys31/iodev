package vn.iodev.iogateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.iodev.iogateway.security.AuthenticationFilter;

import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@Configuration
@EnableHystrix
public class IORouteConfig {
    @Autowired
    AuthenticationFilter filter;
    
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                    .route("humanresources", r -> r.path("/api/files/tochucs/**", "/api/files/canhans/**")
                        .uri("lb://humanresources-service"))
                    .route("humanresources", r -> r.path("/api/tochucs/**", "/api/canhans/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://humanresources-service"))
                    .route("iosecurity", r -> r.path("/api/authenticate/**", "/api/taikhoans/**", "/api/vaitros", "/api/mail/**", "/api/auth/**", "/api/forgot-password/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://iosecurity-service"))
                    .route("cors", r -> r.path("/api/cms/cors")
                        .uri("lb://contestmanagementsystem-service"))
                    .route("contestmanagementsystem", r -> r.path("/api/files/cuocthis/**")
                        .uri("lb://contestmanagementsystem-service"))
                    .route("contestmanagementsystem", r -> r.path("/api/cuocthis/**", "/api/danhsachthis/**", "/api/doanthis/**", "/api/doithis/**", "/api/huanluyenviens/**", "/api/khoithis/**", "/api/thisinhs/**", "/api/danhmucs/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://contestmanagementsystem-service"))
                .build();
    }
}
