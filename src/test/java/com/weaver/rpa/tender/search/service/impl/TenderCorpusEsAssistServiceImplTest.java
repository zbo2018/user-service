package com.weaver.rpa.tender.search.service.impl;

import com.google.gson.Gson;
import com.weaver.rpa.tender.search.model.TenderCorpusEsDto;
import com.weaver.rpa.tender.search.service.TenderCorpusEsAssistService;
import com.weaver.rpa.tender.search.util.FileUtil;
import com.weaver.rpa.tender.search.util.ReadFromFile;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TenderCorpusEsAssistServiceImplTest {

    @Autowired
    TenderCorpusEsAssistService tenderEsAssistService;

    @Test
    public void batchInsertTest() throws InterruptedException, FileNotFoundException {
        String path = "C:\\Users\\Administrator\\Desktop\\corpus";
        List<String> corpusPaths = FileUtil.getAllSubAbsolutePathFileName(path);

        AtomicLong index = new AtomicLong(1);
        for(String corpusPath : corpusPaths){
            /*List<String> corpusList = ReadFromFile.readAllFile(corpusPath);
            String title = corpusList.get(0);
            corpusList.remove(0);
            String content = StringUtils.join(corpusList, "\r\n");*/

            byte[] contentByte = ReadFromFile.read(corpusPath);
            String content = new String(contentByte);

            TenderCorpusEsDto dto = new TenderCorpusEsDto();
            dto.setId(index.getAndIncrement());
            dto.setTitle("");
            dto.setContent(content);

            List<TenderCorpusEsDto> data = new ArrayList<>();
            data.add(dto);
            tenderEsAssistService.batchInsert(data);
        }


        Thread.sleep(5*1000);
    }


    @Test
    public void viewAnalyzerTermsTest(){
        String analyzer = "my_hanlp_remove_stop_analyzer";
        String content = "<div><noticecontent><p> (略) 业务需求，现 (略) “电子印章防伪控制产品采购项目”供应商推荐/征集工作。 (略) 要求的企业均可自愿报名，并提交相关证明文件和材料。具体情况如下：</p><p> * 、供应商征集信息发布渠道</p><p>1. (略) 集中采购管理信息系统</p><p>http:/ *** 中 (略) www.c *** </p><p> * 、项目编号： *** </p><p> * 、项目名称： (略) “电子印章防伪控制产品采购项目”</p><p> * 、项目地点： (略) </p>";

        List<Map<String, Object>> list = tenderEsAssistService.viewAnalyzerTerms(analyzer, content);
        System.out.println(new Gson().toJson(list));
    }

}
