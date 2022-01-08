package com.weaver.rpa.tender.search.model;

import java.util.List;

/**
 * 发现的新词
 */
public class NewWordDto {
    private String word;//发现的新词
    private Integer df;//文档频率
    private List<String> terms;//发现的新词再分词
    private List<String> tags;//发现的新词属于哪些标签

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getDf() {
        return df;
    }

    public void setDf(Integer df) {
        this.df = df;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
