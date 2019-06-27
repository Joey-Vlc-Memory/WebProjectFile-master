package com.osyunge.service;

import com.osyunge.dataobject.EasyUIDataGridResult;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbItem;

public interface ItemService {

    TbItem getItemById(Long itemId);

    FCResult saveItem(TbItem item ,String desc,String paramData);

    EasyUIDataGridResult getItemList(int page, int rows);

    FCResult getItemDescById(Long id);

    FCResult getItemParamItemById(Long id);

    FCResult updateItem(TbItem item,String desc,String itemParams,Long itemParamId);

    FCResult deleteItem(Long[] ids);

    FCResult instockItem(Long[] ids);

    FCResult reshelfItem(Long[] ids);

}
