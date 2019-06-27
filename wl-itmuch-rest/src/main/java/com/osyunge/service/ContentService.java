package com.osyunge.service;

import com.osyunge.dataobject.TbContent;

import java.util.List;

public interface ContentService {

    List<TbContent> getContentList(Long cid);

}
