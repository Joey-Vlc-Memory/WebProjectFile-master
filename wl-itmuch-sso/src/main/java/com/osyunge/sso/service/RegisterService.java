package com.osyunge.sso.service;

import com.osyunge.dataobject.FCResult;
import com.osyunge.dataobject.TbUser;


public interface RegisterService {

    FCResult CheckParam(String param,Integer type);

    FCResult RegisterUser(TbUser user);


}
