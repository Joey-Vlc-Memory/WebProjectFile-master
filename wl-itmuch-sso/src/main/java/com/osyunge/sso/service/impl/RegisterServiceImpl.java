package com.osyunge.sso.service.impl;

import com.osyunge.dao.TbUserMapper;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbUser;
import com.osyunge.dataobject.TbUserExample;
import com.osyunge.sso.service.RegisterService;
import com.osyunge.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public FCResult CheckParam(String param, Integer type) {

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        if (type == 1){
            criteria.andUsernameEqualTo(param);
        }

        if (type == 2){
            criteria.andPhoneEqualTo(param);
        }

        List<TbUser> users = userMapper.selectByExample(example);

        //用户名或者电话号码已使用
        if (users != null && users.size() > 0){
            return FCResult.ok(false);
        }

        return FCResult.ok(true);
    }

    @Override
    public FCResult RegisterUser(TbUser user) {

        Date date = new Date();

        user.setUpdated(date);
        user.setCreated(date);

        //密码用MD5加密
        user.setPassword(MD5Util.EncodeByMD5(user.getPassword()));

        userMapper.insert(user);

        return FCResult.ok();
    }
}
