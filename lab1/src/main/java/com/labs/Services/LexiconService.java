package com.labs.Services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.labs.ORM.Lexicon;

import java.util.List;

@Stateless
public class LexiconService {
    
    @PersistenceContext(unitName="myPersistence")
    private EntityManager entityManager;
    
    public void createLexicon(Lexicon lexicon) {
        entityManager.persist(lexicon);
    }

    public Lexicon getLexiconById(Long lexiconId) {
        return entityManager.find(Lexicon.class, lexiconId);
    }

    public void updateLexicon(Lexicon lexicon) {
        entityManager.merge(lexicon);
    }

    public void deleteLexicon(Long lexiconId) {
        Lexicon lexicon = entityManager.find(Lexicon.class, lexiconId);
        if (lexicon != null) {
            entityManager.remove(lexicon);
        }
    }

    public List<Lexicon> getAllLexicons() {
        return entityManager.createQuery("SELECT l FROM Lexicon l", Lexicon.class).getResultList();
    }
}
