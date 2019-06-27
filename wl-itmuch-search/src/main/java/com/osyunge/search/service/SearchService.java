package com.osyunge.search.service;

import com.osyunge.dataobject.FCResult;
import com.osyunge.search.pojo.SearchResult;

public interface SearchService {

    SearchResult search(String queryString,int page ,int rows) throws Exception;

    FCResult updateItemById(Long itemId) throws Exception;

    FCResult deleteItemById(Long id) throws Exception;

}
