package com.weaver.rpa.tender.search.service.impl;

import com.google.gson.Gson;
import com.weaver.rpa.tender.search.model.TenderKeywordEsDto;
import com.weaver.rpa.tender.search.service.TenderKeywordElasticService;
import com.weaver.rpa.tender.search.util.ReadFromFile;
import com.weaver.rpa.tender.search.util.ResponseModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TenderKeywordElasticServiceImplTest {

    @Autowired
    TenderKeywordElasticService tenderElasticService;

    @Test
    public void searchTest(){
        //String words = "SCRM,abc,123";
        //String words = "abc,123";
        String words = "";
        String title = "";
        String content = "<div>\n" +
                "江源区委常委会议室无纸化办公系统询价项目\n" +
                "<div>\n" +
                "信息时间： *** \n" +
                "</div>\n" +
                "<OA>rmabc</OA>\n" +
                "yuiworkflow\n" +
                "<div>\n" +
                "<p>\n" +
                "江源区委常委会议室无纸化办公系统\n" +
                "</p>\n" +
                "<p>\n" +
                " (略) \n" +
                "</p>\n" +
                "<p>\n" +
                "参加江源区委常委会议室无纸化办公系统项目的潜在供应商应在 (略) 市公共资 (略) （<a href=\"/zhuce.html?id=news\" target=\"_blank\" oldhref=\"http:/ *** \">http:/ *** ）获取采购文件，并于 * 日9： * 分（ (略) 时间）前提交响应文件。\n" +
                "</a></p><a href=\"/zhuce.html?id=news\" target=\"_blank\" oldhref=\"http:/ *** \">\n" +
                "<p>\n" +
                " * 、项目基本情况\n" +
                "</p>\n" +
                "<p>\n" +
                "项目名称：江源区委常委会议室无纸化办公系统项目\n" +
                "</p>\n" +
                "<p>\n" +
                "项目编号：JYZFCG-XJ *** \n" +
                "</p>\n" +
                "<p>\n" +
                "采购计划编号： *** \n" +
                "</p>\n" +
                "<p>\n" +
                "采购方式：询价\n" +
                "</p>\n" +
                "<p>\n" +
                "项目预算： *** . * 元(本项目不接受超过采购预算的投标)\n" +
                "</p>\n" +
                "<p>\n" +
                "项目内容：工程量清 (略) 内容\n" +
                "</p>\n" +
                "<p>\n" +
                "交货地点：\n" +
                "</p>\n" +
                "<p>\n" +
                "质保标准：符合国家相 (略) 行业标准；\n" +
                "</p>\n" +
                "<p>\n" +
                " (略) 期限： * 年；\n" +
                "</p>\n" +
                "<p>\n" +
                "本项目不接受联合体投标。\n" +
                "</p>\n" +
                "<p>\n" +
                " * 、投标人资格要求：\n" +
                "</p>\n" +
                "<p>\n" +
                "1、符合《中华人民共和国政府采购法》第 * 十 * 条规定的资格要求。\n" +
                "</p>\n" +
                "<p>\n" +
                "2、拒绝列 (略) 为记录期间的企业或个人投标。\n" +
                "</p>\n" +
                "<p>\n" +
                " * 、询价文件的获取\n" +
                "</p>\n" +
                "<p>\n" +
                "凡有意参加投标单位请于 * 日至 * 日（ (略) 时间，法定节假日除外 ）必须登 *  (略) 市公 (略) （http:/ *** ）进行 (略) 上报名，下载询价文件，其他途径获取的询价文件 * 律 (略) 理。\n" +
                "</p>\n" +
                "<p>\n" +
                " * 、响应文件的递交\n" +
                "</p>\n" +
                "<p>\n" +
                "截止时间： * 日9: * （ (略) 时间）\n" +
                "</p>\n" +
                "<p>\n" +
                "地点： (略) 市浑江区 (略) 街道翠柏路 * 号新政务大厅 * 楼 (略) 市公 (略) （ (略)  (略) ）北侧（ * ）开标室\n" +
                "</p>\n" +
                "<p>\n" +
                "逾期送达的或者未送达指定地点的询价文件，招标人不予受理。\n" +
                "</p>\n" +
                "<p>\n" +
                " * 、凡与本次采购项目有关的事宜请按下述地址联系\n" +
                "</p>\n" +
                "<p>\n" +
                "招标人信息：\n" +
                "</p>\n" +
                "<p>\n" +
                "名称： (略) 市江源区委办\n" +
                "</p>\n" +
                "<p>\n" +
                "联系人：刘先生 电话： *** \n" +
                "</p>\n" +
                "<p>\n" +
                "采购代理机构： (略) 市江源 (略) \n" +
                "</p>\n" +
                "<p>\n" +
                "地址： (略) 科\n" +
                "</p>\n" +
                "<p>\n" +
                "联系人：狄女士电话： *** \n" +
                "</p>\n" +
                "<p>\n" +
                " (略) 市江源 (略) \n" +
                "</p>\n" +
                "<p>\n" +
                " * 日\n" +
                "</p>\n" +
                "<div>\n" +
                "<img src=\"data:image/gif;base * ,R0lGODlhoACgAMQAAAAAAP////8AAP8BAf9WSf8wK/ * Mv9AN/9KQv8AAP8CAvwCAv8EA/8EBPkEBP8FBfwGBf8HB/8KCfwKCfUKCf8MC/8PDvkSEP8TEv8XFf8cGf8fHv8lIf8pJv9BP////yH5BAEAAB8ALAAAAACgAKAAAAX/4CeOZGmeaKqup+d1BVdsG2wUCKvvfO//wJ6Bs7E8FAOBcslcDhiMyAPT8QSv2KwW6Kk0v+Bw * HJbc/otK4gUYi/g++jigw8KuJhz8nenwUEkFhjxSYdCAwUI2JxlYMOjQQdwICWZ0/++AYt++8HFJWxapUq//+jJihb * hoyAToKHgBVNnZWWRCg/+AiDwQI8nKDjCALooIaUIizHB1DF3LTGdCgJ+EJhBShwqTAp+jmBBBmIBeBYCborghx8YSWaGXKIp0ZkJ4CiR3DE3JqSSMQRCk+/SWbc5ZVHxJiUoZglYCATAg * +Y0oKKUgi6K2MLuhDgh * AUB1gnAVAMw0iZAal5+/3ELDYqR+sxMO6JSIglaspsMiEO8rIvEJFDqeRqBI5C4vlnW2+uBIUSlBMQioWDEoCcj2XEA9+/g9hL5bjG5ElL3MA0CDKVCyb7rWHCA/0MGUvWgBwgQYl////+Ebgtl+hgAv1IRDwSpO5hSlAWGE3VAxkxAZYfCAYKqKJE7ikBndQAI6+/+FiCh1TTGoMyQDY2sZJ5brR1GzBASA/+AACHizmy9hAmGLJlVKdZUFH/lsHoIiUXh8LAvjfJ8JtrSys8j1BG5AKSOU8twzrGSs//9pVS8dBZiJndDshwUVORCUDlsQL * Cnm/KHVS4RKUCR4iNgqy6m1IIBiznXOPm6CDMPOkhFi4oPMDOCQi/xVeCOAkyhQyd * JUEB2cihWQuHgX0UNw/MXGwjDBDaYeVAadyLVxs5sQE * Se5wkmVKePRgEGkQrh/8qEtwDBbLQhDXBVgjgZzBLFEcMJwTShNEwYFZKDcjE+jkaYtCEhOZHCAHnHMQUkMICUvMbMxZ5FFgt * KEjgoGR1sP++///fKKexSsgkNgLoI3+mojlzboz+QJQYuh+++/OzbYvGu5hYCUQMIMiqMJ6LeDbUU4JRAPuAA3F0F4PD9AwnMdN/+XB//8RB/IWtt * z/++/FhCKWoiNJDBEAnQFhZQXiWaV8AIJA0AZCqKTCTRnd+cEV5BGS/RiZHkChuFAryMC3TN6++/gBzGQ2///pGf1AGHxgNni0A5dYlSZgD9d2bcVyBBMaoh+/w4PKIQCCpQK1ZKDWUVo3CnBL * pUjBLB////ORLgUa/v4D7depQGcY+xFwyIAW8qYIp9Sg0AayosRAAI4AEJWQJ0uBscQXPiylNI * /+0LARMI7rlCMeEAH++TD/taBKtIJ/5doDGtAPemIGCBJvsFMvouRV/DZCPtABOehNVZpTFzQp1qQ/6iYkFIklj/+/+=\">\n" +
                "</div>\n" +
                "</a></div><a href=\"/zhuce.html?id=news\" target=\"_blank\" oldhref=\"http:/ *** \">\n" +
                "</a></div><a href=\"/zhuce.html?id=news\" target=\"_blank\" oldhref=\"http:/ *** \">\n" +
                "<em style=\"display:none\" class=\"oss\">119</em></a>";
        ResponseModel<List<String>> resp =tenderElasticService.search(words, title, content);
        System.out.println(new Gson().toJson(resp));
    }

    @Test
    public void batchInsertTest() throws InterruptedException {
        String path = "C:\\Users\\Administrator\\Desktop\\rpa招标关键词.txt";
        List<String> lines = ReadFromFile.readAllFile(path);

        AtomicLong index = new AtomicLong(1);
        List<TenderKeywordEsDto> tenderList = lines.stream().filter(StringUtils::isNotBlank)
                .map(line -> {
                    String[] info = line.split("\t");
                    TenderKeywordEsDto dto = new TenderKeywordEsDto();
                    dto.setId(index.getAndIncrement());
                    dto.setTag(info[0]);
                    if(info.length>1){
                        dto.setClassify(info[info.length-1]);
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        tenderElasticService.batchInsert(tenderList);

        Thread.sleep(5*1000);
    }

    @Test
    public void tt(){
        System.out.println(null==null);
        System.out.println(null=="a");
        System.out.println(null==new Integer(23));
        System.out.println(new Integer(23)==new Integer(23));
        System.out.println(new Integer(23).equals(new Integer(23)));
    }

}
