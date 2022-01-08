package com.weaver.rpa.tender.search.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TenderKeywordEsDto implements Serializable {
    private static final long serialVersionUID = -5683183968953515950L;

    @Expose
    private Long id;
    @Expose
    private String appId;
    @Expose
    private String tag;
    @Expose
    private Integer tagLength;//字长度。纯数字、英文算一个字
    @Expose
    private String classify;//分类


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getTagLength() {
        return tagLength;
    }

    public void setTagLength(Integer tagLength) {
        this.tagLength = tagLength;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }
}
