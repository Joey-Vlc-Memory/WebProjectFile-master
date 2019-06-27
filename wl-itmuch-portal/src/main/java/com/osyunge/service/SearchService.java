package com.osyunge.service;

import com.osyunge.pojo.SearchResult;

public interface SearchService {

    SearchResult search(String queryString,int page);

}
