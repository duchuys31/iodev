package vn.iodev.iogateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RouterValidator {
    public static final List<Route> openApiEndpoints = Arrays.asList(
        new Route("/api/taikhoans$", "GET"),
        new Route("/api/tochucs$", "GET"),
        new Route("/api/canhans$", "GET"),
        new Route("/api/authenticate$", "POST"),
        new Route("/api/cuocthis$", "GET"),
        new Route("/api/cuocthis/.+/hinhanhs$", "GET"),
        new Route("/api/cuocthis/.+$", "GET"),
        new Route("/api/cuocthis/.+/khoithis$", "GET"),
        new Route("/api/khoithis$", "GET"),
        new Route("/api/khoithis/.+$", "GET"),
        new Route("/api/doanthis$", "GET"),
        new Route("/api/doanthis/.+$", "GET"),
        new Route("/api/huanluyenviens$", "GET"),
        new Route("/api/huanluyenviens/.+$", "GET"),
        // new Route("/api/cuocthis/.+/huanluyenviens$", "GET"),
        new Route("/api/khoithis/.+$", "GET"),
        new Route("/api/thisinhs$", "GET"),
        new Route("/api/cuocthis/.+$/thisinhs", "GET"),
        new Route("/api/thisinhs/.+$", "GET"),
        new Route("/api/doithis$", "GET"),
        new Route("/api/cuocthis/.+$/doithis", "GET"),
        new Route("/api/doithis/.+$", "GET"),
        new Route("/api/danhsachthis$", "GET"),
        new Route("/api/cuocthis/.+$/danhsachthis", "GET"),
        new Route("/api/danhsachthis/.+$", "GET"),
        new Route("/api/danhmucs$", "GET"),
        new Route("/api/auth/.+/verify-email*$", "PUT"),
        new Route("/api/auth/register$", "POST"),
        new Route("/api/auth/.+/quenmatkhau", "POST"),
        new Route("/api/auth/forgot-password/.+/verify-email*$", "PUT")
    );
    public boolean isSecured(ServerHttpRequest request) {
        boolean securedFlag = true;
        for (Route r : openApiEndpoints) {
            if (request.getMethodValue().equals(r.getHttpMethod()) && request.getURI().getPath().matches(r.getUrlPattern())) {
                securedFlag = false;
                break;
            }
        }

        return securedFlag;
    }
}
