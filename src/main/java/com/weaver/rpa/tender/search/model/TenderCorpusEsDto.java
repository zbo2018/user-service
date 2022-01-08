package com.weaver.rpa.tender.search.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class TenderCorpusEsDto implements Serializable {
    private static final long serialVersionUID = 2290309863428671452L;

    @Expose
    private Long id;
    @Expose
    private String appId;
    @Expose
    private String title;
    @Expose
    private String content;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
