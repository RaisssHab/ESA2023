package com.labs.ORM;

import javax.persistence.*;

@Entity
@Table(name = "texts")
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "text_id")
    private Long textId;

    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "source_id")
    private Source source;

    // Constructors, getters, and setters

    public Text() {
    }

    public Text(Source source) {
        this.source = source;
    }

    public Long getTextId() {
        return textId;
    }

    public void setTextId(Long textId) {
        this.textId = textId;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}

