package org.example.dto;

import org.example.entity.PointResult;

public class PointResponse {
    private String x;
    private String y;
    private String r;
    private String isShoot;
    private String timestamp;
    private String user;

    public PointResponse() {}


    public PointResponse(PointResult pointResult) {
        this.x = String.valueOf(pointResult.getX());
        this.y = String.valueOf(pointResult.getY());
        this.r = String.valueOf(pointResult.getR());
        this.isShoot = pointResult.getIsShoot();
        this.timestamp = pointResult.getTimestamp().toString();
        this.user = pointResult.getUser().toString();
    }


    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getIsShoot() {
        return isShoot;
    }

    public void setIsShoot(String isShoot) {
        this.isShoot = isShoot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
