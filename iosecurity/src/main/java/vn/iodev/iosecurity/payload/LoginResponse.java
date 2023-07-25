package vn.iodev.iosecurity.payload;

import java.util.List;

public class LoginResponse {
    public LoginResponse() {}
    public LoginResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public LoginResponse(String accessToken, List<String> vaiTros) {
        this.accessToken = accessToken;
        this.vaiTros = vaiTros;
    }

    public LoginResponse(String accessToken, List<String> vaiTros, String id) {
        this.accessToken = accessToken;
        this.vaiTros = vaiTros;
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getTokenType() {
        return tokenType;
    }
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    private String accessToken;
    private String tokenType = "Bearer ";
    private List<String> vaiTros;
    private String id;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<String> getVaiTros() {
        return vaiTros;
    }
    public void setVaiTros(List<String> vaiTros) {
        this.vaiTros = vaiTros;
    }
}
