package com.osyunge.service;

import com.osyunge.dataobject.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {

    List<EasyUITreeNode> getItemCatList(long parentId);

}
