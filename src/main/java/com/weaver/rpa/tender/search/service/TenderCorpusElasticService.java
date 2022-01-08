package com.weaver.rpa.tender.search.service;

import com.weaver.rpa.tender.search.model.NewWordDto;
import com.weaver.rpa.tender.search.util.ResponseModel;

import java.util.List;
import java.util.Map;

public interface TenderCorpusElasticService {
    ResponseModel<Map<String, Integer>> findNewWords();

    ResponseModel<List<NewWordDto>> filterNewWords(Map<String, Integer> newWordMap);

    ResponseModel<Map<String, Integer>> findNewWordsByNgramAndDocFreq();
}
