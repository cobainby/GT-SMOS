package com.southgt.smosplat.project.web;

import java.util.Comparator;
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
import com.southgt.smosplat.project.entity.SitePic;
import com.southgt.smosplat.project.service.ISitePicService;
/**
 * 
 * 现场图片类控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年9月20日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller
public class SitePicController {
	
	@Resource 
	ISitePicService sitePicService;
	
	@RequestMapping("/getAllSitePicByProjectUuid")
	@ResponseBody
	public String getAllSitePicByProjectUuid(HttpSession session,String dateStr){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			List<SitePic> sitePics = sitePicService.getAllSitePicByProjectUuid(project.getProjectUuid());
			//排序
			sitePics.sort(new Comparator<SitePic>() {
				@Override
				public int compare(SitePic sp1, SitePic sp2) {
					if (sp1.getPicName().compareTo(sp2.getPicName()) < 0) {
						return -1;
					} else if (sp1.getPicName().equals(sp2.getPicName())) {
						return 0;
					} else {
						return 1;
					}
				}
			});
			map.put("result", 0);
			map.put("sitePics", sitePics);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/delSitePicById")
	@ResponseBody
	public String delSitePicById(String sitePicUuid, HttpSession session,String dateStr){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			SitePic sitePic = sitePicService.getEntity(sitePicUuid);
			sitePicService.deleteEntity(sitePic);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
}
