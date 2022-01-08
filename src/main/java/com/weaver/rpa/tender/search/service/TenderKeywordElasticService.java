package com.weaver.rpa.tender.search.service;

import com.weaver.rpa.tender.search.model.TenderKeywordEsDto;
import com.weaver.rpa.tender.search.util.ResponseModel;

import java.util.List;

public interface TenderKeywordElasticService {
    ResponseModel<List<String>> search(String words, String title, String content);

    ResponseModel<Boolean> batchInsert(List<TenderKeywordEsDto> tenderList);
}
