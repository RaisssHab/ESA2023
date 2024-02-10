package com.labs.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labs.logging.Notification;

@Service
@Transactional
public class NotificationService {
    @PersistenceContext
    private EntityManager entityManager;

    public Notification getNotificationByEntityName(String entityName) {
        return entityManager.createNamedQuery("Notification.findByEntityName", Notification.class)
        .setParameter("name", entityName)
        .getSingleResult();
    }
}
