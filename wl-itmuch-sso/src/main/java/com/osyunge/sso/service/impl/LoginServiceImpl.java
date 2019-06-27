package com.osyunge.sso.service.impl;

import com.osyunge.api.JedisClient;
import com.osyunge.dao.TbUserMapper;
import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbUser;
import com.osyunge.dataobject.TbUserExample;
import com.osyunge.sso.service.LoginService;
import com.osyunge.utils.CookieUtils;
import com.osyunge.utils.JsonUtils;
import com.osyunge.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;

    @Value("${SSO_SESSION_EXPIRE}")
    private Integer SSO_SESSION_EXPIRE;

    @Override
    public FCResult doLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {

        TbUserExample example = new TbUserExample();

        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<TbUser>  users = userMapper.selectByExample(example);

        TbUser user;

        FCResult result;

        //用户名不存在
        if (users == null || users.size() == 0){

           result = FCResult.build(400,"用户名不存在！");

        }else {

            user = users.get(0);
            //密码与用户名不符
            if (!MD5Util.EncodeByMD5(password).equals(user.getPassword())){
                result = FCResult.build(400,"密码与用户名不符！");
            }else {

                String token = UUID.randomUUID().toString();

                user.setPassword(null);

                //向Cookie中添加token值

                CookieUtils.setCookie(request,response,"TT_TOKEN",token);

                //向Redis中存放用户信息
                jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));

                //设置过期时间
                jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token,SSO_SESSION_EXPIRE);

                result = FCResult.ok(token);

            }

        }


        return result;
    }

    @Override
    public FCResult getUserByToken(String token) {

        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);

        if (StringUtils.isEmpty(json)){
            return  FCResult.build(400,"用户已过期，请重新登录");
        }

        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token,SSO_SESSION_EXPIRE);

        return FCResult.ok(JsonUtils.jsonToPojo(json,TbUser.class));
    }

    @Override
    public FCResult doLogout(String token, HttpServletRequest request, HttpServletResponse response) {

        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);

        if (StringUtils.isEmpty(json)){
            return  FCResult.build(400,"此账户已退出！");
        }

        jedisClient.del(REDIS_USER_SESSION_KEY + ":" + token);

        CookieUtils.deleteCookie(request,response,"T_TOKEN");

        return FCResult.ok();
    }
}
