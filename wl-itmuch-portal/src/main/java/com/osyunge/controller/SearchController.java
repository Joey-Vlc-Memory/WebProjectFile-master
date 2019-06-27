package com.osyunge.controller;

import com.osyunge.pojo.SearchResult;
import com.osyunge.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class SearchController {


    @Autowired
    private SearchService searchService;

    @RequestMapping("search.html")
    public String search(@RequestParam("q") String queryString,
                         @RequestParam(defaultValue = "1") Integer page, Model model) {

        if (queryString != null){
            try {
                queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        SearchResult searchResult = searchService.search(queryString,page);

        //传递参数

        model.addAttribute("query",queryString);

        model.addAttribute("totalPages",searchResult.getPageCount());

        model.addAttribute("itemList",searchResult.getItemList());

        model.addAttribute("page",page);

        return "search";

    }

}
