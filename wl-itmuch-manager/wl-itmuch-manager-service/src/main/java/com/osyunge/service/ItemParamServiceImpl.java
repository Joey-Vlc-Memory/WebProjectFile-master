package com.osyunge.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbItemParamMapper;
import com.osyunge.dataobject.*;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Transactional
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper itemParamMapper;


    @Override
    public EasyUIDataGridResult getItemParam(int page, int rows) {


        PageHelper.startPage(page,rows);

        TbItemParamExample example = new TbItemParamExample();

        List<TbItemParamPlus> list = itemParamMapper.getItemParamList(example);

        PageInfo<TbItemParamPlus> pageInfo = new PageInfo<>(list);

        EasyUIDataGridResult result = new EasyUIDataGridResult();

        result.setRows(list);
        result.setTotal(pageInfo.getTotal());


        return result;
     }

    @Override
    public FCResult getItemCat(Long id) {


        TbItemParamExample example = new TbItemParamExample();

        TbItemParamExample.Criteria criteria = example.createCriteria();

        criteria.andItemCatIdEqualTo(id);
        //查询该类目下是否有规格
        List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);

        if (list != null && list.size() > 0){
            TbItemParam itemParam = list.get(0);

            return FCResult.ok(itemParam);
        }

        return FCResult.ok();
    }

    @Override
    public FCResult insertItemParam(Long cid, String paramData) {

        //创建一个itemParam
        TbItemParam itemParam = new TbItemParam();
        //向itemParam添加数据
        itemParam.setItemCatId(cid);
        itemParam.setParamData(paramData);

        Date date = new Date();

        itemParam.setCreated(date);
        itemParam.setUpdated(date);

        itemParamMapper.insert(itemParam);

        return FCResult.ok();
    }

    @Override
    public FCResult deleteItemParam(Long[] ids) {

        if (ids != null && ids.length > 0){
            for (Long id : ids) {
                itemParamMapper.deleteByPrimaryKey(id);
            }
        }
        return FCResult.ok();
    }
}
