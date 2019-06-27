package com.osyunge.controller;

import com.osyunge.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/")
    public String getIndex(Model model){

        model.addAttribute("ad1",contentService.getAd1List());

        return "index";
    }

}
