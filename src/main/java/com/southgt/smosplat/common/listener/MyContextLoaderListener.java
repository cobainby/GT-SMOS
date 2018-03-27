package com.southgt.smosplat.common.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.southgt.smosplat.common.util.ApplicationUtil;

/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月8日     mohaolin       v1.0.0        create</p>
 *
 */
public class MyContextLoaderListener extends ContextLoaderListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		super.contextInitialized(sce);
		ServletContext sc=sce.getServletContext();
		WebApplicationContext wac=WebApplicationContextUtils.getWebApplicationContext(sc);
		ApplicationUtil.setWebApplicationContext(wac);
		//日志
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//停止应用系统时，需要将所有项目采集状态设置为-1，否则重新部署应用后再开始采集就会报错，因为内存中已经没有了相关对象
//		WebApplicationContext wac=WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
//		IProjectService projectService=(IProjectService) wac.getBean("projectService");
//		projectService.onWebappDestroy();
//		ApplicationUtil.setWebApplicationContext(null);
//		super.contextDestroyed(sce);
	}
	
	
	
}
