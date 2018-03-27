package com.southgt.smosplat.project.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectReport;
import com.southgt.smosplat.project.service.IExportDataService;
import com.southgt.smosplat.project.service.IReportService;

/**
 * excel导出前端控制器
 * 
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author YANG
 * <p>Modification History:</p>
 * <p> Date         Author   杨杰   Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年7月20日     YANG       v1.0.0        create</p>
 *
 */
@Controller
public class ExportDataController {

	@Resource
	IExportDataService exportDataService;
	
	@Resource
	IReportService reportService;
	
	@RequestMapping("/wysExport")
	@ResponseBody
	public void wysExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsx");
		byte[] data=exportDataService.exportWYSDataToExcel(templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("围护墙（边坡）顶部水平位移监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/wydExport")
	@ResponseBody
	public void wydExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsx");
		byte[] data=exportDataService.exportWYDDataToExcel(templatePath, pjName, pjAddress, deviceName,project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("围护墙（边坡）竖向位移监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/lzExport")
	@ResponseBody
	public void lzExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsx");
		byte[] data=exportDataService.exportLZDataToExcel(templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("立柱监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/smExport")
	@ResponseBody
	public void smExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsx");
		byte[] data=exportDataService.exportSMDataToExcel(templatePath, pjName, pjAddress, deviceName,project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("周边建筑竖向位移监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/swExport")
	@ResponseBody
	public void swExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsx");
		byte[] data=exportDataService.exportSWDataToExcel(templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("地下水位监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/zgdExport")
	@ResponseBody
	public void zgdExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/surveyPoint.xlsx");
		byte[] data=exportDataService.exportZGDDataToExcel(templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("周边管线竖向位移监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/mtExport")
	@ResponseBody
	public void mtExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/mt.xlsx");
		byte[] data=exportDataService.exportMTDataToExcel(templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("锚杆内力监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/zcExport")
	@ResponseBody
	public void zcExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/stressGroup.xlsx");
		byte[] data=exportDataService.exportZCDataToExcel(templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("支撑内力监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/cxExport")
	@ResponseBody
	public void cxExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/clinometerGroup.xlsx");
		byte[] data=exportDataService.exportCXDataToExcel(templatePath, pjName, pjAddress,deviceName, project, specification, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode("支护结构深层水平位移监测结果"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/spDataExport")
	@ResponseBody
	public void spDataExport(HttpSession session,String pjName,String pjAddress,String deviceName,String specification,String bDateStr, String eDateStr,ProjectReport prTemp,HttpServletRequest request,HttpServletResponse response){
		Project project=(Project) session.getAttribute("currentProject");
		//更新报告中的各监测项采集仪器和标准规范的字段
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
		String templatePath=request.getServletContext().getRealPath("/project/excelTemplate/surveyData.xlsx");
		byte[] data=exportDataService.exportAllSurveyDataToExcel(templatePath, pjName, pjAddress, project, pr, bDateStr, eDateStr);
		//将字节数组输出到客户端
		OutputStream out=null;
		try {
			String fileName = URLEncoder.encode(pjName+"监测数据"+bDateStr.split(" ")[0]+"至"+eDateStr.split(" ")[0], "UTF-8");
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachement;fileName=" + fileName + ".xlsx");
			out = response.getOutputStream();
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
