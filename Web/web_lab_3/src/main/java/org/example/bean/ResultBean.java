package org.example.bean;

import org.example.entity.PointResult;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named("resultBean")
@ViewScoped

public class ResultBean implements Serializable {

    @PersistenceContext(unitName = "pointCheckerPU")
    private EntityManager entityManager;

    private List<PointResult> results;

    @PostConstruct
    public void init() {
        loadResults();
    }

    public void loadResults() {
        results = entityManager.createQuery(
                "SELECT p FROM PointResult p ORDER BY p.timestamp DESC",
                PointResult.class
        ).getResultList();
    }

    @Transactional
    public void addResult(PointResult result) {
        entityManager.persist(result);
        loadResults();
    }

    public List<PointResult> getResults() {
        return results;
    }
}