package com.osyunge.service.impl;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItemDesc;
import com.osyunge.service.ItemDescService;
import com.osyunge.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ItemDescServiceImpl implements ItemDescService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;

    @Value("${REST_ITEM_DESC}")
    private String REST_ITEM_DESC;

    @Override
    public String getItemDesc(Long itemId) {

        try {

            String json = HttpClientUtil.doGet(REST_BASE_URL + REST_ITEM_DESC + itemId);

            FCResult result = FCResult.formatToPojo(json, TbItemDesc.class);

           if (result.getStatus() == 200){
               TbItemDesc itemDesc = (TbItemDesc) result.getData();


               return itemDesc.getItemDesc();
           }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
