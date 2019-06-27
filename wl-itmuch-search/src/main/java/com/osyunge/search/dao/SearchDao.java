package com.osyunge.search.dao;

import com.osyunge.dataobject.FCResult;
import com.osyunge.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;

public interface SearchDao {

    SearchResult search(SolrQuery query) throws Exception;

    FCResult updateItemById(Long itemId) throws Exception;

    FCResult deleteItemById(Long id) throws Exception;
}
