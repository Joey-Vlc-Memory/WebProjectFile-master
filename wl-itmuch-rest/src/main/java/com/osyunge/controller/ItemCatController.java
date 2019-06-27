package com.osyunge.controller;

import com.osyunge.pojo.ItemCatResult;
import com.osyunge.service.ItemCatService;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest/itemcat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value = "/all",produces = MediaType.APPLICATION_JSON_VALUE+";charset = utf-8")
    @ResponseBody
    public String getItemCatList(String callback){
        ItemCatResult result = itemCatService.getItemCatList();

        if (StringUtils.isEmpty(callback)){
            return JsonUtils.objectToJson(result);
        }

        //如果callback不如空
        String json = JsonUtils.objectToJson(result);
        return callback + "(" + json+ ")";

    }

}
