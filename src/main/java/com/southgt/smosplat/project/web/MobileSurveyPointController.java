package com.southgt.smosplat.project.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.project.entity.Mobile_SurveyPoint;
import com.southgt.smosplat.project.service.IMobileSurveyPointService;

/**
 * 手机端监测点控制器类
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年11月14日     YANG       v1.0.0        create</p>
 *
 */
@Controller
public class MobileSurveyPointController {

	@Resource
	IMobileSurveyPointService mobileSPService;
	
	@RequestMapping(value="/getmobileSPsByProjectAndMonitorItem")
	@ResponseBody
	public String getmobileSPsByProjectAndMonitorItem(String projectUuid,String monitorItemUuid,HttpSession session){
		Map<String,Object> map =new HashMap<>();
		List<Mobile_SurveyPoint> spList=mobileSPService.getSurveyPointsByProjectAndMonitorItem(projectUuid, monitorItemUuid);
		map.put("result", 0);
		map.put("data", spList);
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping(value="/operateSurveyPoints")
	@ResponseBody
	public String operateSurveyPoints(String surveyPointList,HttpSession session){
		Map<String,Object> map =new HashMap<>();
		try {
			List<Mobile_SurveyPoint> spList=JsonUtil.jsonToList(surveyPointList, Mobile_SurveyPoint.class);
			mobileSPService.operateSurveyPoints(spList);
			map.put("result", 0);
			map.put("msg", null);
		} catch (IOException e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return JsonUtil.beanToJson(map);

	}
}
