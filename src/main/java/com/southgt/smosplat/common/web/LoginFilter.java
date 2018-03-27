package com.southgt.smosplat.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.southgt.smosplat.common.util.HttpRequestDeviceUtil;
import com.southgt.smosplat.common.util.LogUtil;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月18日     mohaolin       v1.0.0        create</p>
 *
 */
public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		HttpSession session=httpRequest.getSession();
		//手机端的请求不进行过滤
		if(HttpRequestDeviceUtil.isMobileDevice(httpRequest)){
			chain.doFilter(request, response);
			return;
		}
        //判断用户是否登录，进行页面的处理
        if(session.getAttribute("account")==null){
        	//由于web.xml中设置Filter过滤全部请求，需要排除不需要过滤的url
            String requestURI = httpRequest.getRequestURI();
            String[] ignoreUrls = {"smosplat/", "/loginView","/login","/organIndex",".css",".js",".jpg",".jpeg",".png",".pdf"};
            for (String s : ignoreUrls) {
                if (requestURI.endsWith(s)) {
                	chain.doFilter(request, response);
                    return;
                }
            }
            //重定向到登录页面
            ((HttpServletResponse)response).sendRedirect("/smosplat/loginView");
            return;
        } else {
            //已登录用户，允许访问
            chain.doFilter(request, response);
            return;
        }
	}

	@Override
	public void destroy() {

	}

}
