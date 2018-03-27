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
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.entity.DeviceModel;
import com.southgt.smosplat.organ.entity.DeviceType;
import com.southgt.smosplat.organ.service.IDeviceModelService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.organ.service.IDeviceTypeService;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.Warning;

@Controller
public class DeviceModelController {

	@Resource
	private IDeviceTypeService devTypeService;
	
	@Resource
	private IDeviceModelService deviceModelService;
	
	@RequestMapping("/getAllDeviceModel")
	@ResponseBody
	public String getAllDeviceModel(){
		Map<String, Object> map=new HashMap<String,Object>();
		List<DeviceModel> deviceModelList=deviceModelService.findAllEntity();
		map.put("result", 0);
		map.put("rows", deviceModelList);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getDeviceModelByDevType")
	@ResponseBody
	public String getDeviceModelByDevType(String devTypeName){
		Map<String, Object> map=new HashMap<String,Object>();
		List<DeviceModel> deviceModelList=deviceModelService.getDeviceModelByDeviceType(devTypeName);
		map.put("result", 0);
		map.put("rows", deviceModelList);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/testDeviceModel")
	@ResponseBody
	public void testDeviceModel(){
		IDeviceService dService=(IDeviceService) ApplicationUtil.getWebApplicationContext().getBean("deviceService");
		Device d=new Device();
		dService.saveEntity(d);
		
		Device d1=new Device();
		dService.saveEntity(d1);
		
		Device d2=new Device();
		dService.saveEntity(d2);
		
	}

	

	
}















