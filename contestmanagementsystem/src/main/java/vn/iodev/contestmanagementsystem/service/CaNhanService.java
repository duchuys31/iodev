package vn.iodev.contestmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.iodev.contestmanagementsystem.payload.CaNhanResponse;

@Service
public class CaNhanService {
    @Autowired
    private LoadBalancerClient loadBalancer;
    
    public CaNhanResponse getCaNhanById(String id) {
        ServiceInstance serviceInstance = loadBalancer.choose(IOServiceEndpoint.HUMANRESOURCES_SERVICE_ID);
        String url = serviceInstance.getUri().toString();
        url = url + "/api/canhans/{id}";
        
        RestTemplate restTemplate = new RestTemplate();
        CaNhanResponse caNhan = restTemplate.getForObject(url,CaNhanResponse.class, id);

        return caNhan;
    }
}
