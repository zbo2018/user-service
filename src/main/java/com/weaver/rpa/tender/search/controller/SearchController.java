package com.weaver.rpa.tender.search.controller;


import com.weaver.rpa.tender.search.service.TenderKeywordElasticService;
import com.weaver.rpa.tender.search.util.ResponseConstants;
import com.weaver.rpa.tender.search.util.ResponseModel;
import com.weaver.rpa.tender.search.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rpa/tender")
public class SearchController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private TenderKeywordElasticService elasticService;

    @GetMapping("/hello/{name}")
    @ResponseBody
    public String hello(@PathVariable("name") String name){
        List<String> result = new ArrayList<>();
        result.add(name);
        result.add("张三");
        ResponseModel<List<String>> resp = new ResponseModel<>(result);
        return ResponseUtil.getResponseJson(resp);
    }


    @PostMapping("/search")
    @ResponseBody
    public String search(@RequestBody Map<String, String> param){
        String words = param.get("words");
        String title = param.get("title");
        String content = param.get("content");
        if(StringUtils.isBlank(title) && StringUtils.isBlank(content)){
            return ResponseUtil.getErrorJson(ResponseConstants.PARAMETER_IS_LOSE.getCode(), ResponseConstants.PARAMETER_IS_LOSE.getMessage());
        }

        ResponseModel<List<String>> resp = elasticService.search(words, title, content);
        return ResponseUtil.getResponseJson(resp);
    }



}
