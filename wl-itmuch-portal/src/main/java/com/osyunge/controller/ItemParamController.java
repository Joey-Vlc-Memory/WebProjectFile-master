package com.osyunge.controller;

import com.osyunge.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item/param")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping(value = "/{itemId}.html",produces = { "text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String getItemParam(@PathVariable Long itemId){
        return itemParamService.getItemParam(itemId);
    }
}
