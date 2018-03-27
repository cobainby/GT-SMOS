package com.southgt.smosplat.project.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.IWarnningDataService;

@Controller
public class WarnningDataController {
	
	@Resource
	IWarnningDataService warnningDataService;
	
	@Resource
	IProjectService projectService;
	//获取超限的数据
	@RequestMapping("/getWarningData")
	@ResponseBody
	public String getWarningData(HttpSession session, String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project = projectService.getEntity(projectUuid);
		try {
			warnningDataService.calWarnningOffset(map, project);
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
	//处理点
	@RequestMapping("/processPoints")
	@ResponseBody
	public String processPoints(HttpSession session, String point,String flag){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			warnningDataService.processPoints(project,point,flag);
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
}
