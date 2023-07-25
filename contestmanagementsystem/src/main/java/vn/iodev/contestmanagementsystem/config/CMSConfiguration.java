package vn.iodev.contestmanagementsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CMSConfiguration {
    @Value("${io.cms.gateway.url}")
    private String gatewayUrl;
    public String getGatewayUrl() {
        return gatewayUrl;
    }
    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }
    public CMSConfiguration() {}
}
