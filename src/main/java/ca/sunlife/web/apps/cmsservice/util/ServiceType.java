package ca.sunlife.web.apps.cmsservice.util;

public enum ServiceType {
    PROSPR("prospr"),
    FAA("faa");

    private String type;

    private ServiceType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}