package org.example.bean;


import org.example.entity.PointResult;
import org.example.entity.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PointBean {

    @PersistenceContext(name = "labPU")
    private EntityManager entityManager;

    @EJB
    private AreaCheckBean areaCheckBean;

    public PointResult savePoint(String xParam, String yParam, String rParam, User user) {
        var x = Float.parseFloat(xParam);
        var y = Float.parseFloat(yParam);
        var r = Float.parseFloat(rParam);

        PointResult pointResult = areaCheckBean.checkPoint(x, y, r, user);

        entityManager.persist(pointResult);

        return pointResult;
    }

    public List<PointResult> getUserPoints(Long userId) {
        return entityManager.createQuery(
                        "SELECT p FROM PointResult p WHERE p.user.id = :userId",
                        PointResult.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }
}
