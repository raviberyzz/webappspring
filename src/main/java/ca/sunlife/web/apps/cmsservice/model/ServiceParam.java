package ca.sunlife.web.apps.cmsservice.model;

import org.springframework.stereotype.Component;
import ca.sunlife.web.apps.cmsservice.util.ServiceUtil;

@Component
public class ServiceParam {
    private String paramName = null;
    private boolean paramRequired;
    private String regex = null;
    private String outputName = null;

    public String getParamName() {
    	return this.paramName;
    }
    
    public void setParamName(String s) {
           this.paramName = s;
    }
    
    public boolean getParamRequired() {
    	return this.paramRequired;
    }
    
    public void setParamRequired(boolean isRequired) {
    	this.paramRequired = isRequired;
    }
    
    public String getParamRegex() {
    	return this.regex;
    }
    
    public void setRegex(String s) {
            this.regex = s;
    }
    
    public String getOutputName() {
    	return this.outputName;
    }
    
    public void setOutputName(String s) {
            this.outputName = s;
    }
 
    
   
    
}