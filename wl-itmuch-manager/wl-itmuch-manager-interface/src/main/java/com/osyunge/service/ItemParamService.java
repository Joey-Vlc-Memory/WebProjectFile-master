package com.osyunge.service;

import com.osyunge.dataobject.EasyUIDataGridResult;
import com.osyunge.dataobject.FCResult;


public interface ItemParamService {

    EasyUIDataGridResult getItemParam(int page, int rows);

    FCResult getItemCat(Long id);

    FCResult insertItemParam(Long cid, String paramData);

    FCResult deleteItemParam(Long[] ids);

}
