package com.osyunge.service;

import com.osyunge.dataobject.EasyUIDataGridResult;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbContent;

public interface ContentService {

    EasyUIDataGridResult getContentList(Long categoryId, int page, int rows);

    FCResult saveContent(TbContent content);

    FCResult updateContent(TbContent content);

    FCResult deleteContent(Long[] ids);

}
