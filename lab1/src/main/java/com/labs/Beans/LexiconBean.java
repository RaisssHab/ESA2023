package com.labs.Beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.labs.ORM.Lexicon;

@Stateless
public class LexiconBean {

    @PersistenceContext
    private EntityManager entityManager;

    public void addLexicon(Lexicon lexicon) {
        entityManager.persist(lexicon);
    }

    public void removeLexicon(Long lexiconId) {
        Lexicon lexicon = entityManager.find(Lexicon.class, lexiconId);
        if (lexicon != null) {
            entityManager.remove(lexicon);
        }
    }

    public void updateLexicon(Lexicon lexicon) {
        entityManager.merge(lexicon);
    }

    public Lexicon findLexiconById(Long lexiconId) {
        return entityManager.find(Lexicon.class, lexiconId);
    }

    public List<Lexicon> getAllLexicon() {
        return entityManager.createQuery("SELECT l FROM Lexicon l", Lexicon.class).getResultList();
    }
}
