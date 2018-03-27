package com.southgt.smosplat.data.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.data.entity.LZ_Data;
import com.southgt.smosplat.data.entity.ZGD_Data;
import com.southgt.smosplat.data.service.ICXService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_CXService;
/**
 * 
 * 测斜数据控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月10日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller
public class CXController {
	@Resource
	ICXService cxService;
	
	@Resource 
	ISurveyPoint_CXService surveyPointCXService;
	
	@Resource 
	ICXService cxDataService;
	
	@Resource
	IProjectService projectService;
	
	//手机端将测斜仪导出的数据进行上传(支护结构深层水平位移)
	@RequestMapping("/checkCXData")
	@ResponseBody
	public String checkCXData(String jsonCXData, String sourceData, HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		//手动解析全站仪导出的数据并插入数据库
		try {
			map = cxService.checkCXData(jsonCXData, sourceData);
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
	
	//保存手机端将全站仪导出的数据
	@RequestMapping("/saveCXData")
	@ResponseBody
	public String saveCXData(String projectUuid,String data,String gapRateEarlyWarningPoints, String accumEarlyWarningPoints,String flag,HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//0是只保存没超限部分的数据，1是全部保存。
		byte bFlag = Byte.valueOf(flag);
		try{
			Project project = projectService.getEntity(projectUuid);
			List<String> gapWarningPoints = JsonUtil.jsonToList(gapRateEarlyWarningPoints, String.class);
			List<String> accumWarningPoints = JsonUtil.jsonToList(accumEarlyWarningPoints, String.class);
			List<CX_Data> oriData =  JsonUtil.jsonToList(data, CX_Data.class);
			//获取监测点
			List<SurveyPoint_CX> hPoints = surveyPointCXService.getSP_CXs(projectUuid);
			//保存水平位移原始数据
			cxDataService.saveManualData(project, oriData, gapWarningPoints, accumWarningPoints, bFlag);
			map.put("result", 0);
		}catch(Exception ex){
			ex.printStackTrace();
			map.put("result", -1);
			map.put("msg", ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//获取最新4次测斜数据
	@RequestMapping("/getLatestCXDatasByProject")
	@ResponseBody
	public String getLatestCXDatasByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = cxService.getLatestCXDatasByProject(project);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//获取最新4次测斜数据
	@RequestMapping("/getLatestCXDatas4App")
	@ResponseBody
	public String getLatestCXDatas4App(String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = projectService.getEntity(projectUuid);
		try {
			map = cxService.getLatestCXDatasByProject(project);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//获取最新4次测斜数据
	@RequestMapping("/getLatestTimeBySurveyPoint")
	@ResponseBody
	public String getLatestTimeBySurveyPoint(String surveyPointUuid, HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		Date d = cxService.getLatestTimeBySurveyPoint(surveyPointUuid);
		map.put("latestTime", d);
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	
	//获取项目中所有测斜点最大的测量次序
	@RequestMapping("/getmaxSurveyOrderByProject")
	@ResponseBody
	public String getmaxSurveyOrderByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = cxService.getMaxSurveyOrderBySurveyPoint(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getmaxSurveyOrderByProjectUuid")
	@ResponseBody
	public String getmaxSurveyOrderByProject(String projectUuid, HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = projectService.getEntity(projectUuid);
		if(project == null){
			map.put("result", -1);
			map.put("msg", "项目为空！");
			String s=JsonUtil.beanToJson(map);
			return s;
		}
		try {
			map = cxService.getMaxSurveyOrderBySurveyPoint(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//根据项目id，测量次序获取测斜数据
	@RequestMapping("/getCxDataBySurveyOrderAndSurveyPoint")
	@ResponseBody
	public String getCxDataBySurveyOrderAndSurveyPoint(int surveyOrder,String surveyPointUuid,HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = cxService.getCXDatasBySurveyOrderAndSurveyPoint(surveyOrder, surveyPointUuid);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
