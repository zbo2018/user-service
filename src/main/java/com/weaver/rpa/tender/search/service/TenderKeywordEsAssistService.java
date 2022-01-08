package com.weaver.rpa.tender.search.service;

import com.weaver.rpa.tender.search.model.CorpusTermVO;
import com.weaver.rpa.tender.search.model.TenderKeywordEsDto;
import com.weaver.rpa.tender.search.util.ResponseModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TenderKeywordEsAssistService {
    List<Map<String, Object>> viewAnalyzerTerms(String analyzer, String content);

    ResponseModel<List<Boolean>> termMatchBaseWord(String appId, List<CorpusTermVO> termList);

    ResponseModel<Integer> maxLength(String appId);

    ResponseModel<List<TenderKeywordEsDto>> search(String content);

    ResponseModel<Boolean> existsDomain(List<String> words);

    boolean batchInsert(List<TenderKeywordEsDto> tenderList);

    ResponseModel<Boolean> existsSingleWord(String appId, Map<String, Boolean> singleWordMap);

    ResponseModel<Boolean> excludeBaseKeyword(Map<String, Integer> newWords);

    ResponseModel<Integer> totalKeywords(String appId, String classify);

    ResponseModel<TenderKeywordEsDto> nextDoc(Long befOrder);

    ResponseModel<Map<String, Integer[]>> checkKeywordsFreq(String appId, List<String> terms, String classify);

    ResponseModel<Boolean> updateTagLowFreq(String appId, String docId, String val);

    ResponseModel<Boolean> indexRefresh(String appId);
}
