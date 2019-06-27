package com.osyunge.sso.controller;

import com.osyunge.dataobject.FCResult;
import com.osyunge.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/{page}")
    public String getLogin(@PathVariable String page){
        return page;
    }

    @RequestMapping("/user/login")
    @ResponseBody
    public FCResult doLogin(String username, String password, HttpServletRequest request, HttpServletResponse response){

        FCResult result;

        try {
            result = loginService.doLogin(username,password,request,response);

        }catch (Exception e){
            result = FCResult.build(400, ExceptionUtils.getStackTrace(e));
        }

        return result;
    }


    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback){
        FCResult result;

        try {
            result = loginService.getUserByToken(token);
        }catch (Exception e){
            result = FCResult.build(400,ExceptionUtils.getStackTrace(e));
        }

        if (StringUtils.isEmpty(callback)){
            return result;
        }else {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);

            return mappingJacksonValue;
        }

    }

    @RequestMapping("/user//logout/{token}")
    @ResponseBody
    public Object doLogout(@PathVariable String token,String callback, HttpServletRequest request, HttpServletResponse response){

        FCResult result;

        try {
            result = loginService.doLogout(token,request,response);
        }catch (Exception e){
            e.printStackTrace();
            result = FCResult.build(400,ExceptionUtils.getStackTrace(e));
        }

        if (StringUtils.isEmpty(callback)){
            return result;
        }else {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);

            return mappingJacksonValue;
        }

    }

}
