package com.weaver.rpa.tender.search.service.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener<T> extends AnalysisEventListener<T> {


    private List<T>  data = new ArrayList();//暂时存储data

    /**
     * 回调执行：解析的当前行数据
     * 注意：
     *      空行，不会回调invoke()方法
     *      用javaModel读取，标题行不会回调invoke()方法
     * @param obj
     * @param context
     */
    @Override
    public void invoke(T obj, AnalysisContext context) {
        /*Integer currentRowNum = context.getCurrentRowNum();
        Sheet currentSheet = context.getCurrentSheet();
        System.out.println("sheetName="+currentSheet.getSheetName()+" 当前行："+currentRowNum);
        System.out.println(currentSheet);*/

        //对第一行表头做校验
        /*if(context.getCurrentRowNum()==0){
            List<String> headList = (List<String>) obj;
            System.out.println("标题长度:"+headList+"，标题:"+headList);
        }*/
        //
        data.add(obj);
        /*if(data.size()>=100){
            doSomething();
            data = new ArrayList();
        }*/

    }

    /**
     * 整个excel解析结束执行
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //doSomething();
    }

    public void doSomething(){
        for (Object o:data) {
            System.out.println(o);
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
