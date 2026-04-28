package org.example;

public class Point {
    private String isShoot;
    private String x;
    private String y;
    private String r;
    private String time;
    private String exectime;


    public Point(String isShoot, String x, String y, String r, String time, String exectime) {
        this.isShoot = isShoot;
        this.x = x;
        this.y = y;
        this.r = r;
        this.time = time;
        this.exectime = exectime;
    }

    public String getIsShoot() {
        return isShoot;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getR() {
        return r;
    }

    public String getTime() {
        return time;
    }

    public String getExectime() {
        return exectime;
    }
}
