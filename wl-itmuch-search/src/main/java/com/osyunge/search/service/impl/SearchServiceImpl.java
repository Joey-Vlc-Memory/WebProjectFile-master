package com.osyunge.search.service.impl;

import com.osyunge.dataobject.FCResult;
import com.osyunge.search.dao.SearchDao;
import com.osyunge.search.pojo.SearchResult;
import com.osyunge.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String queryString, int page, int rows) throws Exception {

        //创建查询对象
        SolrQuery query = new SolrQuery();

        //设置查询条件
        query.setQuery(queryString);

        //设置分页
        query.setStart((page - 1) * rows);
        query.setRows(rows);

        //设置默认搜索域
        query.set("df","item_keywords");

        //设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style =\"color:red\">");
        query.setHighlightSimplePost("</em>");

        //执行查询
        SearchResult result = searchDao.search(query);

        long recordCount = result.getRecordCount();

        long pageCount = recordCount / rows;

        if (recordCount % rows > 0){
            pageCount++;
        }
        result.setPageCount(pageCount);
        result.setCurPage((long) page);

        return result;
    }

    @Override
    public FCResult updateItemById(Long itemId) throws Exception {

        return searchDao.updateItemById(itemId);
    }

    @Override
    public FCResult deleteItemById(Long id) throws Exception {
        return searchDao.deleteItemById(id);
    }
}
