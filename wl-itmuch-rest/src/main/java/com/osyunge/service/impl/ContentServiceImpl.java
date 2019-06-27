package com.osyunge.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbContentMapper;
import com.osyunge.dataobject.TbContent;
import com.osyunge.dataobject.TbContentExample;
import com.osyunge.service.ContentService;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_CONTENT_KEY}")
    private String REDIS_CONTENT_KEY;

    @Value("${REDIS_CONTENT_EXPIRE}")
    private Integer REDIS_CONTENT_EXPIRE;

    @Override
    public List<TbContent> getContentList(Long cid) {

        try {

            String result = jedisClient.get(REDIS_CONTENT_KEY + ":" + cid + ":base");

            if (!StringUtils.isEmpty(result)){
                return JsonUtils.jsonToList(result,TbContent.class);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //根据cid查询内容列表
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();

        criteria.andCategoryIdEqualTo(cid);

        List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(example);

        try {

            jedisClient.set(REDIS_CONTENT_KEY + ":" + cid + ":base",JsonUtils.objectToJson(contentList));
            jedisClient.expire(REDIS_CONTENT_KEY + ":" + cid + ":base",REDIS_CONTENT_EXPIRE);

        }catch (Exception e){
            e.printStackTrace();
        }

        return contentList;
    }
}
