package com.labs.Services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SourceService {

    @PersistenceContext
    private EntityManager entityManager;

    // Methods for CRUD operations on Source entity
}