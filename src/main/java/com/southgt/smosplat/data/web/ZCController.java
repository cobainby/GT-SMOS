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
import com.southgt.smosplat.data.entity.ZC_Data;
import com.southgt.smosplat.data.service.IZCService;
import com.southgt.smosplat.project.entity.Project;

/**
 * 
 *支撑轴力计数据控制器
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
public class ZCController {
	@Resource IZCService zcService;
	//获取最新10条支撑轴力计数据
	@RequestMapping("/getLatestZCDatasByProject")
	@ResponseBody
	public String getLatestZCDatasByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = zcService.getLatestZCDatasByProject(project);
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
