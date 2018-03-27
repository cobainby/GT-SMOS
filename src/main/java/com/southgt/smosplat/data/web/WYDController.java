package com.southgt.smosplat.data.web;

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
import com.southgt.smosplat.data.util.math.Station;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYDService;
import com.southgt.smosplat.project.service.IWarnningDataService;

/**
 * 
 * 水准数据控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月27日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller
public class WYDController {
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	ISurveyPoint_WYDService sp_WYDService;
	
	@Resource
	IWYDLevelDataService wydLevelDataService;
	
	@Resource
	IWarnningDataService warningDataService;
	
	@Resource
	IProjectService projectService;
	
	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;
	
	//手机端将水准仪导出的数据进行上传
	@RequestMapping("/saveManualWYDLevelData")
	@ResponseBody
	public String saveManualWYDLevelData(String jsonLevelData, String sourceData, HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		//手动解析全站仪导出的数据并插入数据库
		try {
			map = wydLevelDataService.checkLevelData(jsonLevelData, sourceData);
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg",e.toString());
			e.printStackTrace();
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getLatestWYDLevelDatasByProject")
	@ResponseBody
	public String getLatestWYDLevelDatasByProject(HttpSession session){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = (Project) session.getAttribute("currentProject");
		try {
			map = wydLevelDataService.getLatestWYDLevelDatasByProject(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/getLatestWYDLevelDatas4App")
	@ResponseBody
	public String getLatestWYDLevelDatas4App(String projectUuid){
		Map<String, Object> map = new HashMap<String,Object>();
		//从会话中得到项目id
		Project project = projectService.getEntity(projectUuid);
		try {
			map = wydLevelDataService.getLatestWYDLevelDatasByProject(project);
			map.put("projectUuid", project.getProjectUuid());
			map.put("result", 0);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result",-1);
			map.put("msg", e.toString());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	
	
	@RequestMapping("/uploadWYDDatas")
	@ResponseBody
	public String uploadWYDDatas(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String,Object>();
		Project project=(Project) request.getSession().getAttribute("currentProject");
		MonitorItem monitorItem = monitorItemService.getMonitorItemByNumber(5);//监测项
		//水平位移这个项目下所有的监测点
		List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(), monitorItem.getMonitorItemUuid());
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
						s=new String(data,"GBK");
						//选择了文件但是文件内容为空
						if(s==null||s.equals("")){
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
							double cal_H = 0.0;//解算高程
							String code = "";//点名
							for(int m = 0; m < indexs.size(); m++){
								String row = rows[indexs.get(m)];
								String[]col = row.split("\t",-1);
								if(col.length > 1){
									code = col[1].trim();
									cal_H += Double.parseDouble(col[14].trim());
								}
							}
							double size = indexs.size();
							if(size > 0){
								cal_H /= size;
							}
							String pn = code;
							List<SurveyPoint_WYD> sp = sps.stream().filter(p -> p.getCode().equals(pn)).collect(Collectors.toList()); 
							WYD_LevelData hData = new WYD_LevelData();
							hData.setLevelH(cal_H);
							hData.setSurveyTime(date);
							if(sp.size() > 0){
								hData.setSurveyPoint(sp.get(0));
								wydLevelDataService.saveEntity(hData);
							}else{
								pointsWithoutEstablished.add(code);
							}
							String organUuid = sps.get(0).getProject().getOrgan().getOrganUuid();
							String projectUuid = sps.get(0).getProject().getProjectUuid();
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
							String millionTime = sdf1.format(date);
							//保存原始数据源文件
							FileUtil.saveSourceFile(organUuid, projectUuid, uploadFileSrc, millionTime,"WYD", s);
						}
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
