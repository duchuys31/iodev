package vn.iodev.humanresources.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HumanResourcesConfiguration {
    @Value("${io.humanresources.id.generated.retry:10}")
    private int nOfIdGeneratedRetry;

    @Value("${io.humanresources.id.canhan.length:6}")
    private int caNhanIdLength;

    @Value("${io.humanresources.id.tochuc.length:5}")
    private int toChucIdLength;

    public int getToChucIdLength() {
        return toChucIdLength;
    }
    public void setToChucIdLength(int toChucIdLength) {
        this.toChucIdLength = toChucIdLength;
    }
    public int getCaNhanIdLength() {
        return caNhanIdLength;
    }
    public void setCaNhanIdLength(int caNhanIdLength) {
        this.caNhanIdLength = caNhanIdLength;
    }
    public int getnOfIdGeneratedRetry() {
        return nOfIdGeneratedRetry;
    }
    public void setnOfIdGeneratedRetry(int nOfIdGeneratedRetry) {
        this.nOfIdGeneratedRetry = nOfIdGeneratedRetry;
    }
    private static final HumanResourcesConfiguration _configuration = new HumanResourcesConfiguration();
    public HumanResourcesConfiguration() {}
    public static HumanResourcesConfiguration getInstance() {
        return _configuration;
    }
}
