package com.osyunge.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.osyunge.dao.TbItemDescMapper;
import com.osyunge.dao.TbItemMapper;
import com.osyunge.dao.TbItemParamItemMapper;
import com.osyunge.dataobject.*;
import com.osyunge.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;


    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicDestination;

    @Override
    public TbItem getItemById(Long itemId) {


        TbItemExample example = new TbItemExample();

        //创建查询条件
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);

        List<TbItem> list = itemMapper.selectByExample(example);

        //判断list是否为空

        TbItem item = null;

        if (list != null && list.size() > 0) {
            item = list.get(0);
        }

        return item;
    }

    @Override
    public FCResult saveItem(TbItem item, String desc, String paramData) {

        //为item增添必要属性
        item.setId(IDUtils.genItemId());

        Date date = new Date();

        item.setCreated(date);
        item.setUpdated(date);

        item.setStatus((byte) 1);
        //执行插入操作
        itemMapper.insert(item);

        if (desc != null) {
            //创建一个itemDesc实体类
            TbItemDesc itemDesc = new TbItemDesc();

            //为itemDesc添加数据
            itemDesc.setItemId(item.getId());
            itemDesc.setItemDesc(desc);
            itemDesc.setUpdated(date);
            itemDesc.setCreated(date);

            //执行插入操作
            itemDescMapper.insertSelective(itemDesc);

        }

        if (paramData != null) {
            //创建一个itemParamItem实体类
            TbItemParamItem itemParamItem = new TbItemParamItem();

            //为itemParamItem增添数据
            itemParamItem.setItemId(item.getId());
            itemParamItem.setParamData(paramData);
            itemParamItem.setCreated(date);
            itemParamItem.setUpdated(date);

            //执行插入操作
            itemParamItemMapper.insert(itemParamItem);

        }

        //向ActiveMQ发送一个商品添加信息
        jmsTemplate.send(topicDestination, session -> session.createTextMessage(item.getId() + "#add"));


        return FCResult.ok();
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {

        //分页处理
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //获取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //返回处理结果
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);

        return result;
    }

    @Override
    public FCResult getItemDescById(Long id) {

        //创建查询条件
        TbItemDescExample example = new TbItemDescExample();

        TbItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(id);

        //查询商品详情
        List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);

        TbItemDesc itemDesc = null;

        if (list != null && list.size() > 0) {
            itemDesc = list.get(0);

        }

        return FCResult.ok(itemDesc);
    }

    @Override
    public FCResult getItemParamItemById(Long id) {

        //创建查询条件
        TbItemParamItemExample example = new TbItemParamItemExample();

        TbItemParamItemExample.Criteria criteria = example.createCriteria();

        criteria.andItemIdEqualTo(id);

        List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);

        TbItemParamItem itemParamItem = null;

        if (list != null && list.size() > 0) {
            itemParamItem = list.get(0);
        }

        return FCResult.ok(itemParamItem);
    }

    @Override
    public FCResult updateItem(TbItem item, String desc, String itemParams, Long itemParamId) {

        TbItem t = itemMapper.selectByPrimaryKey(item.getId());
        //更新数据

        Date date = new Date();

        t.setUpdated(date);
        t.setTitle(item.getTitle());
        t.setPrice(item.getPrice());
        t.setBarcode(item.getBarcode());
        t.setImage(item.getImage());
        t.setNum(item.getNum());
        t.setSellPoint(item.getSellPoint());

        //执行更新操作
        itemMapper.updateByPrimaryKeySelective(t);

        TbItemDesc desc1 = itemDescMapper.selectByPrimaryKey(item.getId());
        //更新数据
        desc1.setItemDesc(desc);
        desc1.setUpdated(date);

        //执行更新操作
        itemDescMapper.updateByPrimaryKeySelective(desc1);

        //如果接收到的规格参数不为空
        if (itemParams != null && itemParamId != null) {
            TbItemParamItem itemParamItem = itemParamItemMapper.selectByPrimaryKey(itemParamId);

            if (itemParamItem != null) {
                //更新数据
                itemParamItem.setParamData(itemParams);
                itemParamItem.setUpdated(date);

                //执行更新
                itemParamItemMapper.updateByPrimaryKeySelective(itemParamItem);

            }
        }

        jmsTemplate.send(topicDestination,session -> session.createTextMessage(item.getId() + "#update"));

        return FCResult.ok();

    }

    @Override
    public FCResult deleteItem(Long[] ids) {

        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                itemMapper.deleteByPrimaryKey(id);
                itemDescMapper.deleteByPrimaryKey(id);
            }
        }

        jmsTemplate.send(topicDestination,session -> session.createTextMessage(Arrays.toString(ids) + "#delete"));

        return FCResult.ok();
    }

    @Override
    public FCResult instockItem(Long[] ids) {

        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                TbItem item = itemMapper.selectByPrimaryKey(id);
                item.setStatus((byte) 2);

                itemMapper.updateByPrimaryKeySelective(item);
            }
        }

        return FCResult.ok();
    }

    @Override
    public FCResult reshelfItem(Long[] ids) {
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                TbItem item = itemMapper.selectByPrimaryKey(id);
                item.setStatus((byte) 1);

                itemMapper.updateByPrimaryKeySelective(item);
            }
        }

        return FCResult.ok();
    }
}
