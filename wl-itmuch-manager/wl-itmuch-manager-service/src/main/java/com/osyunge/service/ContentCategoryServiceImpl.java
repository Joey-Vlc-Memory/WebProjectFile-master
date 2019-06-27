package com.osyunge.service;

import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbContentCategoryMapper;
import com.osyunge.dao.TbContentMapper;
import com.osyunge.dataobject.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public List<EasyUITreeNode> getContentCat(Long parentId) {


        //根据parentId查询
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();

        criteria.andParentIdEqualTo(parentId);

        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);

        List<EasyUITreeNode> result = new ArrayList<>();

        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();

            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");

            result.add(node);
        }

        return result;
    }

    @Override
    public FCResult createContentCat(Long parentId, String name) {

        //新建一个contentCategory实体类
        TbContentCategory contentCategory = new TbContentCategory();

        //更新数据

        Date date = new Date();
        contentCategory.setCreated(date);
        contentCategory.setUpdated(date);

        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        contentCategory.setIsParent(false);

        contentCategory.setName(name);
        contentCategory.setParentId(parentId);

        //在父目录之下添加子节点，将父目录的属性进行更新
        TbContentCategory parentCon = contentCategoryMapper.selectByPrimaryKey(parentId);
        parentCon.setIsParent(true);
        contentCategoryMapper.updateByPrimaryKeySelective(parentCon);

        //进行插入操作
        int result = contentCategoryMapper.insert(contentCategory);

        if (result < 1){
            return null;
        }

        return FCResult.ok(contentCategory);
    }

    @Override
    public FCResult updateContentCat(Long id, String name) {

        //查找出要更新的contentCategory
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setUpdated(new Date());

        if (name != null){
            contentCategory.setName(name);
        }
        //执行更新操作
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        return FCResult.ok();
    }

    @Override
    public FCResult deleteContentCat(Long id) {

        //如果该分类下有子节点
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        if (contentCategory.getIsParent()){
            return null;
        }

        //如果该分类下有具体内容
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(id);

        List<TbContent> list = contentMapper.selectByExample(example);

        if (list != null&& list.size() > 0){
            return null;
        }

        //进行删除操作
        TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategoryMapper.deleteByPrimaryKey(id);

        TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria1 = contentCategoryExample.createCriteria();
        criteria1.andParentIdEqualTo(category.getParentId());

        List<TbContentCategory> list1 = contentCategoryMapper.selectByExample(contentCategoryExample);

        if (list1 == null || list1.size() <= 0){
            TbContentCategory parentCon = contentCategoryMapper.selectByPrimaryKey(category.getParentId());
            parentCon.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parentCon);
        }

        return FCResult.ok();
    }

}
