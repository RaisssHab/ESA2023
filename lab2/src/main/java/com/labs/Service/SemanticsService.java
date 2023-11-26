package com.labs.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.labs.ORM.Semantics;

@Service
@Transactional
public class SemanticsService {

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
