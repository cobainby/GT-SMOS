package com.southgt.smosplat.project.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.ApplicationUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.common.util.LogUtil;
import com.southgt.smosplat.project.entity.Network;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.INetworkService;
import com.southgt.smosplat.project.service.IProjectService;

/**
 * 自动化采集前端控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月17日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class AutoCollectController {
	
	@Resource
	IProjectService projectService;
	
	@Resource 
	INetworkService networkService;
//	//开始采集
//	@RequestMapping("/projectStartCollect")
//	@ResponseBody
//	public String projectStartCollect(String networkUuids,int interval,int connectMcuTime,HttpSession session){
//		Project project=(Project) session.getAttribute("currentProject");
//		Map<String, Object> map=new HashMap<String, Object>();
//		try {
//			projectService.projectStartCollect(project,networkUuids,interval,connectMcuTime);
//			map.put("result", 0);
//		}catch (Exception e) {
//			map.put("result", -1);
//			map.put("msg", e.getMessage());
//		}
//		return JsonUtil.beanToJson(map);
//	}
	
	//开始采集
	@RequestMapping("/projectStartCollect_NEW")
	@ResponseBody
	public String projectStartCollect_NEW(String networkUuids,int interval,int connectMcuTime,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			projectService.projectStartCollect_NEW(project,networkUuids,interval,connectMcuTime);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	//开始采集
	@RequestMapping("/projectStartCollect_LRK")
	@ResponseBody
	public String projectStartCollect_LRK(String networkUuids,int interval,int connectMcuTime,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			projectService.projectStartCollect_LRK(project,networkUuids,interval,connectMcuTime);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	@RequestMapping("/mcuFilter")
	@ResponseBody
	public String mcuFilter(String networkUuids, HttpSession session){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			map = projectService.mcuFilter(networkUuids);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	//开始召测采集
	@RequestMapping("/callCollect")
	@ResponseBody
	public String callCollect(String networkUuids,int connectMcuTime){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			projectService.projectStartCallCollect(networkUuids,connectMcuTime);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	//开始召测采集
	@RequestMapping("/testConnect")
	@ResponseBody
	public String testConnect(String networkUuid){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			projectService.testConnect(networkUuid);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", "端口监听失败，检查IP,端口是否正确/端口是否被占用！");
		}
		return JsonUtil.beanToJson(map);
	}
	
	//开始召测采集
	@RequestMapping("/testMcu")
	@ResponseBody
	public String testMcu(String mcuUuid){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			projectService.testMcu(mcuUuid);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	//结束召测采集
	@RequestMapping("/stopCallCollect")
	@ResponseBody
	public String stopCallCollect(String networkUuid){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			Network network = networkService.getEntity(networkUuid);
			ApplicationUtil.endConnectAction(network);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	
	//该接口用于保持session会话，避免会话失效后导致接口调用失败
	@RequestMapping("/keepSessionAlive")
	@ResponseBody
	public String keepSessionAlive(){
		Map<String,Object> map=new HashMap<String,Object>();
		return JsonUtil.beanToJson(map);
	}
	
	//开始采集
	@RequestMapping("/testMTCollect")
	@ResponseBody
	public String testMTCollect(HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			String path = "E:\\UploadJson.txt";
			String json = readTxtFile(path);
			ApplicationUtil.getSimpMessagingTemplate().convertAndSend("/collect/mcuSWStartCollect/"+"5e8fb5cb-0d9c-4e7d-b11a-54938bc18f9f", json);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
	}
	
	/**
	 * 
	 * 功能：Java读取txt文件的内容
	 * 
	 * 步骤：1：先获得文件句柄
	 * 
	 * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 
	 * 3：读取到输入流后，需要读取生成字节流
	 * 
	 * 4：一行一行的输出。readline()。
	 * 
	 * 备注：需要考虑的是异常情况
	 * 
	 * @param filePath
	 * 
	 */

	public static String readTxtFile(String filePath) {
		String result = "";
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					System.out.println(lineTxt);
					result += lineTxt;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return result;
	}
	
	//停止采集
	@RequestMapping("/projectStopCollect")
	@ResponseBody
	public String projectStopCollect(String networkUuids,HttpSession session){
		Project project=(Project) session.getAttribute("currentProject");
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			projectService.projectStopCollect(project,networkUuids);
			map.put("result", 0);
		}catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
			LogUtil.error(e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
}
