package com.labs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labs.helper.MapDifferentiator;
import com.labs.helper.SemanticsMapper;
import com.labs.jms.MessageSender;
import com.labs.logging.ChangeType;
import com.labs.orm.Semantics;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class SemanticsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MessageSender messageSender;

    public void addSemantics(Semantics semantics) {
        entityManager.persist(semantics);
        entityManager.flush();
        messageSender.sendMessage(ChangeType.CREATE, "semantics", semantics.getSemanticId(), 
                                SemanticsMapper.map(semantics));
    }

    public void removeSemantics(Long semanticId) {
        Semantics semantics = entityManager.find(Semantics.class, semanticId);
        if (semantics != null) {
            entityManager.remove(semantics);
        }
        messageSender.sendMessage(ChangeType.DELETE, "semantics", semantics.getSemanticId(), 
                                SemanticsMapper.map(semantics));
    }

    public void updateSemantics(Semantics semantics, Map<String, Object> oldSemanticsMap) {
        entityManager.merge(semantics);
        entityManager.flush();
        messageSender.sendMessage(ChangeType.UPDATE, "semantics", semantics.getSemanticId(),
                                MapDifferentiator.differentiate(oldSemanticsMap, SemanticsMapper.map(semantics)));
    }

    public Semantics findSemanticsById(Long semanticId) {
        return entityManager.find(Semantics.class, semanticId);
    }

    public List<Semantics> getAllSemantics() {
        return entityManager.createQuery("SELECT s FROM Semantics s", Semantics.class).getResultList();
    }
}
