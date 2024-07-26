package ca.sunlife.web.apps.cmsservice.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Collection;

import ca.sunlife.web.apps.cmsservice.controller.CmsServiceController;
import ca.sunlife.web.apps.cmsservice.model.ServiceRequest;

@Component
public class ServiceRequestFactory {
	private static final Logger logger = LogManager.getLogger(CmsServiceController.class);
	
	@Autowired
	Collection<ServiceRequest> services;

    private static HashMap<String, ServiceRequest> serviceRequestCache = new HashMap<>();

    @PostConstruct
    void initCache() {
        for (ServiceRequest s : services) {
        	s.init();
            serviceRequestCache.put(s.getServiceName(), s);
            System.out.println("Service initialized: " + s.getServiceName());
		}
    }
    
    public ServiceRequest getServiceRequest(String serviceName) {
    	if (serviceRequestCache.containsKey(serviceName)) {
    		return serviceRequestCache.get(serviceName);
    	}
   		logger.info("service class does not exist: " + serviceName);
   		return null;
    }
    
}