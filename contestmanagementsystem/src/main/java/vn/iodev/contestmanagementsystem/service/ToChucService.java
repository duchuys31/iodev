package vn.iodev.contestmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import vn.iodev.contestmanagementsystem.payload.ToChucResponse;

@Component
@Slf4j
public class ToChucService {
    @Autowired
    private LoadBalancerClient loadBalancer;
    
    public ToChucResponse[] getToChucByTenGoi(String tenGoi) {
        ServiceInstance serviceInstance = loadBalancer.choose(IOServiceEndpoint.HUMANRESOURCES_SERVICE_ID);
        String url = serviceInstance.getUri().toString();
        url = url + "/api/tochucs?tenGoi={tenGoi}";
        
        RestTemplate restTemplate = new RestTemplate();
        ToChucResponse[] toChucs = restTemplate.getForObject(url,ToChucResponse[].class, tenGoi);

        return toChucs;
    }

    public ToChucResponse getToChucById(String id) {
        ServiceInstance serviceInstance = loadBalancer.choose(IOServiceEndpoint.HUMANRESOURCES_SERVICE_ID);
        String url = serviceInstance.getUri().toString();
        url = url + "/api/tochucs/{id}";
        
        RestTemplate restTemplate = new RestTemplate();
        ToChucResponse toChuc = restTemplate.getForObject(url,ToChucResponse.class, id);

        return toChuc;
    }

    public ToChucResponse createToChuc(ToChucResponse tcr) {
        ServiceInstance serviceInstance = loadBalancer.choose(IOServiceEndpoint.HUMANRESOURCES_SERVICE_ID);
        String url = serviceInstance.getUri().toString();
        url = url + "/api/tochucs";
        ToChucResponse[] findByTenGois = getToChucByTenGoi(tcr.getTenGoi());
        log.info("Find " + tcr.getTenGoi() + ", found: " + findByTenGois.length);
        if (findByTenGois.length > 0) {
            return findByTenGois[0];
        }
        else {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(url, tcr, ToChucResponse.class);
        }
    }
}
