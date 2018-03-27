package com.southgt.smosplat.project.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectReport;
import com.southgt.smosplat.project.service.IExportDataService;
import com.southgt.smosplat.project.service.IExportSPDataService;
import com.southgt.smosplat.project.service.IReportService;

@Controller
public class ExportSPDataController {
	@Resource
	IExportDataService exportDataService;
	
	@Resource
	IExportSPDataService exportSPDataService;
	
	@Resource
	IReportService reportService;

	@RequestMapping("/weeklyExport")
	@ResponseBody
	public String weeklyExcel(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,ProjectReport prTemp,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map =new HashMap<String, Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			ProjectReport pr = reportService.getProjectReportByProject(project.getProjectUuid());
			pr.setP1p78(prTemp.getP1p78());
			pr.setP1p79(prTemp.getP1p79());
			pr.setP1p80(prTemp.getP1p80());
			pr.setP1p81(prTemp.getP1p81());
			pr.setP1p82(prTemp.getP1p82());
			pr.setP1p83(prTemp.getP1p83());
			pr.setP1p84(prTemp.getP1p84());
			pr.setP1p85(prTemp.getP1p85());
			pr.setP1p86(prTemp.getP1p86());
			pr.setP1p87(prTemp.getP1p87());
			pr.setP1p88(prTemp.getP1p88());
			pr.setP1p89(prTemp.getP1p89());
			pr.setP1p90(prTemp.getP1p90());
			pr.setP1p91(prTemp.getP1p91());
			pr.setP1p92(prTemp.getP1p92());
			pr.setP1p93(prTemp.getP1p93());
			pr.setP1p94(prTemp.getP1p94());
			pr.setP1p95(prTemp.getP1p95());
			reportService.operateProjectReport(pr, project.getProjectUuid());
			List<String> templatePath=new ArrayList<String>();
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsm"));
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/mt.xlsm"));
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/stressGroup.xlsm"));
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/cx.xlsm"));
			String fileUrl=exportSPDataService.exportExcelCoreService(false,templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr,pr);
			map.put("result", 0);
			map.put("fileUrl", fileUrl);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		 
	}
	
	@RequestMapping("/dailyExport")
	@ResponseBody
	public String dailyExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,ProjectReport prTemp,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map =new HashMap<String, Object>();
		Project project=(Project) session.getAttribute("currentProject");
		try {
			ProjectReport pr = reportService.getProjectReportByProject(project.getProjectUuid());
			pr.setP1p78(prTemp.getP1p78());
			pr.setP1p79(prTemp.getP1p79());
			pr.setP1p80(prTemp.getP1p80());
			pr.setP1p81(prTemp.getP1p81());
			pr.setP1p82(prTemp.getP1p82());
			pr.setP1p83(prTemp.getP1p83());
			pr.setP1p84(prTemp.getP1p84());
			pr.setP1p85(prTemp.getP1p85());
			pr.setP1p86(prTemp.getP1p86());
			pr.setP1p87(prTemp.getP1p87());
			pr.setP1p88(prTemp.getP1p88());
			pr.setP1p89(prTemp.getP1p89());
			pr.setP1p90(prTemp.getP1p90());
			pr.setP1p91(prTemp.getP1p91());
			pr.setP1p92(prTemp.getP1p92());
			pr.setP1p93(prTemp.getP1p93());
			pr.setP1p94(prTemp.getP1p94());
			pr.setP1p95(prTemp.getP1p95());
			reportService.operateProjectReport(pr, project.getProjectUuid());
			List<String> templatePath=new ArrayList<String>();
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsm"));
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/mt.xlsm"));
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/stressGroup.xlsm"));
			templatePath.add(request.getServletContext().getRealPath("/project/excelTemplate/cxDaily.xlsm"));
			String fileUrl=exportSPDataService.exportExcelCoreService(true,templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr,pr);
			map.put("result", 0);
			map.put("fileUrl", fileUrl);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		return JsonUtil.beanToJson(map);
		
	}
	
}
