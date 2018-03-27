package com.southgt.smosplat.project.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectReport;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.IReportService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;

/**
 * 报表导出前端控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月14日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class ReportController {
	
	@Resource
	IReportService reportService;
	@Resource
	IMonitorItemService monitorItemService;
	@Resource
	IProjectService projectService;
	
	@Resource
	ISurveyPoint_WYSService sp_WYSService1;
	//生成日报
	@RequestMapping("/dailyReport")
	@ResponseBody
	public String dailyReport(HttpSession session,String dateStr,ProjectReport prTemp){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		ProjectReport pr = reportService.getProjectReportByProject(project.getProjectUuid());
		pr.setP7p8(prTemp.getP7p8());
		pr.setP7p16(prTemp.getP7p16());
		pr.setP7p24(prTemp.getP7p24());
		pr.setP7p32(prTemp.getP7p32());
		pr.setP7p40(prTemp.getP7p40());
		pr.setP7p48(prTemp.getP7p48());
		pr.setP7p56(prTemp.getP7p56());
		pr.setP7p64(prTemp.getP7p64());
		pr.setP7p72(prTemp.getP7p72());
		reportService.operateProjectReport(pr, project.getProjectUuid());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date eDate = sdf.parse(dateStr);
			Calendar date = Calendar.getInstance();
			date.setTime(eDate);
			date.add(date.DATE, - 2);
			date.add(Calendar.SECOND, 1);
			//起始日期就是选中日期的前一天
			Date sDate = sdf.parse(sdf.format(date.getTime()));
			//dateStr的格式为2017-10-01 23:59:59
			String fileUrl = reportService.dailyReport(project,sDate,eDate,pr);
			map.put("result", 0);
			map.put("fileUrl", fileUrl);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	//获取日报数据
	@RequestMapping("/dailyReportData")
	@ResponseBody
	public String dailyReportData(HttpSession session,String dateStr){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			//dateStr的格式为2017-10-01 23:59:59
			//获取数据
			reportService.getDailyReportDate(map,project,dateStr);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	//周报
	@RequestMapping("/weeklyReport")
	@ResponseBody
	public String weeklyReport(HttpSession session,String bDateStr, String eDateStr, ProjectReport pr){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			pr.setProject(project);
			reportService.operateProjectReport(pr, project.getProjectUuid());
			String fileUrl=reportService.weeklyReport(project, bDateStr, eDateStr,pr);
			map.put("result", 0);
			map.put("fileUrl", fileUrl);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	//一段时间的报表
	//flag:监测项简称
	@RequestMapping("/timeReport")
	@ResponseBody
	public String timeReport(HttpSession session,String bDateStr, String eDateStr,String flag){
		Map<String, Object> map=new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			reportService.timeReport(map,project, bDateStr, eDateStr, flag);
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getLimitAccumOffsetAndBiggestChangeRate")
	@ResponseBody
	public String getLimitAccumOffsetAndBiggestChangeRate(HttpSession session, String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project = projectService.getEntity(projectUuid);
		try {
			map = reportService.getLimitAccumOffsetAndBiggestChangeRate(project);
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
	//
	@RequestMapping("/getLimitAccumOffsetAndBiggestChangeRateByDay")
	@ResponseBody
	public String getLimitAccumOffsetAndBiggestChangeRateByDay(HttpSession session, String dateStr){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
	//		Project project = projectService.getEntity(projectUuid);
			Project project=(Project) session.getAttribute("currentProject");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date eDate = sdf.parse(dateStr);
			Calendar date = Calendar.getInstance();
			date.setTime(eDate);
			date.add(date.DATE, - 2);
			date.add(Calendar.SECOND, 1);
			//起始日期就是选中日期的前一天
			Date sDate = sdf.parse(sdf.format(date.getTime()));
		
			map = reportService.getLimitAccumOffsetAndBiggestChangeRateByPeriod(project,sDate,eDate);
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getLimitAccumOffsetAndBiggestChangeRateByPeriod")
	@ResponseBody
	public String getLimitAccumOffsetAndBiggestChangeRateByPeriod(HttpSession session,String sDateStr, String eDateStr){
		Map<String, Object> map = new HashMap<String,Object>();
		try {
	//		Project project = projectService.getEntity(projectUuid);
			Project project=(Project) session.getAttribute("currentProject");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date eDate = sdf.parse(eDateStr);
			Date sDate = sdf.parse(sDateStr);
		
			map = reportService.getLimitAccumOffsetAndBiggestChangeRateByPeriod(project,sDate,eDate);
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/getProjectReportByProject")
	@ResponseBody
	public String getProjectReportByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			//空的，还没写取数据部分
			ProjectReport pr= reportService.getProjectReportByProject(project.getProjectUuid());
			map.put("result", 0);
			map.put("entity", pr);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
	
	@RequestMapping("/addProjectReport")
	@ResponseBody
	public String addProjectReport(HttpSession session, ProjectReport pr){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			pr.setProject(project);
			reportService.operateProjectReport(pr, project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
}
