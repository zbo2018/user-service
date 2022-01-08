package com.weaver.rpa.tender.search.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    /**
     * 按数量分组
     * @param dataList
     * @param num 每多少个元素一组
     * @return
     */
    public static <T> List<List<T>> groupByNum(List<T> dataList, int num){
        if(dataList==null || dataList.isEmpty()){
            return new ArrayList<>();
        }
        if(num>=dataList.size() || num<=0){
            List<List<T>> groupList = new ArrayList<>();
            groupList.add(dataList);
            return groupList;
        }

        List<List<T>> groupList = new ArrayList<>();
        List<T> tempList = new ArrayList<>();
        for(T vo : dataList){
            if(tempList.size()>=num){
                groupList.add(tempList);
                tempList = new ArrayList<>();
            }
            tempList.add(vo);
        }
        if(!tempList.isEmpty()){
            groupList.add(tempList);
        }

        return groupList;
    }
}
