package com.osyunge.controller;

import com.osyunge.dataobject.EasyUIDataGridResult;
import com.osyunge.dataobject.FCResult;
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

    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getItemParam(Integer page,Integer rows){
        return itemParamService.getItemParam(page,rows);
    }

    @RequestMapping("/add")
    public String getItemParamAdd(){
        return "item-param-add";
    }

    @RequestMapping("/query/itemcatid/{id}")
    @ResponseBody
    public FCResult getItemCat(@PathVariable Long id){
        return itemParamService.getItemCat(id);
    }

    @RequestMapping("/save/{cid}")
    @ResponseBody
    public FCResult insertItemParam(@PathVariable Long cid,String paramData){
        return itemParamService.insertItemParam(cid,paramData);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public FCResult deleteItemParam(Long[] ids){
        return itemParamService.deleteItemParam(ids);
    }

}
