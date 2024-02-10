package com.labs.logging;

import javax.persistence.*;

@Entity
@Table(name = "change_log")
public class ChangeLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "change_id")
    private Long changeId;
    
    @Column(name = "change_type", nullable = false, length = 50)
    private String changeType;
    
    @Column(name = "entity_class", nullable = false, length = 50)
    private String entityClass;
    
    @Column(name = "entity_id", nullable = false)
    private Long entityId;
    
    @Column(name = "field_name", length = 50)
    private String fieldName;
    
    @Column(name = "new_value", length = 1000)
    private String newValue;
    
    public ChangeLog() {}

    // Getters and Setters
    public Long getChangeId() {
        return changeId;
    }

    public void setChangeId(Long changeId) {
        this.changeId = changeId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}