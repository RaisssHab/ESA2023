package com.labs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labs.orm.Phrase;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class PhraseService {

    @PersistenceContext
    private EntityManager entityManager;

    public void addPhrases(Phrase phrases) {
        entityManager.persist(phrases);
    }

    public void removePhrases(Long phraseId) {
        Phrase phrases = entityManager.find(Phrase.class, phraseId);
        if (phrases != null) {
            entityManager.remove(phrases);
        }
    }

    public void updatePhrases(Phrase phrases) {
        entityManager.merge(phrases);
    }

    public Phrase findPhrasesById(Long phraseId) {
        return entityManager.find(Phrase.class, phraseId);
    }

    public List<Phrase> getAllPhrases() {
        return entityManager.createQuery("SELECT p FROM Phrase p", Phrase.class).getResultList();
    }
}
