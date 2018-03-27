package com.southgt.smosplat.data.web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.southgt.smosplat.common.util.FileUtil;
import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.data.entity.WYD_LevelData;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.WYS_OriginalData;
import com.southgt.smosplat.data.service.IWYDLevelDataService;
import com.southgt.smosplat.data.service.IWYSCoordDataService;
import com.southgt.smosplat.data.service.IWYSService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.data.util.math.Station;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYDService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;
import com.southgt.smosplat.project.service.IWarnningDataService;

/**
 * 围护墙(边坡)顶部水平位移数据展示前端控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月8日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class WYSController {
	
	@Resource
	IProjectService projectService;
	
	@Resource
	IWYSService wysService;
	
	@Resource
	IWYDLevelDataService wydLevelDataService;
	
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	ISurveyPoint_WYSService sp_WYSService;
	
	@Resource
	ISurveyPoint_WYDService sp_WYDService;
	
	@Resource
	IWYSCoordDataService wysCoordDataService;
	
	@Resource
	IWarnningDataService warningDataService;
	
	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;
	
	//获取项目下的所有监测点的手动模式下全站仪导出的数据
	@RequestMapping("/getLatestWYSCoordDatasByProject")
	@ResponseBody
	public String getLatestWYSCoordDatasByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = wysCoordDataService.getLatestWYSCoordDatasByProject(project);
			map.put("result", 0);
			map.put("projectUuid", project.getProjectUuid());
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
	
	//获取项目下的所有监测点的手动模式下全站仪导出的数据
	@RequestMapping("/getLatestWYSCoordDatasByProject4App")
	@ResponseBody
	public String getLatestWYSCoordDatasByProject4App(String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = projectService.getEntity(projectUuid);
		try {
			map = wysCoordDataService.getLatestWYSCoordDatasByProject(project);
			map.put("result", 0);
			map.put("projectUuid", project.getProjectUuid());
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.toString());
		}
		return JsonUtil.beanToJson(map);
	}
	
	
	
	
	//检查手机端将全站仪导出的数据
	@RequestMapping("/checkManualWYSData")
	@ResponseBody
	public String checkManualWYSData(String tsData,String sourceData, HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String, Object> result = new HashMap<String,Object>();
		List<String> gapRateWarningPoints = new ArrayList<>();
		List<String> accumWarningPoints = new ArrayList<>();
		List<String> accumControlWarningPoints = new ArrayList<>();
		List<WYS_CoordData> coord = new ArrayList<>();
		List<WYS_OriginalData> originalData = new ArrayList<>();
		List<WYD_LevelData> levelH = new ArrayList<>();
		//超限数据
		Map<String, Object> overrun = new HashMap<String,Object>();
		//所有的坐标点解算数据
		List<WYS_CoordData> resultData = new ArrayList<>();
		
		Warning hWarning = null;
		//手动解析全站仪导出的数据并插入数据库
		try {
			
			/****************传入的角度单位需统一为十进制的角度单位***************/
			map = wysService.checkManualWYSData(tsData, sourceData);
