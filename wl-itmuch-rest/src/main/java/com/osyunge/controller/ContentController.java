package com.osyunge.controller;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbContent;
import com.osyunge.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/rest/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/{cid}")
    @ResponseBody
    public FCResult getContentList(@PathVariable Long cid) {
        try {
            List<TbContent> list = contentService.getContentList(cid);
            return FCResult.ok(list);
        }catch (Exception e){
            e.printStackTrace();
            return FCResult.build(500,"服务器内部错误！");
        }

    }

}
