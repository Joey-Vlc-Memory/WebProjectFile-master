package com.osyunge.service.impl;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItem;
import com.osyunge.pojo.ItemPlus;
import com.osyunge.service.ItemService;
import com.osyunge.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ItemServiceImpl implements ItemService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;

    @Value("${REST_ITEM_INFO}")
    private String REST_ITEM_INFO;

    @Override
    public TbItem getItemById(Long itemId) {

        try {

            String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_INFO + itemId);

            if (!StringUtils.isEmpty(json)){
                FCResult result = FCResult.formatToPojo(json, ItemPlus.class);

                if (result.getStatus() == 200){
                    return (TbItem) result.getData();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
