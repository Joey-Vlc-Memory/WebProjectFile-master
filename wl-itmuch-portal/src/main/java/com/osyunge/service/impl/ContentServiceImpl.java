package com.osyunge.service.impl;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbContent;
import com.osyunge.pojo.AdNode;
import com.osyunge.service.ContentService;
import com.osyunge.utils.HttpClientUtil;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;

    @Value("${REST_CONTENT_URL}")
    private String REST_CONTENT_URL;

    @Value("${REST_CONTENT_AD1_CID}")
    private String REST_CONTENT_AD1_CID;

    @Override
    @SuppressWarnings("all")
    public String getAd1List() {

        String json = HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_URL + REST_CONTENT_AD1_CID);

        //将获取到的json数据转换成java实体类型
        FCResult result = FCResult.formatToList(json, TbContent.class);

        List<TbContent> contentList = (List<TbContent>) result.getData();

        List<AdNode> nodeList = new ArrayList<>();

        for (TbContent tbContent : contentList) {
            AdNode node = new AdNode();
            node.setHeight(240);
            node.setWidth(670);
            node.setSrc(tbContent.getPic());

            node.setHeightB(240);
            node.setWidthB(550);
            node.setSrcB(tbContent.getPic2());

            node.setAlt(tbContent.getSubTitle());
            node.setHref(tbContent.getUrl());

            nodeList.add(node);
        }

        return JsonUtils.objectToJson(nodeList);
    }
}
