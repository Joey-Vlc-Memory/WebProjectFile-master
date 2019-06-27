package com.osyunge.search.controller;

import com.osyunge.dataobject.FCResult;
import com.osyunge.search.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/search/manager")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/importall")
    @ResponseBody
    public FCResult importAll(){
        return itemService.importAllItems();
    }

}
