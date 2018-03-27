package com.southgt.smosplat.common.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该filter用于验证请求Referer是否是本站点，用来简单处理跨站点请求伪造问题（应该使用token参数更为严谨）
 * @version v1.0.0
 * Copyright (C) 2018 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2018年3月5日     mohaolin       v1.0.0        create</p>
 *
 */
public class CheckRefererFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	/* 检查头部的Refeter属性，是否是跨站点的请求
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String referer=((HttpServletRequest)request).getHeader("Referer");
		if(referer!=null&&!referer.equals("")){
			if(referer.indexOf("smosplat")!=-1){//具有smosplat表明来源于本站
				chain.doFilter(request, response);
			}else{
				//有些请求是页面要跳转，有些请求是返回数据，暂时不分别处理
				response.getWriter().write("非法请求！");
				return;
			}
		}else{
            chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		
	}

}
