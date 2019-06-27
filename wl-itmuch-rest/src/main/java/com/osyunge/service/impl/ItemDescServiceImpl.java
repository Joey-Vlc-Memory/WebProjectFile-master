package com.osyunge.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbItemDescMapper;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItemDesc;
import com.osyunge.dataobject.TbItemDescExample;
import com.osyunge.service.ItemDescService;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemDescServiceImpl implements ItemDescService {

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEMDECS_KEY}")
    private String REDIS_ITEMDECS_KEY;

    @Value("${REDIS_ITEMDECS_EXPIRE}")
    private Integer REDIS_ITEMDECS_EXPIRE;

    @Override
    public FCResult getItemDesc(Long itemId) {

        try {

            String result = jedisClient.get(REDIS_ITEMDECS_KEY + ":" + itemId + ":base");

            if (!StringUtils.isEmpty(result)){
                return JsonUtils.jsonToPojo(result,FCResult.class);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        TbItemDescExample example = new TbItemDescExample();
        TbItemDescExample.Criteria criteria = example.createCriteria();

        criteria.andItemIdEqualTo(itemId);

        List<TbItemDesc> itemDescList = itemDescMapper.selectByExampleWithBLOBs(example);

        TbItemDesc itemDesc = null;

        if (itemDescList != null && itemDescList.size() > 0){
            itemDesc = itemDescList.get(0);

            try {

                jedisClient.set(REDIS_ITEMDECS_KEY + ":" + itemId + ":base",JsonUtils.objectToJson(FCResult.ok(itemDesc)));
                jedisClient.expire(REDIS_ITEMDECS_KEY + ":" + itemId + ":base",REDIS_ITEMDECS_EXPIRE);

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return FCResult.ok(itemDesc);
    }
}
