package com.labs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labs.helper.MapDifferentiator;
import com.labs.helper.PhraseMapper;
import com.labs.jms.MessageSender;
import com.labs.logging.ChangeType;
import com.labs.orm.Phrase;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class PhraseService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MessageSender messageSender;

    public void addPhrases(Phrase phrases) {
        entityManager.persist(phrases);
        entityManager.flush();
        messageSender.sendMessage(ChangeType.CREATE, "phrases", phrases.getPhraseId(), 
                                PhraseMapper.map(phrases));
    }

    public void removePhrases(Long phraseId) {
        Phrase phrases = entityManager.find(Phrase.class, phraseId);
        if (phrases != null) {
            entityManager.remove(phrases);
        }
        messageSender.sendMessage(ChangeType.DELETE, "phrases", phrases.getPhraseId(), 
                                PhraseMapper.map(phrases));
    }

    public void updatePhrases(Phrase phrases, Map<String, Object> oldPhraseMap) {
        entityManager.merge(phrases);
        entityManager.flush();
        messageSender.sendMessage(ChangeType.UPDATE, "phrases", phrases.getPhraseId(),
                                MapDifferentiator.differentiate(oldPhraseMap, PhraseMapper.map(phrases)));
    }

    public Phrase findPhrasesById(Long phraseId) {
        return entityManager.find(Phrase.class, phraseId);
    }

    public List<Phrase> getAllPhrases() {
        return entityManager.createQuery("SELECT p FROM Phrase p", Phrase.class).getResultList();
    }
}