//			List<Station> stations = (List<Station>) map.get("stationList");
			List<SurveyPoint_WYS> extraPointCode = (List<SurveyPoint_WYS>)map.get("extraPointCode");
			Optional<List<Station>> stations = Optional.ofNullable((List<Station>)map.get("stationList"));
			if(stations.isPresent()){
				for(Station s : stations.get()){
					Optional<List<WYS_OriginalData>> lstOriginalData = Optional.ofNullable((List<WYS_OriginalData>) map.get(s.getName()));
					Optional<List<WYD_LevelData>> lstLevelH = Optional.ofNullable((List<WYD_LevelData>) map.get(s.getName()+"levelH"));
					if(lstOriginalData.isPresent()){
						List<SurveyPoint_WYD> wydPoints = sp_WYDService.getSP_WYDs(lstOriginalData.get().get(0).getSurveyPoint().getProject().getProjectUuid());
						if(wydPoints.size() == 0){
							throw(new Exception("请先设置（边坡）竖向位移监测点！"));
						}
						hWarning = wydPoints.get(0).getWarning();
						//计算，平差所有的点
						List<WYS_CoordData> tempCoord = wysCoordDataService.checkCoordData(lstOriginalData.get(), s);
						resultData.addAll(tempCoord);
						originalData.addAll(lstOriginalData.get());
						levelH.addAll(lstLevelH.get());
					}
				}
			}
			//计算单次变化速率，累计位移
			overrun = wysCoordDataService.calOffset(resultData, hWarning);
			gapRateWarningPoints = (List<String>) overrun.get("gapRateWarningPoints");
			accumWarningPoints = (List<String>) overrun.get("accumWarningPoints");
			accumControlWarningPoints = (List<String>) overrun.get("accumControlWarningPoints");
			coord = (List<WYS_CoordData>) overrun.get("coordData");
			
			result.put("extraPointCode", extraPointCode);
			result.put("lstTsOriginalData", originalData);
			result.put("lstLevelH", levelH);
			result.put("gapRateWarningPoints", gapRateWarningPoints);
			result.put("accumWarningPoints", accumWarningPoints);
			result.put("accumControlWarningPoints", accumControlWarningPoints);
			result.put("coordData", coord);
			result.put("result", 0);
		} catch (Exception e) {
			result.put("result", -1);
			result.put("msg",e.toString());
			e.printStackTrace();
		}
		String s=JsonUtil.beanToJson(result);
		return s;
	}
	
	//检查手机端将全站仪导出的数据
	@RequestMapping("/testWarn")
	@ResponseBody
	public String testWarn(String projectUuid,String phones,HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project = projectService.getEntity(projectUuid);
		//计算是否超限并更新工程状态。
		warningDataService.calWarnningOffsetByMonitorItem("WYS", project,phones);
		warningDataService.calWarnningOffsetByMonitorItem("WYD", project,phones);
		return "";
	}
	
	
	//保存手机端将全站仪导出的数据
	@RequestMapping("/saveWYSData")
	@ResponseBody
	public String saveWYSData(String projectUuid,String oriData, String coordData, String gapRateEarlyWarningPoints, String accumEarlyWarningPoints,String flag,HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//0是只保存没超限部分的数据，1是全部保存。
		byte bFlag = Byte.valueOf(flag);
		try{
			Project project = projectService.getEntity(projectUuid);
			List<String> gapWarningPoints = JsonUtil.jsonToList(gapRateEarlyWarningPoints, String.class);
			List<String> accumWarningPoints = JsonUtil.jsonToList(accumEarlyWarningPoints, String.class);
			
			List<WYS_OriginalData> ts_Data =  JsonUtil.jsonToList(oriData, WYS_OriginalData.class);
			List<WYS_CoordData> coord_Data =  JsonUtil.jsonToList(coordData, WYS_CoordData.class);
			//获取wyd监测点
			List<SurveyPoint_WYD> wydPoints = sp_WYDService.getSP_WYDs(projectUuid);
			//保存水平位移原始数据
			wysService.saveManualWYSData(ts_Data, gapWarningPoints, accumWarningPoints, bFlag);
			//边坡高程
			List<WYD_LevelData> hData = new ArrayList<>();
			coord_Data.stream().forEach((p)->{
				WYD_LevelData hItem = new WYD_LevelData();
				List<SurveyPoint_WYD> point = wydPoints.stream().filter(q->q.getCode().equals(p.getSurveyPoint().getCode())).collect(Collectors.toList());
				if(point.size() > 0){
					hItem.setLevelH(p.getCaculateH());
					hItem.setSurveyPoint(point.get(0));
					hItem.setSurveyTime(p.getSurveyTime());
					hData.add(hItem);
				}
			});
			//保存高程数据
			wydLevelDataService.saveManualWYDLevelData(hData, gapWarningPoints, accumWarningPoints, bFlag);
			//保存解算后数据
			wysCoordDataService.saveCoordData(coord_Data, gapWarningPoints, accumWarningPoints, bFlag);
			map.put("result", 0);
		}catch(Exception ex){
			ex.printStackTrace();
			map.put("result", -1);
			map.put("msg", ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/calWYSWarnSendText")
	@ResponseBody
	public String calWYSWarnSendText(String projectUuid, HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String,Object>();
		try{
			Project project = projectService.getEntity(projectUuid);
			//发短信给项目监测人员和监测负责人。
			String phones = "";
			String contacts = project.getMonitorWorker();
			contacts += project.getMonitorLeader();
			while(contacts.contains("(")||contacts.contains(")")){
				String temp = contacts.substring(contacts.indexOf("(")+1,contacts.indexOf(")"));
				if(!phones.contains(temp)){
					phones += temp+",";
				}
				contacts = contacts.substring(contacts.indexOf(")")+1);
			}
			//计算是否超限并更新工程状态。
			warningDataService.calWarnningOffsetByMonitorItem("WYS", project,phones);
			warningDataService.calWarnningOffsetByMonitorItem("WYD", project,phones);
			map.put("result", 0);
		}catch(Exception ex){
			map.put("result", -1);
			map.put("msg", "发短信预警失败！"+ex.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/uploadWYSDatas")
	@ResponseBody
	public String uploadWYSDatas(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project=(Project) request.getSession().getAttribute("currentProject");
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(1);//监测项
		//水平位移这个项目下所有的监测点
		List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(project.getProjectUuid(), monitorItem.getMonitorItemUuid());
		List<String> pointsWithoutEstablished = new ArrayList<>();//后台没有建点，但是数据有这些点
//		List<String> pointsWithoutData = new ArrayList<>();//后台有建点，但是没有数据
		CommonsMultipartResolver MultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		if(MultipartResolver.isMultipart(request)){
			//将request转换成可以处理multipart类型的request
			MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
			Iterator<String> it=multipartRequest.getFileNames();
			String s="";
			//没有选择文件
			if(!it.hasNext()){
				map.put("result", -1);
				map.put("msg", "没有选择文件！");
				return JsonUtil.beanToJson(map);
			}
			while (it.hasNext()) {
				String paramName = (String) it.next();
				MultipartFile file=multipartRequest.getFile(paramName);
				if(file!=null){
					byte[] data;
					try {
						data = file.getBytes();
						s = new String(data,"GBK");
						//选择了文件但是文件内容为空
						if(s == null || s.equals("")){
							map.put("result", -1);
							map.put("msg", "数据长度为0！");
							return JsonUtil.beanToJson(map);
						}
						//解析字符串并入库
						String[] rows = s.split("\r\n", -1);
						String pName = rows[0].split("\t",-1)[1];//第一行的点名
						//所有点的点名
						List<String> names = new ArrayList<>();
						Map<String, Object> points = new HashMap<>();
						Date date = new Date();
						for(int i = 0; i < rows.length; i++){
							String[] col = rows[i].split("\t",-1);
							if(col.length > 1){
								if(!names.contains(col[1])){
									//点名的list
									names.add(col[1]);
								}
							}

						}
						for(int i = 0; i < names.size(); i++){
							List<Integer> samePointIndex = new ArrayList<>();
							for(int j = 0; j < rows.length; j++){
								String[] params = rows[j].split("\t",-1);
								if(params.length > 1){
									if(params[1].equals(names.get(i))){
										pName = params[1];
										samePointIndex.add(j);
									}
								}
							}
							//根据点名做key,同一个点下的索引做value
							points.put(pName, samePointIndex);
						}
						for(int k = 0; k < names.size(); k++){
							List<Integer> indexs = (List<Integer>) points.get(names.get(k));
							double cal_Va = 0.0;//解算竖直角
							double cal_Ha = 0.0;//解算水平角
							double cal_Sd = 0.0;//解算斜距
							double cal_E = 0.0;//解算东坐标
							double cal_N = 0.0;//解算北坐标
							double cal_H = 0.0;//解算高程
							String code = "";//点名
							for(int m = 0; m < indexs.size(); m++){
								String row = rows[indexs.get(m)];
								String[]col = row.split("\t",-1);
								if(col.length > 1){
									code = col[1].trim();
									cal_Va += GtMath.angleToRadian(col[4].trim());//度分秒转为弧度
									cal_Ha += GtMath.angleToRadian(col[5].trim());
									cal_Sd += Double.parseDouble(col[6].trim());
									cal_E += Double.parseDouble(col[12].trim());
									cal_N += Double.parseDouble(col[13].trim());
									cal_H += Double.parseDouble(col[14].trim());
								}
							}
							double size = indexs.size();
							if(size > 0){
								cal_Va /= size;
								cal_Ha /= size;
								cal_Sd /= size;
								cal_H /= size;
								cal_N /= size;
								cal_E /= size;
							}
							String pn = code;
							List<SurveyPoint_WYS> sp = sps.stream().filter(p -> p.getCode().equals(pn)).collect(Collectors.toList()); 
							WYS_CoordData coordData = new WYS_CoordData();
							coordData.setCaculateE(cal_E);
							coordData.setCaculateH(cal_H);
							coordData.setCaculateN(cal_N);
							coordData.setCaculateHA(cal_Ha);
							coordData.setCaculateVA(cal_Va);
							coordData.setCaculateSD(cal_Sd);
							coordData.setSurveyTime(date);
							if(sp.size() > 0){
								coordData.setSurveyPoint(sp.get(0));
								wysCoordDataService.saveEntity(coordData);
							}else{
								pointsWithoutEstablished.add(code);
							}
						}
						String organUuid = sps.get(0).getProject().getOrgan().getOrganUuid();
						String projectUuid = sps.get(0).getProject().getProjectUuid();
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
						String millionTime = sdf1.format(date);
						//保存原始数据源文件
						FileUtil.saveSourceFile(organUuid, projectUuid, uploadFileSrc, millionTime,"WYS", s);
					} catch (Exception e) {
						e.printStackTrace();
						map.put("result", -1);
						map.put("msg", "数据格式错误！");
						return JsonUtil.beanToJson(map);
					}
				}
			}
		}
		String msg = "";
		if(pointsWithoutEstablished.size() > 0){
			for(String code : pointsWithoutEstablished){
				msg += code+",";
			}
			msg += "以上的点没有在后台建点，请核查！";
			map.put("msg", msg);
		}
		map.put("result", 0);
		return JsonUtil.beanToJson(map);
	}
}
