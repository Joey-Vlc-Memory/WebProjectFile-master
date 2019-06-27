package com.osyunge.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbItemParamItemMapper;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItemParamItem;
import com.osyunge.dataobject.TbItemParamItemExample;
import com.osyunge.service.ItemParamService;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEMPARAM_KEY}")
    private String REDIS_ITEMPARAM_KEY;

    @Value("${REDIS_ITEMPARAM_EXPIRE}")
    private Integer REDIS_ITEMPARAM_EXPIRE;

    @Override
    public FCResult getItemParam(Long itemId) {


        try {

            String result = jedisClient.get(REDIS_ITEMPARAM_KEY + ":" + itemId + ":base");

            if (!StringUtils.isEmpty(result)){
                return JsonUtils.jsonToPojo(result,FCResult.class);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();

        criteria.andItemIdEqualTo(itemId);

        List<TbItemParamItem> itemParamItemList = itemParamItemMapper.selectByExampleWithBLOBs(example);

        TbItemParamItem itemParamItem;

        if (itemParamItemList != null && itemParamItemList.size() > 0){
            itemParamItem = itemParamItemList.get(0);

            try {

                jedisClient.set(REDIS_ITEMPARAM_KEY + ":" + itemId + ":base",JsonUtils.objectToJson(FCResult.ok(itemParamItem)));
                jedisClient.expire(REDIS_ITEMPARAM_KEY + ":" + itemId + ":base",REDIS_ITEMPARAM_EXPIRE);

            }catch (Exception e){
                e.printStackTrace();
            }
            return FCResult.ok(itemParamItem);
        }

       return FCResult.build(400,"无该商品规格");
    }
}
