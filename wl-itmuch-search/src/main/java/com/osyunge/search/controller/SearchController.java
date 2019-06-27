package com.osyunge.search.controller;

import com.alibaba.druid.util.StringUtils;
import com.osyunge.dataobject.FCResult;
import com.osyunge.search.pojo.SearchResult;
import com.osyunge.search.service.SearchService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/query")
    @ResponseBody
    public FCResult search(@RequestParam("q") String queryString,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "60") Integer rows) {
        //查询条件不可为空
        if (StringUtils.isEmpty(queryString)){
            return FCResult.build(400,"查询条件不可为空");

        }
        SearchResult result;

        try {
            queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
            result = searchService.search("item_title:"+ queryString,page,rows);
        }catch (Exception e){
            e.printStackTrace();
            return FCResult.build(500, ExceptionUtils.getStackTrace(e));
        }
        return FCResult.ok(result);
    }

}
