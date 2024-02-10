package com.labs.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labs.logging.ChangeLog;

@Service
@Transactional
public class ChangeLogService {
    @PersistenceContext
    private EntityManager entityManager;

    public void createLog(ChangeLog changeLog) {
        entityManager.persist(changeLog);
    }
}
