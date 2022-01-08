package com.weaver.rpa.tender.search.excelMode;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @ExcelProperty(index = 3)
 * 数字代表该字段与excel对应列号做映射。第一列从0开始
 *
 * @ExcelProperty(value = {"一级表头","二级表头"})
 * 用于解决不确切知道excel第几列和该字段映射，位置不固定，但表头的内容知道的情况。
 */
public class ReadModel extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String word;

    @ExcelProperty(index = 1)
    private Integer df;


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

    @Override
    public String toString() {
        return "ReadModel{" +
                "word='" + word + '\'' +
                ", df=" + df +
                '}';
    }
}
