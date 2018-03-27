package com.southgt.smosplat.data.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.common.util.FileUtil;
import com.southgt.smosplat.common.web.FileController;
import com.southgt.smosplat.data.dao.IWYSDao;
import com.southgt.smosplat.data.entity.WYD_LevelData;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.WYS_OriginalData;
import com.southgt.smosplat.data.service.IWYSService;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.data.util.math.Station;
import com.southgt.smosplat.project.entity.MonitorItem;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.Section;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPointService;

/**
 * 水平位移原始数据服务实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月8日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("wysService")
public class WYSServiceImpl extends BaseServiceImpl<WYS_OriginalData> implements IWYSService {
	
	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;
	@Resource
	ISurveyPointService surveyPointServcie;
	
	@Resource
	IWYSService wysService;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IProjectService projectService;

	@Resource(name="wysDao")
	@Override
	public void setDao(IBaseDao<WYS_OriginalData> dao) {
		super.setDao(dao);
	}

	
	public List<WYS_CoordData> calculateCoordData(List<WYS_OriginalData> originalDatas){
		SurveyPoint_WYS sp = originalDatas.get(0).getSurveyPoint();
		List<WYS_CoordData> coordDatas = new ArrayList<WYS_CoordData>();
		//找出原始数据里面所有时间点
		List<Date> times = new ArrayList<Date>();
		for (int i = 0; i < originalDatas.size(); i++) {
			boolean finded = false;
			for (int j = 0; j < times.size(); j++) {
				if(times.get(j).getTime() == originalDatas.get(i).getSurveyTime().getTime()){
					finded = true;
					break;
				}
			}
			if(!finded){
				times.add(originalDatas.get(i).getSurveyTime());
			}
		}
		//根据每个时间点计算坐标数据
		for (int i = 0; i < times.size(); i++) {
			Date d=times.get(i);
			// 根据时刻，测点id匹配出上面根据最新11个时刻取出的所有数据
			List<WYS_OriginalData> datasAtSameTime = new ArrayList<WYS_OriginalData>();
			datasAtSameTime = originalDatas.stream().filter(p -> p.getSurveyTime().equals(d)).collect(Collectors.toList());
			WYS_CoordData coord = average(datasAtSameTime);
			coord.setSurveyTime(d);
			//不需要返回那么多数据
			SurveyPoint_WYS tempSp = new SurveyPoint_WYS();
			tempSp.setSurveyPointUuid(sp.getSurveyPointUuid());
			tempSp.setCode(sp.getCode());
			tempSp.setSection(sp.getSection());
			coord.setSurveyPoint(tempSp);
			coordDatas.add(coord);
		}
		return coordDatas;
	}
	
	public WYS_CoordData average(List<WYS_OriginalData>lstData){
		WYS_CoordData item = new WYS_CoordData();
		double sumVa = 0.0;
		double sumHa = 0.0;
		double sumSd = 0.0;
		double sumE = 0.0;
		double sumN = 0.0;
		double sumH = 0.0;
		//测量错误的距离个数
		int errorSdNum = 0;
		for(WYS_OriginalData data : lstData){
			//统一归化到盘左
			if(data.getVa() > 180){
				sumVa += (360 - data.getVa());
				double temp = data.getHa() - 180;
				if(temp >= 0){
					sumHa += temp;
				}else{
					sumHa += temp + 360;
				}
			}else{
				sumVa += data.getVa();
				sumHa += data.getHa();
			}
			if(data.getSd() != -1.0E10){
				sumSd += data.getSd();
			}else{
				//测距出错
				errorSdNum ++;
			}
			 sumE += data.getEast();
			 sumN += data.getNorth();
			 sumH += data.getHeight();
		}
		//原始数据求平均，作为解算数据
		item.setCaculateE(sumE/lstData.size());
		item.setCaculateN(sumN/lstData.size());
		item.setCaculateH(sumH/lstData.size());
		item.setCaculateVA(sumVa/lstData.size());
		item.setCaculateHA(sumHa/lstData.size());
		item.setCaculateSD(sumSd/(lstData.size() - errorSdNum));
		return item;
	}

	@Override
	public Map<String,Object> checkManualWYSData(String data, String sourceData) throws Exception {
		Map<String,Object>map = new HashMap();
		
		List<Station> lstStation = new ArrayList<>();
		//找出后台建了点但是上传数据中没有的点
		List<Integer> index = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		List<SurveyPoint_WYS> lstSurveyPoint = new ArrayList<>();
		List<WYD_LevelData> lstLevelData = new ArrayList<>();
		
		JsonNode root = null;
		try {
			root = mapper.readTree(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String projectUuid = root.get("projectUuid").asText();
		String surveyDateTime = root.get("surveyDateTime").asText();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//测量时间
		Date d = sdf.parse(surveyDateTime);
		
		if(!ifDataUnique(d, projectUuid)){
			throw(new Exception("上传的数据在数据库中已经存在！"));
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String millionTime = sdf1.format(d);
		String monitorItemUuid = root.get("monitorItemUuid").asText();
		
		//根据项目uuid，监测项uuid获取所有的监测点
		lstSurveyPoint = surveyPointServcie.getSurveyPoints(projectUuid, monitorItemUuid);
		if(lstSurveyPoint == null || lstSurveyPoint.size() == 0){
			throw(new Exception("请先在后台添加监测点！"));
		}
		map.put("surveyPointList", lstSurveyPoint);
		
		List<SurveyPoint_WYS> outList =new ArrayList<>();
		outList.addAll(lstSurveyPoint);
		//去除空值
		lstSurveyPoint.removeAll(Collections.singleton(null));
		JsonNode originalData = root.get("originalDataByStation");
		if (originalData.isArray()) {
		    for (Iterator it =  originalData.elements(); it.hasNext(); ){
				List<WYS_OriginalData> lstTsOriginalData = new ArrayList<>();
		    	boolean left = true;
				boolean right = true;
		        JsonNode avator = (JsonNode) it.next();
		        JsonNode s = avator.get("station");
		        Station station = new Station();
		        station.setName(s.get("stationName").asText());
		        station.setEast(s.get("stationEast").asDouble());
		        station.setNorth(s.get("stationNorth").asDouble());
		        station.setHeight(s.get("stationHeight").asDouble());
		        station.setInstrumentHeight(s.get("stationInsHeight").asDouble());
		        lstStation.add(station);
		        JsonNode backPoints = avator.get("backPoints");
		        //后视点。单机版是以后视点列表最后一个点为定向点，现在手机版改成第一个点为定向点。
		        JsonNode backSight = null;
		        if (backPoints.isArray()) {
		        	backSight = (JsonNode) backPoints.elements().next();
		        }
		        JsonNode surveyPointData = avator.get("surveyPointData");
		        if (surveyPointData.isArray()) {
		    		//全圆观测的第一个/最后一个点，用来算全站仪转360度后水平角的误差。
		    		String firstName = "";
		        	//监测数据计数器
		        	for (Iterator itr = surveyPointData.elements(); itr.hasNext(); ){
		        		JsonNode point = (JsonNode) itr.next();
		        		Double va = point.get("va").asDouble();
		        		Double ha = point.get("ha").asDouble();
		        		Double prismH = point.get("prismHeight").asDouble();
		        		Double sd = point.get("sd").asDouble();
		        		String name = point.get("name").asText();
		        		if(firstName == ""){
		        			firstName = name;
		        		}
		        		Double east = point.get("east").asDouble();
		        		Double north = point.get("north").asDouble();
		        		Double height = point.get("height").asDouble();
		        		WYS_OriginalData item = new WYS_OriginalData();
		        		WYD_LevelData itemH = new WYD_LevelData();
						//根据天顶距判断盘左盘右。天顶距在0~180°为盘左，否则盘右
						if(va>=0&&va<=180){
							item.setFace((byte) 0);
						}else{
							item.setFace((byte) 1);
						}
						//和后视点同名的点，第一次观测盘左盘右都是1，第二次观测盘左盘右都是2
//						if(name.equals(backSight.get("name").asText())){
						if(name.equals(firstName)){
							if(item.getFace().equals((byte)0) && left){
								item.setPointType((byte) 1);
								left = false;
								right = true;
							}else if(item.getFace().equals((byte)0) && !left){
								item.setPointType((byte) 2);
							}
							if(item.getFace().equals((byte)1) && right){
								item.setPointType((byte) 1);
								right = false;
								left = true;
							}else if(item.getFace().equals((byte)1) && !right){
								item.setPointType((byte) 2);
							}
						}else{
							//普通监测点，都是3
							item.setPointType((byte) 3);
						}
						item.setHa(ha);
						item.setVa(va);
						item.setPrismH(prismH);
						item.setSd(sd);
						item.setEast(east);
						item.setNorth(north);
						item.setHeight(height);
						itemH.setLevelH(height);
						//入库时间
						item.setSurveyTime(d);
						//根据点名匹配出监测点
						List<SurveyPoint_WYS>surveyPoints = outList.stream().filter(p->p.getCode().equals(name)).collect(Collectors.toList());
						if(surveyPoints.size()>0){
							item.setSurveyPoint(surveyPoints.get(0));
							lstTsOriginalData.add(item);
							lstLevelData.add(itemH);						
						}
		        	}
		        }
		        if(lstTsOriginalData.size() == 0){
		        	throw(new Exception("上传的点点名全部有误，请核查！"));
		        }
		        map.put(station.getName(), lstTsOriginalData);
		        //边坡高程
		        map.put(station.getName()+"levelH", lstLevelData);
				for(int i = 0; i < lstTsOriginalData.size(); i++){
					for(int j = 0; j < outList.size(); j++){
						if(lstTsOriginalData.get(i).getSurveyPoint().getSurveyPointUuid().equals(outList.get(j).getSurveyPointUuid())){
							if(!index.contains(j)){
								index.add(j);
							}
						}
					}
				}
				for(int k = 0; k < index.size(); k++){
					lstSurveyPoint.remove(outList.get(k));
				}
		    }
		}
		
		Project project = projectService.getEntity(projectUuid);
		String organUuid = project.getOrgan().getOrganUuid();
			//保存原始数据源文件
		FileUtil.saveSourceFile(organUuid, projectUuid, uploadFileSrc, millionTime,"WYS", sourceData);
		FileUtil.saveSourceFile(organUuid, projectUuid, uploadFileSrc, millionTime,"WYD", sourceData);
		//lstSurveyPoint这个时候如果没空，剩下的就是后台没建点但是又上传了的点
		map.put("extraPointCode", lstSurveyPoint);
		map.put("stationList", lstStation);
		return map;
	}

	@Override
	public void deleteWYSLevelDataBySurveyPoint(String spUuid) {
		((IWYSDao)getDao()).deleteWYSLevelDataBySurveyPoint(spUuid);
		
	}

	@Override
	public void saveManualWYSData(List<WYS_OriginalData> data, List<String> gapRateEarlyWarningPoints, List<String> accumEarlyWarningPoints, byte flag) {
		if(flag == 0){
			//只上传不超限部分
			for(int i = 0; i< data.size(); i++){
				String pn = data.get(i).getSurveyPoint().getCode();
//				List<String> overrunChangeRatePoints = gapRateEarlyWarningPoints.stream().filter(p -> p.equals(pn)).collect(Collectors.toList());
//				List<String> overrunAccumPoints = accumEarlyWarningPoints.stream().filter(p -> p.equals(pn)).collect(Collectors.toList());
				List<String> overrunChangeRatePoints = new ArrayList<>();
				for(String p : gapRateEarlyWarningPoints){
					if(p.equals(pn)){
						overrunChangeRatePoints.add(p);
					}
				}
				List<String> overrunAccumPoints = new ArrayList<>();
				for(String q : accumEarlyWarningPoints){
					if(q.equals(pn)){
						overrunAccumPoints.add(q);
					}
				}
				if(overrunChangeRatePoints.size() > 0 || overrunAccumPoints.size() > 0){
					continue;
				}
				//原始数据入库
				this.saveEntity(data.get(i));
			}
		}else if(flag == 1){
			//全部上传
			for(int i = 0; i< data.size(); i++){
				//原始数据入库
				this.saveEntity(data.get(i));
			}
		}
	}


	@Override
	public boolean ifDataUnique(Date date, String projectUuid) {
		return ((IWYSDao)getDao()).ifDataUnique(date, projectUuid);
	}

}
