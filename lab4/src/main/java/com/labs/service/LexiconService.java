package com.labs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labs.helper.LexiconMapper;
import com.labs.helper.MapDifferentiator;
import com.labs.jms.MessageSender;
import com.labs.logging.ChangeType;
import com.labs.orm.Lexicon;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LexiconService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MessageSender messageSender;

    public void createLexicon(Lexicon lexicon) {
        entityManager.persist(lexicon);
        entityManager.flush();
        messageSender.sendMessage(ChangeType.CREATE, "lexicon", lexicon.getLexiconId(), 
                                LexiconMapper.map(lexicon, lexicon.getPhrase(), lexicon.getSemantics()));
    }

    public Lexicon getLexiconById(Long lexiconId) {
        return entityManager.find(Lexicon.class, lexiconId);
    }

    public void updateLexicon(Lexicon lexicon, Map<String, Object> oldLexiconMap) {
        entityManager.merge(lexicon);
        entityManager.flush();
        messageSender.sendMessage(ChangeType.UPDATE, "lexicon", lexicon.getLexiconId(),
                                MapDifferentiator.differentiate(oldLexiconMap, LexiconMapper.map(lexicon, lexicon.getPhrase(), lexicon.getSemantics())));
    }

    public void deleteLexicon(Long lexiconId) {
        Lexicon lexicon = entityManager.find(Lexicon.class, lexiconId);
        if (lexicon != null) {
            entityManager.remove(lexicon);
        }
        messageSender.sendMessage(ChangeType.DELETE, "lexicon", lexicon.getLexiconId(), 
                                LexiconMapper.map(lexicon, lexicon.getPhrase(), lexicon.getSemantics()));
    }

    public List<Lexicon> getAllLexicons() {
        return entityManager.createQuery("SELECT l FROM Lexicon l", Lexicon.class).getResultList();
    }
}
