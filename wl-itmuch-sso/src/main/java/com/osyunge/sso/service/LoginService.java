package com.osyunge.sso.service;

import com.osyunge.dataobject.FCResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

    FCResult doLogin(String username, String password, HttpServletRequest request, HttpServletResponse response);

    FCResult getUserByToken(String token);

    FCResult doLogout(String token, HttpServletRequest request, HttpServletResponse response);

}
