package com.southgt.smosplat.data.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.data.entity.LZ_Data;
import com.southgt.smosplat.data.entity.WYD_LevelData;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.WYS_OriginalData;
import com.southgt.smosplat.data.service.ILZService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_LZService;
/**
 * 
 * 力柱竖向位移数据控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月12日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller
public class LZController {
	@Resource
	ILZService lzDataService;
	
	@Resource
	IProjectService projectService;
	
	@Resource
	ISurveyPoint_LZService sp_LZService;
	
	//手机端将水准仪导出的数据进行上传
	@RequestMapping("/checkLZData")
	@ResponseBody
	public String checkManualLZLevelData(String jsonLevelData, String sourceData,HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		//手动解析全站仪导出的数据并插入数据库
		try {
			map = lzDataService.checkLZData(jsonLevelData, sourceData);
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
	@RequestMapping("/saveLZData")
	@ResponseBody
	public String saveLZData(String projectUuid,String data,String gapRateEarlyWarningPoints, String accumEarlyWarningPoints,String flag,HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//0是只保存没超限部分的数据，1是全部保存。
		byte bFlag = Byte.valueOf(flag);
		try{
			Project project = projectService.getEntity(projectUuid);
			List<String> gapWarningPoints = JsonUtil.jsonToList(gapRateEarlyWarningPoints, String.class);
			List<String> accumWarningPoints = JsonUtil.jsonToList(accumEarlyWarningPoints, String.class);
			List<LZ_Data> oriData =  JsonUtil.jsonToList(data, LZ_Data.class);
			//获取监测点
			List<SurveyPoint_LZ> hPoints = sp_LZService.getSP_LZs(projectUuid);
			//保存水平位移原始数据
			lzDataService.saveManualData(project, oriData, gapWarningPoints, accumWarningPoints, bFlag);
			map.put("result", 0);
		}catch(Exception ex){
			ex.printStackTrace();
			map.put("result", -1);
			map.put("msg", ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//手机端将水准仪导出的数据进行上传
	@RequestMapping("/getLatestLZDatasByProject")
	@ResponseBody
	public String getLatestLZDatasByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = lzDataService.getLatestLZDatasByProject(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	
	
	
	@RequestMapping("/getLatestLZDatas4APP")
	@ResponseBody
	public String getLatestLZDatas4App(String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project = projectService.getEntity(projectUuid);
		try {
			map = lzDataService.getLatestLZDatasByProject(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
