package com.labs.request;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "semanticsRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class SemanticsRequest {
    @JsonbProperty("semanticId")
    private Long semanticId;
    @JsonbProperty("translation") 
    private String translation;
    @JsonbProperty("explanation") 
    private String explanation;

    public SemanticsRequest() {}

    public Long getSemanticId() {
        return semanticId;
    }
    public String getTranslation() {
        return translation;
    }
    public String getExplanation() {
        return explanation;
    }

    public void setSemanticId(Long semanticId) {
        this.semanticId = semanticId;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
