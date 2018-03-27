package com.southgt.smosplat.data.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.data.service.IQXService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IProjectService;
/**
 * 
 * 建筑物倾斜数据控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月15日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller
public class QXController {
	@Resource
	IQXService qxService;
	
	@Resource
	IProjectService projectService;
	
	//手机端将测斜仪导出的数据进行上传(支护结构深层水平位移)
	@RequestMapping("/saveManualQXData")
	@ResponseBody
	public String saveManualQX(String jsonQXData,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		//手动解析全站仪导出的数据并插入数据库
		try {
			map = qxService.saveQXData(jsonQXData);
//			List<WYD_LevelData> dataList = (List<WYD_LevelData>) map.get("dataList");
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg",e.toString());
			e.printStackTrace();
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//获取最新4次测斜数据
	@RequestMapping("/getLatestQXDatasByProject")
	@ResponseBody
	public String getLatestQXDatasByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = qxService.getLatestQXDatasByProject(project);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//获取最新4次测斜数据
	@RequestMapping("/getLatestQXDatas4App")
	@ResponseBody
	public String getLatestQXDatas4App(String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project = projectService.getEntity(projectUuid);
		try {
			map = qxService.getLatestQXDatasByProject(project);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//获取项目中所有测斜点最大的测量次序
	@RequestMapping("/getQxMaxSurveyOrderByProject")
	@ResponseBody
	public String getQxMaxSurveyOrderByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = qxService.getMaxSurveyOrderBySurveyPoint(project);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//根据项目id，测量次序获取测斜数据
	@RequestMapping("/getQxDataBySurveyOrderAndSurveyPoint")
	@ResponseBody
	public String getQxDataBySurveyOrderAndSurveyPoint(int surveyOrder,String surveyPointUuid,HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = qxService.getQXDatasBySurveyOrderAndSurveyPoint(surveyOrder, surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
