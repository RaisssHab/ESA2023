package com.labs.Beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.labs.ORM.Semantics;

@Stateless
public class SemanticsBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void addSemantics(Semantics semantics) {
        entityManager.persist(semantics);
    }

    public void removeSemantics(Long semanticId) {
        Semantics semantics = entityManager.find(Semantics.class, semanticId);
        if (semantics != null) {
            entityManager.remove(semantics);
        }
    }

    public void updateSemantics(Semantics semantics) {
        entityManager.merge(semantics);
    }

    public Semantics findSemanticsById(Long semanticId) {
        return entityManager.find(Semantics.class, semanticId);
    }

    public List<Semantics> getAllSemantics() {
        return entityManager.createQuery("SELECT s FROM Semantics s", Semantics.class).getResultList();
    }
}
