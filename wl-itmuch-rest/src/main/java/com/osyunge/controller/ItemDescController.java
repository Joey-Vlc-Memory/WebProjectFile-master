package com.osyunge.controller;

import com.osyunge.dataobject.FCResult;
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

    @RequestMapping("/{itemId}")
    @ResponseBody
    public FCResult getItemDesc(@PathVariable Long itemId){
        return itemDescService.getItemDesc(itemId);
    }

}
