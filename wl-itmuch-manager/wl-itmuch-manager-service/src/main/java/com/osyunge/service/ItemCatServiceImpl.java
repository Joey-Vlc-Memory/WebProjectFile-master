package com.osyunge.service;


import com.osyunge.dao.TbItemCatMapper;
import com.osyunge.dataobject.EasyUITreeNode;
import com.osyunge.dataobject.TbItemCat;
import com.osyunge.dataobject.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {


        //根据parentId查询产品分类
        TbItemCatExample example = new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();

        criteria.andParentIdEqualTo(parentId);

        //执行条件
        List<TbItemCat> list = itemCatMapper.selectByExample(example);

        //转换成EasyUITreeNode列表
        List<EasyUITreeNode> resultList = new ArrayList<>();

        for (TbItemCat tbItemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            //添加至列表中
            resultList.add(node);
        }

        return resultList;
    }
}
