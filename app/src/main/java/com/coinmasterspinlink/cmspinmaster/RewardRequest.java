package com.coinmasterspinlink.cmspinmaster;

public class RewardRequest {
    private String service_type;
    private String app_id;

    public RewardRequest(String service_type, String app_id) {
        this.service_type = service_type;
        this.app_id = app_id;
    }

    // Getters and Setters
    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }
}

