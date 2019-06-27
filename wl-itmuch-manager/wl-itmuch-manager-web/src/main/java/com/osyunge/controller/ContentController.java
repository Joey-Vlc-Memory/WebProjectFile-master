package com.osyunge.controller;

import com.osyunge.dataobject.EasyUIDataGridResult;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbContent;
import com.osyunge.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(Long categoryId,int page,int rows){
        return contentService.getContentList(categoryId, page, rows);
    }

    @RequestMapping("/content/save")
    @ResponseBody
    public FCResult saveContent(TbContent content){
        return contentService.saveContent(content);
    }

    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public FCResult updateContent(TbContent content){
        return contentService.updateContent(content);
    }


    @RequestMapping("/content/delete")
    @ResponseBody
    public FCResult deleteContent(Long[] ids){
        return contentService.deleteContent(ids);
    }

}
