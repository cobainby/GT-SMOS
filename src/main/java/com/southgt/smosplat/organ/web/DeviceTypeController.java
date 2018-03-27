package com.southgt.smosplat.organ.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.organ.entity.DeviceType;
import com.southgt.smosplat.organ.service.IDeviceTypeService;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.Warning;

/**
 * 设备类型控制器类
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月29日     YANG       v1.0.0        create</p>
 *
 */
@Controller
public class DeviceTypeController {

	@Resource
	private IDeviceTypeService devTypeService;
	
	@RequestMapping("/getAllDevTypes")
	@ResponseBody
	public String getAllDevices(){
		Map<String, Object> map=new HashMap<String,Object>();
		List<DeviceType> devTypeList=devTypeService.findAllEntity();
		map.put("result", 0);
		map.put("rows", devTypeList);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/testDevType")
	@ResponseBody
	public void testDevType(){
		
//		IDeviceTypeService dtService=(IDeviceTypeService) ApplicationUtil.getWebApplicationContext().getBean("devTypeService");
//		
//		//增加设备类型
//		//sss
//		DeviceType dt=new DeviceType();
//		dt.setDevTypeName("测斜仪");
//		dtService.saveEntity(dt);
		
		
	}

	

	
}















