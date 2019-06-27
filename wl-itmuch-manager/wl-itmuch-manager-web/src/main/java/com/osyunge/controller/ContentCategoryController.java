package com.osyunge.controller;

import com.osyunge.dataobject.EasyUITreeNode;
import com.osyunge.dataobject.FCResult;
import com.osyunge.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(value = "id",defaultValue = "0") Long parentId){
        return contentCategoryService.getContentCat(parentId);
    }

    @RequestMapping("/create")
    @ResponseBody
    public FCResult createContentCat(Long parentId,String name){
        return contentCategoryService.createContentCat(parentId,name);
    }

    @RequestMapping("/update")
    @ResponseBody
    public FCResult updateContentCat(Long id,String name){
        return contentCategoryService.updateContentCat(id, name);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public FCResult deleteContent(Long id){
        return contentCategoryService.deleteContentCat(id);
    }

}
