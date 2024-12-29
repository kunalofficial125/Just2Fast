package com.just2fast.ushop;

public class DeliveryAreaGeofence {
    private String requestId;
    private String areaName;
    private com.google.android.gms.maps.model.LatLng center;
    private double radiusInMeters;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public com.google.android.gms.maps.model.LatLng getCenter() {
        return center;
    }

    public void setCenter(com.google.android.gms.maps.model.LatLng center) {
        this.center = center;
    }

    public double getRadiusInMeters() {
        return radiusInMeters;
    }

    public void setRadiusInMeters(double radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }



}
