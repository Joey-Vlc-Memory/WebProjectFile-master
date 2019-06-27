package com.osyunge.search.dao.impl;

import com.osyunge.dao.TbItemMapper;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.Item;
import com.osyunge.search.dao.SearchDao;
import com.osyunge.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public SearchResult search(SolrQuery query) throws Exception {

        //返回值对象
        SearchResult result = new SearchResult();

        //根据查询条件查询索引库
        QueryResponse queryResponse = solrServer.query(query);

        //取查询结果
        SolrDocumentList solrDocuments = queryResponse.getResults();

        //取查询结果总数量
        result.setRecordCount(solrDocuments.getNumFound());

        //商品列表
        List<Item> itemList = new ArrayList<>();

        //取高亮显示
        Map<String, Map<String, List<String>>> highLighting = queryResponse.getHighlighting();

        //取商品列表
        for (SolrDocument solrDocument : solrDocuments) {
            //创建一商品对象
            Item item = new Item();

            item.setId((String) solrDocument.get("id"));
            //取高亮显示的结果
            List<String> list = highLighting.get(solrDocument.get("id")).get("item_title");

            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            //添加至商品列表

            itemList.add(item);
        }

        result.setItemList(itemList);

        return result;
    }

    @Override
    public FCResult updateItemById(Long itemId) throws Exception {

        //调用方法

        Item item = itemMapper.getItemById(itemId);

        //创建solrDocument对象
        SolrInputDocument document = new SolrInputDocument();

        //添加数据
        document.addField("id", item.getId());
        document.addField("item_title", item.getTitle());
        document.addField("item_sell_point", item.getSell_point());
        document.addField("item_price", item.getPrice());
        document.addField("item_image", item.getImage());
        document.addField("item_category_name", item.getCategory_name());
        document.addField("item_desc", item.getItem_desc());

        //向solr索引库增加商品信息
        solrServer.add(document);
        solrServer.commit();

        return FCResult.ok();
    }

    @Override
    public FCResult deleteItemById(Long id) throws Exception {

        //通过id删除索引库中的商品信息
        solrServer.deleteById(id+"");
        solrServer.commit();

        return FCResult.ok();
    }


}
