package com.weaver.rpa.tender.search.service.impl;

import com.google.gson.Gson;
import com.weaver.rpa.tender.search.excelMode.ReadModel;
import com.weaver.rpa.tender.search.model.NewWordDto;
import com.weaver.rpa.tender.search.service.TenderCorpusElasticService;
import com.weaver.rpa.tender.search.util.EasyExcelUtil;
import com.weaver.rpa.tender.search.util.FileUtil;
import com.weaver.rpa.tender.search.util.ResponseModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TenderCorpusElasticServiceImplTest {

    @Autowired
    private TenderCorpusElasticService tenderCorpusElasticService;

    @Test
    public void findNewWordsTest(){
        long start = System.currentTimeMillis();
        ResponseModel<Map<String, Integer>> resp = tenderCorpusElasticService.findNewWords();
        long end = System.currentTimeMillis();
        System.out.println("最终执行结果："+new Gson().toJson(resp));
        System.out.println("总耗时："+(end-start)+"ms");
    }



    @Test
    public void findNewWordsByNgramAndDocFreqTest(){
        long start = System.currentTimeMillis();
        ResponseModel<Map<String, Integer>> resp = tenderCorpusElasticService.findNewWordsByNgramAndDocFreq();
        long end = System.currentTimeMillis();
        System.out.println("最终执行结果："+new Gson().toJson(resp));
        System.out.println("总耗时："+(end-start)+"ms");
    }

    @Test
    public void filterNewWordsTest() throws FileNotFoundException {
        String path = "C:\\Users\\Administrator\\Desktop\\计算出的实体.xlsx";
        InputStream inputStream = new FileInputStream(path);
        int sheetNo = 1;
        int headLineMun = 0;
        // 解析每行结果在listener中处理：实现业务逻辑
        ExcelListener<ReadModel> excelListener = new ExcelListener();
        EasyExcelUtil.readBigSheet(inputStream, sheetNo, headLineMun, ReadModel.class, excelListener);
        List<ReadModel> data = excelListener.getData();

        Map<String, Integer> wordMap = data.stream()
                .collect(Collectors.toMap(m -> m.getWord(), m -> m.getDf()));

        ResponseModel<List<NewWordDto>> resp = tenderCorpusElasticService.filterNewWords(wordMap);
        List<NewWordDto> wordDtoList = resp.getData();

        wordMap = wordDtoList.stream().collect(Collectors.toMap(w -> w.getWord(), w -> w.getDf()));
        System.out.println(new Gson().toJson(wordDtoList));
        System.out.println(new Gson().toJson(wordMap));
    }


    @Test
    public void matchTest(){
        String str = "cgjd@cmict.chinamobile.com";
        str = "bidcente";
        boolean matches = str.matches("^[\\w\\.@\\-&]+$");
        System.out.println(str+"="+matches);

        str = "1.";
        str = str.replaceAll("\\.|@|\\-|&|\\d|_","");
        System.out.println(str);

        List<String> strList = new ArrayList<>();
        strList.add("项目编号");
        strList.add("123");
        strList.add("123项目编456号");
        strList.add("2021年信息化");
        strList.add("2021年信息化abc");
        for(String s : strList){
            matches = s.matches(".*\\d+.*") && s.matches(".*[\\u4e00-\\u9fa5]+.*");
            System.out.println(s+"="+matches);
        }


        str = "b";
        matches = str.matches("[a-zA-Z]+");
        System.out.println(str+"="+matches);
    }
}
