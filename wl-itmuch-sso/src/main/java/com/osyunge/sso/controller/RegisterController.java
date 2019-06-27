package com.osyunge.sso.controller;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbUser;
import com.osyunge.sso.service.RegisterService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class RegisterController {


    @Autowired
    private RegisterService registerService;

    @RequestMapping("/showRegister")
    public String showRegister(){
        return "register";
    }

    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public Object checkParam(@PathVariable String param,@PathVariable Integer type,String callback){

        FCResult result = null;

        if (StringUtils.isEmpty(param)){
            result = FCResult.build(400,"校验内容不能为空！");
        }
        if (type == null){
            result = FCResult.build(400,"校验类型不能为空！");
        }

        if (type != null && type != 1 && type != 2){
            result = FCResult.build(400,"校验类型出错！");
        }

        if (result != null){
            if (callback != null){
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);

                mappingJacksonValue.setJsonpFunction(callback);

                return mappingJacksonValue;
            }else {
                return result;
            }
        }

        try {

            result = registerService.CheckParam(param,type);

        }catch (Exception e){
            result = FCResult.build(400, ExceptionUtils.getStackTrace(e));
        }

        if (callback != null){
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;

        }else {
            return result;

        }

    }

    @RequestMapping("/register")
    @ResponseBody
    public FCResult registerUser(TbUser user){

        FCResult result;
        try {
            result = registerService.RegisterUser(user);
        }catch (Exception e){
            return FCResult.build(400,ExceptionUtils.getStackTrace(e));
        }

        return result;
    }

    @RequestMapping("/showLogin")
    public String showLogin(Model model,HttpServletRequest request){

        model.addAttribute("redirect",request.getHeader("Referer"));

        return "login";
    }

}
