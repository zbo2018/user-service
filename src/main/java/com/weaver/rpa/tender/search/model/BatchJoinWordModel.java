package com.weaver.rpa.tender.search.model;

import java.util.Map;

public class BatchJoinWordModel {
    //开始拼接的位置
    int start;

    //向左拼接的新词。key:新词、value:最左边拼接的单词位置
    Map<String, Integer> leftJoinWordsMap;
    //向右拼接的新词。key:新词、value:最右边拼接的单词位置
    Map<String, Integer> rightJoinWordsMap;
    //左右同时拼接的新词。key:新词、value:最左边拼接的单词位置_最右边拼接的单词位置
    Map<String, String> leftRightJoinWordsMap;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Map<String, Integer> getLeftJoinWordsMap() {
        return leftJoinWordsMap;
    }

    public void setLeftJoinWordsMap(Map<String, Integer> leftJoinWordsMap) {
        this.leftJoinWordsMap = leftJoinWordsMap;
    }

    public Map<String, Integer> getRightJoinWordsMap() {
        return rightJoinWordsMap;
    }

    public void setRightJoinWordsMap(Map<String, Integer> rightJoinWordsMap) {
        this.rightJoinWordsMap = rightJoinWordsMap;
    }

    public Map<String, String> getLeftRightJoinWordsMap() {
        return leftRightJoinWordsMap;
    }

    public void setLeftRightJoinWordsMap(Map<String, String> leftRightJoinWordsMap) {
        this.leftRightJoinWordsMap = leftRightJoinWordsMap;
    }
}
