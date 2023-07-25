package vn.iodev.iogateway.security;

public class Route {
    public Route(String urlPattern, String httpMethod) {
        this.urlPattern = urlPattern;
        this.httpMethod = httpMethod;
    }
    public String getUrlPattern() {
        return urlPattern;
    }
    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }
    public String getHttpMethod() {
        return httpMethod;
    }
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
    private String urlPattern;
    private String httpMethod;
}
