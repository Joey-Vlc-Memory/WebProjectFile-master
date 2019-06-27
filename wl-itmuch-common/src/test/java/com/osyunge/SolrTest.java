package com.osyunge;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrTest {

    @Test
    public void addDocument() throws Exception{
        //创建连接
        SolrServer solrServer = new HttpSolrServer("http://47.103.16.213:8081/solr");

        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();

        document.addField("id","test");
        document.addField("item_title","测试");
        document.addField("item_price",1000);

        //写入索引库
        solrServer.add(document);

        solrServer.commit();

    }


    @Test
    public void demo01(){

        String str = "12345#123";

        System.out.println(str.substring(0,str.lastIndexOf("#")));

    }

}
