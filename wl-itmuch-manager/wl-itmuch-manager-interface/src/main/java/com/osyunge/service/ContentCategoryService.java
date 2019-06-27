package com.osyunge.service;

import com.osyunge.dataobject.EasyUITreeNode;
import com.osyunge.dataobject.FCResult;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCat(Long parentId);

    FCResult createContentCat(Long parentId,String name);

    FCResult updateContentCat(Long id,String name);

    FCResult deleteContentCat(Long id);
}
