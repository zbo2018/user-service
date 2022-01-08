package com.weaver.rpa.tender.search.model;

import java.io.Serializable;

public class CorpusTermVO implements Serializable {
    private static final long serialVersionUID = -2655651614879019159L;

    private String term;
    private Integer startOffset;
    private Integer endOffset;
    private Integer position;
    private Integer positionLength;
    private String type;
    
    private boolean matchPartBaseWord;//匹配基础词汇部分文字
    private boolean hasJoin;//true:已经拼接过，false:未拼接

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    public Integer getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(Integer endOffset) {
        this.endOffset = endOffset;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPositionLength() {
        return positionLength;
    }

    public void setPositionLength(Integer positionLength) {
        this.positionLength = positionLength;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMatchPartBaseWord() {
        return matchPartBaseWord;
    }

    public void setMatchPartBaseWord(boolean matchPartBaseWord) {
        this.matchPartBaseWord = matchPartBaseWord;
    }

    public boolean isHasJoin() {
        return hasJoin;
    }

    public void setHasJoin(boolean hasJoin) {
        this.hasJoin = hasJoin;
    }
}
