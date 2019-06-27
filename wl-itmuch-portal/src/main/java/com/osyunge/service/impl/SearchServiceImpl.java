package com.osyunge.service.impl;

import com.osyunge.dataobject.FCResult;
import com.osyunge.pojo.SearchResult;
import com.osyunge.service.SearchService;
import com.osyunge.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Value("${SEARCH_BASE_URL}")
    private String SEARCH_BASE_URL;

    @Override
    public SearchResult search(String queryString, int page) {

        //调用search服务
        Map<String,String> param = new HashMap<>();

        param.put("q",queryString);
        param.put("page",page + "");

        try {

            //调用服务
            String json = HttpClientUtil.doGet(SEARCH_BASE_URL,param);

            if (!StringUtils.isEmpty(json)){

                FCResult fcResult = FCResult.formatToPojo(json,SearchResult.class);
                if (fcResult.getStatus() == 200){

                    return (SearchResult) fcResult.getData();
                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
