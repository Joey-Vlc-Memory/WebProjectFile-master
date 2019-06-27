package com.osyunge.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbItemCatMapper;
import com.osyunge.dataobject.TbItem;
import com.osyunge.dataobject.TbItemCat;
import com.osyunge.dataobject.TbItemCatExample;
import com.osyunge.pojo.CatNode;
import com.osyunge.pojo.ItemCatResult;
import com.osyunge.service.ItemCatService;
import com.osyunge.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEMCATLIST_KEY}")
    private String REDIS_ITEMCATLIST_KEY;

    @Value("${REDIS_ITEMCATLIST_EXPIRE}")
    private Integer REDIS_ITEMCATLIST_EXPIRE;

    @Override
    public ItemCatResult getItemCatList() {

        //递归查询父节点下的子节点
        List catList = getItemCatList(0L);

        ItemCatResult result = new ItemCatResult();
        result.setData(catList);

        return result;
    }

    @Override
    public List getItemCatList(Long parentId) {

        //从Redis中获取数据
        try {

            String result = jedisClient.get(REDIS_ITEMCATLIST_KEY + ":" + parentId + ":base");

            if(!StringUtils.isEmpty(result)){
               return JsonUtils.jsonToList(result, CatNode.class);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //根据parentId查询列表
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        List resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : list) {
            //如果是父节点
            if (tbItemCat.getIsParent()) {
                CatNode node = new CatNode();
                node.setUrl("/products/"+tbItemCat.getId()+".html");
                //如果当前节点为第一级节点
                if (tbItemCat.getParentId() == 0) {
                    node.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
                } else {
                    node.setName(tbItemCat.getName());
                }
                node.setItems(getItemCatList(tbItemCat.getId()));
                //把node添加到列表
                resultList.add(node);
            } else {
                //如果是叶子节点
                String item = "/products/"+tbItemCat.getId()+".html|" + tbItemCat.getName();
                resultList.add(item);
            }
        }

        try {

            jedisClient.set(REDIS_ITEMCATLIST_KEY + ":" + parentId + ":base",JsonUtils.objectToJson(resultList));
            jedisClient.expire(REDIS_ITEMCATLIST_KEY + ":" + parentId + ":base",REDIS_ITEMCATLIST_EXPIRE);

        }catch (Exception e){
            e.printStackTrace();
        }

        return resultList;
    }


}

