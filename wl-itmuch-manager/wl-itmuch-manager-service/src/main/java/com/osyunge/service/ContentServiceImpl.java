package com.osyunge.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.osyunge.dao.TbContentMapper;
import com.osyunge.dataobject.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public EasyUIDataGridResult getContentList(Long categoryId, int page, int rows) {


        PageHelper.startPage(page, rows);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();

        criteria.andCategoryIdEqualTo(categoryId);

        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);

        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();

        result.setRows(list);
        result.setTotal(pageInfo.getTotal());

        return result;
    }

    @Override
    public FCResult saveContent(TbContent content) {

        if (content != null) {
            Date date = new Date();
            //为content实体类增添数据
            content.setCreated(date);
            content.setUpdated(date);


            //保存数据
            contentMapper.insertSelective(content);

        }

        return FCResult.ok();
    }

    @Override
    public FCResult updateContent(TbContent content) {

        if (content != null) {

            Date date = new Date();

            content.setUpdated(date);

            //查找未更新之前的数据
            TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
            content.setCreated(tbContent.getCreated());

            //更新数据
            contentMapper.updateByPrimaryKeyWithBLOBs(content);

        }

        return FCResult.ok();
    }

    @Override
    @Transactional
    public FCResult deleteContent(Long[] ids) {

        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                contentMapper.deleteByPrimaryKey(id);
            }
        }

        return FCResult.ok();
    }
}
