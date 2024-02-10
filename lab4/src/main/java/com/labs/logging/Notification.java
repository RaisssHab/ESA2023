package com.labs.logging;

import javax.persistence.*;

@Entity
@Table(name = "notification")
@NamedQuery(name = "Notification.findByEntityName", query = "SELECT l FROM Notification l WHERE l.entityName = :name")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email") 
    private String email;

    @Column(name = "entity_name") 
    private String entityName;

    public Notification() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
