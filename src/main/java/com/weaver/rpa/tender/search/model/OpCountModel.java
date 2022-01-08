package com.weaver.rpa.tender.search.model;

/**
 * DocWriteRequest 不同类型的文档操作数
 */
public class OpCountModel {

    private int indexCount;
    private int creatCount;
    private int updateCount;
    private int deleteCount;

    public OpCountModel(){}

    public OpCountModel(int indexCount,int creatCount,int updateCount,int deleteCount){
        this.indexCount = indexCount;
        this.creatCount = creatCount;
        this.updateCount = updateCount;
        this.deleteCount = deleteCount;
    }

    public void increaseIndexCount() {
        indexCount++;
    }

    public void increaseCreatCount() {
        creatCount++;
    }

    public void increaseUpdateCount() {
        updateCount++;
    }

    public void increaseDeleteCount() {
        deleteCount++;
    }

    public int getIndexCount() {
        return indexCount;
    }

    public void setIndexCount(int indexCount) {
        this.indexCount = indexCount;
    }

    public int getCreatCount() {
        return creatCount;
    }

    public void setCreatCount(int creatCount) {
        this.creatCount = creatCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    @Override
    public String toString() {
        return "OpCountModel{" +
                "indexCount=" + indexCount +
                ", creatCount=" + creatCount +
                ", updateCount=" + updateCount +
                ", deleteCount=" + deleteCount +
                '}';
    }
}
