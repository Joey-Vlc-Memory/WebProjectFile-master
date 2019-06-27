package com.osyunge.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {

    //商品列表
    private List<Item> itemList;

    //总记录数
    private Long recordCount;

    //总页数
    private Long pageCount;

    //当前页
    private Long curPage;

}
