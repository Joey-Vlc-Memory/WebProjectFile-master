package com.osyunge.service;

import com.osyunge.pojo.ItemCatResult;

import java.util.List;

public interface ItemCatService {

    ItemCatResult getItemCatList();

    List getItemCatList(Long parentId);

}
