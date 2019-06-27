package com.osyunge.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbItemMapper;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItem;
import com.osyunge.service.ItemService;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;

    @Value("${REDIS_ITEM_EXPIRE}")
    private Integer REDIS_ITEM_EXPIRE;

    @Override
    public FCResult getItemBaseInfo(Long itemId) {

        try {

            String result = jedisClient.get(REDIS_ITEM_KEY + ":" +itemId + ":base");

            if (!StringUtils.isEmpty(result)){

                return JsonUtils.jsonToPojo(result,FCResult.class);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        try {

            jedisClient.set(REDIS_ITEM_KEY + ":" +itemId + ":base",JsonUtils.objectToJson(FCResult.ok(item)));
            jedisClient.expire(REDIS_ITEM_KEY + ":" +itemId + ":base",REDIS_ITEM_EXPIRE);

        }catch (Exception e){
            e.printStackTrace();
        }

        return FCResult.ok(item);
    }
}
