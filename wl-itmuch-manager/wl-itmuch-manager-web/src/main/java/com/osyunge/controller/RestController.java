package com.osyunge.controller;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItem;
import com.osyunge.dataobject.TbItemDesc;
import com.osyunge.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 对商品进行操作
 */
@Controller
@RequestMapping("/rest")
public class RestController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/page/item-edit",method = RequestMethod.GET)
    public String getItemEdit(){
        return "item-edit";
    }

    @RequestMapping("/item/query/item/desc/{id}")
    @ResponseBody
    public FCResult getItemDesc(@PathVariable Long id) {

        return itemService.getItemDescById(id);
    }

    @RequestMapping("/item/param/item/query/{id}")
    @ResponseBody
    public FCResult getItemParam(@PathVariable Long id){
        return itemService.getItemParamItemById(id);
    }

    @RequestMapping("/item/update")
    @ResponseBody
    public FCResult updateItem(TbItem item,String desc,String itemParams,Long itemParamId){
        return itemService.updateItem(item,desc,itemParams,itemParamId);
    }

    @RequestMapping("/item/delete")
    @ResponseBody
    public FCResult deleteItem(Long[] ids){
        return itemService.deleteItem(ids);
    }

    @RequestMapping("/item/instock")
    @ResponseBody
    public FCResult instockItem(Long[] ids){
        return itemService.instockItem(ids);
    }


    @RequestMapping("/item/reshelf")
    @ResponseBody
    public FCResult reshelfItem(Long[] ids){
        return itemService.reshelfItem(ids);
    }

}
