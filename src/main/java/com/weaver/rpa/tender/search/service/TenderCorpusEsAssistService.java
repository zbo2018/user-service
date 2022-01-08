package com.weaver.rpa.tender.search.service;

import com.weaver.rpa.tender.search.model.TenderCorpusEsDto;
import com.weaver.rpa.tender.search.util.ResponseModel;

import java.util.List;
import java.util.Map;

public interface TenderCorpusEsAssistService {
    List<Map<String, Object>> viewAnalyzerTerms(String analyzer, String content);

    ResponseModel<TenderCorpusEsDto> nextDoc(Long befOrder);

    boolean batchInsert(List<TenderCorpusEsDto> corpusList);

    ResponseModel<Integer> totalCorpus(String appId);

    ResponseModel<Map<String, Integer>> checkWordsFreq(String appId, List<String> joinWords, int minFreq);
}
