package com.southgt.smosplat.data.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.service.ISWService;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IProjectService;

/**
 * 
 * 水位计数据控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年5月3日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller
public class SWController {
	
	@Resource ISWService swService;
	
	@Resource
	IProjectService projectService;
	//获取最新10条水位数据
	@RequestMapping("/getLatestSWDatasByProject")
	@ResponseBody
	public String getLatestSWDatasByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = swService.getLatestSWDatasByProject(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getLatestSWDatas4App")
	@ResponseBody
	public String getLatestSWDatas4App(String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = projectService.getEntity(projectUuid);
		try {
			map = swService.getLatestSWDatasByProject(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
