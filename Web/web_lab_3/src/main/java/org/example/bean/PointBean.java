package org.example.bean;

import org.example.entity.PointResult;
import org.example.service.AreaChecker;
import org.example.service.Validate;
import org.example.service.ValidateRes;


import javax.enterprise.inject.spi.CDI;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


@ManagedBean(name = "pointBean")
@RequestScoped
public class PointBean {
    private String x;
    private String y;
    private String r;

    private AreaChecker areaChecker = new AreaChecker();
    private Validate validate = new Validate();


    public void checkPoint() {
        ValidateRes validateRes = validate.check(x, y, r);
        if (!validateRes.isValid()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", validateRes.getMessage()));
            return;
        }


        String isShoot = areaChecker.checkHit(x, y, r);

        PointResult result = new PointResult(isShoot,
                Integer.parseInt(x),
                Float.parseFloat(y),
                Integer.parseInt(r));


        getResultBean().addResult(result);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Результат", isShoot));
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

    private ResultBean getResultBean() {
        return CDI.current().select(ResultBean.class).get();
    }
}
