package com.osyunge.search.service.impl;

import com.osyunge.dao.TbItemMapper;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.Item;
import com.osyunge.search.service.ItemService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;


    @Override
    public FCResult importAllItems() {

        try {
            List<Item> list = itemMapper.getItemMapper();

            //将获取到的商品信息放入索引库中
            for (Item item : list) {
                //创建一个SolrInputDocument对象
                SolrInputDocument document = new SolrInputDocument();

                document.setField("id", item.getId());
                document.setField("item_title", item.getTitle());
                document.setField("item_sell_point", item.getSell_point());
                document.setField("item_price", item.getPrice());
                document.setField("item_image", item.getImage());
                document.setField("item_category_name", item.getCategory_name());
                document.setField("item_desc", item.getItem_desc());

                solrServer.add(document);
            }

            //更新提交
            solrServer.commit();

        }catch (Exception e){
            e.printStackTrace();
            return FCResult.build(500, ExceptionUtils.getStackTrace(e));
        }

        return FCResult.ok();
    }
}
