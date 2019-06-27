package com.osyunge.controller;

import com.osyunge.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item/desc")
public class ItemDescController {

    @Autowired
    private ItemDescService itemDescService;

    @RequestMapping(value = "/{itemId}.html",produces = { "text/html;charset=UTF-8;", "application/json;charset=UTF-8;"})
    @ResponseBody
    public String getItemDesc(@PathVariable Long itemId){
        return itemDescService.getItemDesc(itemId);
    }

}
