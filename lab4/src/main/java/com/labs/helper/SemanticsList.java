package com.labs.helper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.labs.orm.Semantics;

@XmlRootElement
public class SemanticsList {
    private List<Semantics> semantics;

    @XmlElement
    public List<Semantics> getSemantics() {
        return semantics;
    }

    public void setSemantics(List<Semantics> semantics) {
        this.semantics = semantics;
    }
}
