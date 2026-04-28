package org.example.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "point_results")
public class PointResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;


    @Column(name = "is_shoot")
    private String isShoot;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private float y;

    @Column(nullable = false)
    private int r;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;


    public PointResult(){
    }

    public PointResult(String isShoot, int x, float y, int r) {
        this.isShoot = isShoot;
        this.x = x;
        this.y = y;
        this.r = r;
        this.timestamp = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsShoot() {
        return isShoot;
    }

    public void setIsShoot(String isShoot) {
        this.isShoot = isShoot;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
