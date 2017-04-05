package com.vk.tottenham.mybatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vk.tottenham.mybatis.mappers.DictionaryMapper;
import com.vk.tottenham.mybatis.service.DictionaryService;

@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public String translateInRussianNom(String term) {
        String result = dictionaryMapper.translateInRussianNom(term);
        return result != null ? result : term;
    }

}
