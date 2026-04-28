package org.example.bean;


import org.example.entity.PointResult;
import org.example.entity.User;
import org.example.service.AreaChecker;
import org.example.service.Validate;
import org.example.service.ValidateRes;

import javax.ejb.Stateless;
import java.util.Date;

@Stateless
public class AreaCheckBean {
    public PointResult checkPoint(float x, float y, float r, User user) {
        Validate validate = new Validate();
        AreaChecker areaChecker = new AreaChecker();


        ValidateRes validation = validate.check(
                String.valueOf(x),
                String.valueOf(y),
                String.valueOf(r)
        );

        if (!validation.isValid()) {
            throw new IllegalArgumentException(validation.getMessage());
        }

        String result = areaChecker.checkHit(
                String.valueOf(x),
                String.valueOf(y),
                String.valueOf(r)
        );

        PointResult pointResult = new PointResult();
        pointResult.setX(x);
        pointResult.setY(y);
        pointResult.setR(r);
        pointResult.setIsShoot(result);
        pointResult.setTimestamp(new Date());
        pointResult.setUser(user);

        return pointResult;
    }
}
