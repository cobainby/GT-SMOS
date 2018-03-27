package com.southgt.smosplat.project.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.util.WebServiceUtil;
import com.southgt.smosplat.data.dao.ICXDataDao;
import com.southgt.smosplat.data.dao.ILZDataDao;
import com.southgt.smosplat.data.dao.IMTDataDao;
import com.southgt.smosplat.data.dao.ISMDataDao;
import com.southgt.smosplat.data.dao.ISWDataDao;
import com.southgt.smosplat.data.dao.IWYDLevelDataDao;
import com.southgt.smosplat.data.dao.IWYSCoordDataDao;
import com.southgt.smosplat.data.dao.IZCDao;
import com.southgt.smosplat.data.dao.IZGDDataDao;
import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.data.entity.LZ_Data;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.entity.SM_Data;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.entity.WYD_LevelData;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.ZC_Data;
import com.southgt.smosplat.data.entity.ZGD_Data;
import com.southgt.smosplat.data.util.math.GtMath;
import com.southgt.smosplat.project.entity.CableMeter;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;
import com.southgt.smosplat.project.entity.Section;
import com.southgt.smosplat.project.entity.SurveyPoint_CX;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_MT;
import com.southgt.smosplat.project.entity.SurveyPoint_SM;
import com.southgt.smosplat.project.entity.SurveyPoint_SW;
import com.southgt.smosplat.project.entity.SurveyPoint_WYD;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;
import com.southgt.smosplat.project.entity.SurveyPoint_ZGD;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.ICableMeterService;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectMonitorItemService;
import com.southgt.smosplat.project.service.IProjectService;
import com.southgt.smosplat.project.service.ISurveyPoint_CXService;
import com.southgt.smosplat.project.service.ISurveyPoint_LZService;
import com.southgt.smosplat.project.service.ISurveyPoint_MTService;
import com.southgt.smosplat.project.service.ISurveyPoint_SMService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYDService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZCService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZGDService;
import com.southgt.smosplat.project.service.IWarnningDataService;
@Service("warnningDataService")
public class WarnningDataServiceImpl implements IWarnningDataService{
	
	@Value("#{iisIpHostProperties['iisRemoteIpPort']}")
	private  String iisRemoteIpPort;
	@Resource
	IProjectMonitorItemService projectMonitorItemService;
	
	@Resource
	ISurveyPoint_WYSService sp_WYSService;
	
	@Resource 
	ISurveyPoint_SWService sp_SWService;
	
	@Resource
	ISurveyPoint_MTService sp_MTService;
	
	@Resource
	ISurveyPoint_ZGDService sp_ZGDService;
	
	@Resource
	ISurveyPoint_WYDService sp_WYDService;
	
	@Resource 
	ISurveyPoint_LZService sp_LZService;
	
	@Resource 
	ISurveyPoint_CXService sp_CXService;
	
	@Resource
	ISurveyPoint_ZCService sp_ZCService;
	
	@Resource
	ISurveyPoint_SMService sp_SMService;
	
	@Resource
	IWYSCoordDataDao wysCoordDataDao;
	
	@Resource
	IProjectService projectService;
	
	@Resource
	ICableMeterService cableMeterService;
	
	@Resource 
	ISWDataDao swDataDao;
	
	@Resource
	IMTDataDao mtDataDao;
	
	@Resource
	IZGDDataDao zgdDataDao;
	
	@Resource
	ISMDataDao smDataDao;
	
	@Resource
	IWYDLevelDataDao wydDataDao;
	
	@Resource
	ILZDataDao lzDataDao;
	
	@Resource 
	ICXDataDao cxDataDao;
	
	@Resource
	IZCDao zcDataDao;
	
	@Resource
	IMonitorItemService monitorItemService;
	
	static double INVALID_VALUE = 1e10;
	
	private boolean hasMonitorItemByNumber(List<ProjectMonitorItem> projectMonitorItems,byte number){
		for (ProjectMonitorItem projectMonitorItem : projectMonitorItems) {
			if(projectMonitorItem.getMonitorItem().getNumber()==number){
				return true;
			}
		}
		return false;
	}
	@Override
	public void calWarnningOffsetByMonitorItem(String monitorCode,Project project, String phoneNums){
		//监测项名称
		String monitorItemName = monitorItemService.getMonitorItemByCode(monitorCode).getMonitorItemName();
		//监测时间
		Date surveyTime = new Date();
		if(monitorCode.equals("WYS")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//单次变化速率超过预警值部分
			//超过报警值部分
//			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<WYS_CoordData> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(1).getMonitorItemUuid());
			
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<WYS_CoordData> firstData = new ArrayList<>();
				for(int k = 0; k < sps.size(); k++){
					List<String> spUuids = new ArrayList<>();
					spUuids.add(sps.get(k).getSurveyPointUuid());
					List<WYS_CoordData> tempList = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spUuids);
					if(tempList.size() > 0){
						firstData.add(tempList.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					double aveX =0.0;
					double aveY = 0.0;
					List<WYS_CoordData> firstThreeData = wysCoordDataDao.getFirstThreeDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveX += firstThreeData.get(kk).getCaculateN();
							aveY += firstThreeData.get(kk).getCaculateE();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCaculateE(aveY / firstThreeData.size());
						firstThreeData.get(0).setCaculateN(aveX / firstThreeData.size());
					}
					List<WYS_CoordData> latestTwoData = wysCoordDataDao.getLatestTwoWYSCoordDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					
					
					Section section = latestTwoData.get(0).getSurveyPoint().getSection();
					//根据断面设置的点名得到以第一次测量为基准的断面的起始点和终止点的坐标数据
					//端面起始点
					List<WYS_CoordData> start = null;
					start = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getStartPointName())).collect(Collectors.toList());
					List<WYS_CoordData> end = null;
					//断面终止点
					end = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getEndPointName())).collect(Collectors.toList());
					if(start.size() == 0 || end.size() == 0){
						//如果断面选择的起始点和终止点没有测到数据
						continue;
					}
					double sectionAzimuth = GtMath.calculateSectionAngle(start.get(0), end.get(0));
					if(latestTwoData.size() == 2){
						// 东单次位移,化为毫米
						gapOffset = GtMath.y_Displacement(latestTwoData.get(0).getCaculateN(),
								latestTwoData.get(0).getCaculateE(), sectionAzimuth,
								latestTwoData.get(1).getCaculateN(), latestTwoData.get(1).getCaculateE())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<WYS_CoordData> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getSurveyTime();
						// 东累计位移，化为毫米
						accumOffset = GtMath.y_Displacement(latestTwoData.get(0).getCaculateN(),
								latestTwoData.get(0).getCaculateE(), sectionAzimuth,
								firstThreeData.get(0).getCaculateN(), firstThreeData.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumEOffset(accumOffset);
						latestTwoData.get(0).setGapEOffset(gapOffset);
						latestTwoData.get(0).setChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
							//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
							if(!latestTwoData.get(0).getCoordDataUuid().equals(sps.get(i).getProcessedDataUuid())){
								//累计变化量绝对值超过累计变化量报警值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumEOffset()) > Math.abs(warning.getWarnAccum())){
									accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumEOffset()));
									accumWarningVal.add(warning.getWarnAccum());
									pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getCoordDataUuid());
									if(!warnPoints.contains(sps.get(i).getCode())){
										warnPoints.add(sps.get(i).getCode());
									}
									//累计变化量绝对值超过累计变化量控制值绝对值
									if(Math.abs(latestTwoData.get(0).getAccumEOffset()) > Math.abs(warning.getControlAccum())){
										controlPoints.add(sps.get(i).getCode());
										accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumEOffset()));
										accumControlWarningVal.add(warning.getControlAccum());
									}
								}
								//单次变化速率超限
								if(Math.abs(latestTwoData.get(0).getChangeRate()) > Math.abs(warning.getWarnSingleRate())){
									gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getChangeRate()));
									gapRateWarningVal.add(warning.getWarnSingleRate());
									gapWarnPoints.add(sps.get(i).getCode());
									pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getCoordDataUuid());
								}else{
								}
							}
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"mm）"+"已超过控制值（"+accumControlWarningVal.get(j)+"mm);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"mm）"+"已超过报警值（"+accumWarningVal.get(j)+"mm);";
				}
			}
			
			if(gapRateWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化速率（"+gapRateWarningOffset.get(j)+"mm/d）"+"已超过报警值（"+gapRateWarningVal.get(j)+"mm/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}			
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			data.put("pointsExpect2Process", pointsExpect2Process);
//			dataList.put("WYS", data);
		}//围护墙(边坡)顶部竖向位移
		if(monitorCode.equals("WYD")){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//超过报警值部分
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<WYD_LevelData> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(5).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<LZ_Data> firstData = smDataDao.getFirstSM_DataBySurveyPoint(spUuids);
				List<WYD_LevelData> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<WYD_LevelData> firstThreeData = wydDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
		//			List<WYS_CoordData> firstData = ((IWYSCoordDataDao)getDao()).getFirstWYSCoordDataBySurveyPoint(sp);
					List<WYD_LevelData> latestTwoData = wydDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<WYD_LevelData> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getSurveyTime();
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getLevelDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLevelDataUuid());
								accumWarningVal.add(warning.getWarnAccum());
								if(!warnPoints.contains(sps.get(i).getCode())){
									warnPoints.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									controlPoints.add(sps.get(i).getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						}
							
						//单次变化速率绝对值超过单次变化速率预警部分
							//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getLevelDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapHChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLevelDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									gapWarnPoints.add(sps.get(i).getCode());
									gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHChangeRate()));
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"mm）"+"已超过控制值（"+accumControlWarningVal.get(j)+"mm);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"mm）"+"已超过报警值（"+accumWarningVal.get(j)+"mm);";
				}
			}
			
			if(gapRateWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化速率（"+gapRateWarningOffset.get(j)+"mm/d）"+"已超过报警值（"+gapRateWarningVal.get(j)+"mm/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}				
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
//			dataList.put("WYD", data);
		}//立柱竖向位移
		if(monitorCode.equals("LZ")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//超过报警值部分
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<LZ_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(6).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<LZ_Data> firstData = smDataDao.getFirstSM_DataBySurveyPoint(spUuids);
				List<LZ_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<LZ_Data> firstThreeData = lzDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<LZ_Data> latestTwoData = lzDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
		//			List<WYS_CoordData> firstData = ((IWYSCoordDataDao)getDao()).getFirstWYSCoordDataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<LZ_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getSurveyTime();
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getLzDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLzDataUuid());
								accumWarningVal.add(warning.getWarnAccum());
								if(!warnPoints.contains(sps.get(i).getCode())){
									warnPoints.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									controlPoints.add(sps.get(i).getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						}
							
						//单次变化速率绝对值超过单次变化速率预警部分
							//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getLzDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLzDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									gapWarnPoints.add(sps.get(i).getCode());
									gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()));
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"mm）"+"已超过控制值（"+accumControlWarningVal.get(j)+"mm);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"mm）"+"已超过报警值（"+accumWarningVal.get(j)+"mm);";
				}
			}
			
			if(gapRateWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化速率（"+gapRateWarningOffset.get(j)+"mm/d）"+"已超过报警值（"+gapRateWarningVal.get(j)+"mm/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}				
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
//			dataList.put("LZ", data);
		}//周边建筑物竖向位移
		if(monitorCode.equals("SM")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//超过报警值部分
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<SM_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SM> sps = sp_SMService.getSP_SMs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(8).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<SM_Data> firstData = smDataDao.getFirstSM_DataBySurveyPoint(spUuids);
				List<SM_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<SM_Data> firstThreeData = smDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SM_Data> latestTwoData = smDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<SM_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getSurveyTime();
						// 东累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getSmDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSmDataUuid());
								accumWarningVal.add(warning.getWarnAccum());
								if(!warnPoints.contains(sps.get(i).getCode())){
									warnPoints.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									controlPoints.add(sps.get(i).getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						}
							
						//单次变化速率绝对值超过单次变化速率预警部分
							//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getSmDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSmDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									gapWarnPoints.add(sps.get(i).getCode());
									gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()));
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"mm）"+"已超过控制值（"+accumControlWarningVal.get(j)+"mm);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"mm）"+"已超过报警值（"+accumWarningVal.get(j)+"mm);";
				}
			}
			
			if(gapRateWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化速率（"+gapRateWarningOffset.get(j)+"mm/d）"+"已超过报警值（"+gapRateWarningVal.get(j)+"mm/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}			
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
//			dataList.put("SM", data);
		}//周边管线竖向位移
		if(monitorCode.equals("ZGD")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//超过报警值部分
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<ZGD_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZGD> sps = sp_ZGDService.getSP_ZGDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(10).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				List<ZGD_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<ZGD_Data> firstThreeData = zgdDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZGD_Data> latestTwoData = zgdDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<ZGD_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getSurveyTime();
						// 东累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getZgdDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZgdDataUuid());
								accumWarningVal.add(warning.getWarnAccum());
								if(!warnPoints.contains(sps.get(i).getCode())){
									warnPoints.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									controlPoints.add(sps.get(i).getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						}
							
						//单次变化速率绝对值超过单次变化速率预警部分
							//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getZgdDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapHChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZgdDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									gapWarnPoints.add(sps.get(i).getCode());
									gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHChangeRate()));
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"mm）"+"已超过控制值（"+accumControlWarningVal.get(j)+"mm);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"mm）"+"已超过报警值（"+accumWarningVal.get(j)+"mm);";
				}
			}
			
			if(gapRateWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化速率（"+gapRateWarningOffset.get(j)+"mm/d）"+"已超过报警值（"+gapRateWarningVal.get(j)+"mm/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}				
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
//			dataList.put("ZGD", data);
		}//水位
		if(monitorCode.equals("SW")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//超过报警值部分
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<SW_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SW> sps = sp_SWService.getSP_SWs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(12).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<SW_Data> firstData = swDataDao.getFirstOneSWDatasBySurveyPoints(spUuids);
				List<SW_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<SW_Data> firstThreeData = swDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getCalValue();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCalValue(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SW_Data> latestTwoData = swDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<SW_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getCollectTime();
						// 东累计位移，化为毫米
						accumOffset = latestTwoData.get(0).getCalValue() - tempFirst.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumOffset(accumOffset);
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getSwDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumOffset()) > Math.abs(warning.getWarnAccum())){
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSwDataUuid());
								accumWarningVal.add(warning.getWarnAccum());
								if(!warnPoints.contains(sps.get(i).getCode())){
									warnPoints.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumOffset()) > Math.abs(warning.getControlAccum())){
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
									controlPoints.add(sps.get(i).getCode());
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						}
						//单次变化速率绝对值超过单次变化速率预警部分
							//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getSwDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSwDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									gapWarnPoints.add(sps.get(i).getCode());
									gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
								gapRateWarningVal.add(warning.getWarnSingleRate());
							}
						}
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			
			
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"mm）"+"已超过控制值（"+accumControlWarningVal.get(j)+"mm);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"mm）"+"已超过报警值（"+accumWarningVal.get(j)+"mm);";
				}
			}
			
			if(gapRateWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化速率（"+gapRateWarningOffset.get(j)+"mm/d）"+"已超过报警值（"+gapRateWarningVal.get(j)+"mm/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}			
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
//			dataList.put("SW", data);
		}//轴力
		if(monitorCode.equals("ZC")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//本次內力變化速率超过报警值部分
			List<Double> calValWarningOffset = new ArrayList<>();
			List<Float>  calValWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> calValPointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<ZC_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZC> sps = sp_ZCService.getSP_ZCs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(15).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<ZC_Data> firstData = zcDataDao.getFirstOneZCDatasBySurveyPoints(spUuids);
				List<ZC_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<ZC_Data> firstThreeData = zcDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getCalValue();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCalValue(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZC_Data> latestTwoData = zcDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<ZC_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getCollectTime();
						// 东累计位移，化为毫米
//						accumOffset = latestTwoData.get(0).getCalValue() - tempFirst.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
//						BigDecimal bb = new BigDecimal(accumOffset); 
//						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
//						latestTwoData.get(0).setAccumOffset(accumOffset);
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//累计变化量绝对值超过累计变化量预警值绝对值
//						if(Math.abs(latestTwoData.get(0).getAccumOffset()) > Math.abs(warning.getEarlyAccum())){
							//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
							if(!latestTwoData.get(0).getZcDataUuid().equals(sps.get(i).getProcessedDataUuid())){
//								accumEarlyWarningPoints.add(sps.get(i).getCode());
//								accumEarlyWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
//								accumEarlyWarningVal.add(warning.getEarlyAccum());
								//累计变化量绝对值超过累计变化量报警值绝对值
								if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getWarnAccum())){
									warnPoints.add(sps.get(i).getCode());
									accumWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
									accumWarningVal.add(warning.getWarnAccum());
	//								//累计变化量绝对值超过累计变化量控制值绝对值
									if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getControlAccum())){
										controlPoints.add(sps.get(i).getCode());
										accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
										accumControlWarningVal.add(warning.getControlAccum());
									}
								}
							}
							calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZcDataUuid());
//						}
						//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getZcDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								calValWarningOffset.add(Math.abs(latestTwoData.get(0).getGapChangeRate()));
								calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZcDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									gapWarnPoints.add(sps.get(i).getCode());
								}
								calValWarningVal.add(warning.getWarnSingleRate());
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"kn）"+"已超过控制值（"+accumControlWarningVal.get(j)+"kn);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"kn）"+"已超过报警值（"+accumWarningVal.get(j)+"kn);";
				}
			}
			
			if(calValWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化量（"+calValWarningOffset.get(j)+"kn/d）"+"已超过报警值（"+calValWarningOffset.get(j)+"kn/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}					
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("gapChangeRateWarningOffset", calValWarningOffset);
			data.put("gapChangeRateWarningVal", calValWarningVal);
			
			data.put("pointsExpect2Process", calValPointsExpect2Process);
			
//			dataList.put("ZC", data);
		}//锚索
		if(monitorCode.equals("MT")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//本次內力變化速率超过报警值部分
			List<Double> calValWarningOffset = new ArrayList<>();
			List<Float>  calValWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> calValPointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<MT_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_MT> sps = sp_MTService.getSP_MTs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(18).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<MT_Data> firstData = mtDataDao.getFirstOneMTDatasBySurveyPoints(spUuids);
				List<MT_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<MT_Data> firstThreeData = mtDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getCalValue();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCalValue(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<MT_Data> latestTwoData = mtDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<MT_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						surveyTime = latestTwoData.get(0).getCollectTime();
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						//报警情况
						Warning warning = sps.get(i).getWarning();
						String spUuid = sps.get(i).getSurveyPointUuid();
						//找出这个点下锚索的个数
						List<CableMeter> cableMeters = cableMeterService.getCableMeterBySP_mt(spUuid);
						//累计变化量绝对值超过累计变化量预警值绝对值
//						if(Math.abs(latestTwoData.get(0).getAccumOffset()) > Math.abs(warning.getEarlyAccum())){
							//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
							if(!latestTwoData.get(0).getMtDataUuid().equals(sps.get(i).getProcessedDataUuid())){
//								accumEarlyWarningPoints.add(sps.get(i).getCode());
//								accumEarlyWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
//								accumEarlyWarningVal.add(warning.getEarlyAccum());
								//累计变化量绝对值超过累计变化量报警值绝对值,回来数据中的锚索模数不足不报警
								if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getWarnAccum()) && (latestTwoData.get(0).getModuleData().split(",")).length == cableMeters.size()){
									warnPoints.add(sps.get(i).getCode());
									accumWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
									accumWarningVal.add(warning.getWarnAccum());
	//								//累计变化量绝对值超过累计变化量控制值绝对值
									if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getControlAccum())){
										controlPoints.add(sps.get(i).getCode());
										accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
										accumControlWarningVal.add(warning.getControlAccum());
									}
								}
							}
							calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getMtDataUuid());
//						}
						//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getMtDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值,锚索模数全部回来才报警
							if(Math.abs(latestTwoData.get(0).getGapChangeRate()) > Math.abs(warning.getWarnSingleRate()) && (latestTwoData.get(0).getModuleData().split(",")).length == cableMeters.size()){
								calValWarningOffset.add(Math.abs(latestTwoData.get(0).getGapChangeRate()));
								calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getMtDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									gapWarnPoints.add(sps.get(i).getCode());
								}
								calValWarningVal.add(warning.getWarnSingleRate());
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"kn）"+"已超过控制值（"+accumControlWarningVal.get(j)+"kn);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"kn）"+"已超过报警值（"+accumWarningVal.get(j)+"kn);";
				}
			}
			
			if(calValWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化量（"+calValWarningOffset.get(j)+"kn/d）"+"已超过报警值（"+calValWarningOffset.get(j)+"kn/d);";
				}
			}


			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}					
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("gapChangeRateWarningOffset", calValWarningOffset);
			data.put("gapChangeRateWarningVal", calValWarningVal);
			
			data.put("pointsExpect2Process", calValPointsExpect2Process);
//			dataList.put("MT", data);
		}//深层水平测斜
		if(monitorCode.equals("CX")){
			Map<String, Object> data = new HashMap<String, Object>();
			//超过报警值点号
			List<String> warnPoints = new ArrayList<>();
			//超过控制值点号
			List<String> controlPoints = new ArrayList<>();
			//变化速率超限的点
			List<String> gapWarnPoints = new ArrayList<>();
			//超过报警值部分
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<CX_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_CX> sps = sp_CXService.getSP_CXs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(4).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<CX_Data> latestCXData = cxDataDao.getLatestOneCXDataBySurveyPoint(sp);
					if(latestCXData != null && latestCXData.size() > 0){
						surveyTime = latestCXData.get(0).getCollectTime();
						//报警情况
						Warning warning = latestCXData.get(0).getSurveyPoint().getWarning();
						
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestCXData.get(0).getCxDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestCXData.get(0).getAccumOffset()) > Math.abs(warning.getWarnAccum())){
								accumWarningOffset.add(Math.abs(latestCXData.get(0).getAccumOffset()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestCXData.get(0).getCxDataUuid());
								if(!warnPoints.contains(sps.get(i).getCode())){
									warnPoints.add(sps.get(i).getCode());
								}
								accumWarningVal.add(warning.getWarnAccum());
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestCXData.get(0).getAccumOffset()) > Math.abs(warning.getControlAccum())){
									accumControlWarningOffset.add(Math.abs(latestCXData.get(0).getAccumOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
									controlPoints.add(sps.get(i).getCode());
								}
								
							}
						}
							
						//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestCXData.get(0).getCxDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestCXData.get(0).getChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								gapRateWarningOffset.add(Math.abs(latestCXData.get(0).getChangeRate()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestCXData.get(0).getCxDataUuid());
								if(!gapWarnPoints.contains(sps.get(i).getCode())){
									gapWarnPoints.add(sps.get(i).getCode());
								}
								gapRateWarningVal.add(warning.getWarnSingleRate());
							}
						
							recentData.add(latestCXData.get(0));
						}
					}
				}
			}
			String message = "";
			
			if(accumControlWarningOffset.size() > 0){
				if(Integer.valueOf(project.getWarningStatus()) < 2){
					//工程状态，超控制值
					project.setWarningStatus("2");
				}
				for(int j = 0; j < controlPoints.size(); j++){
					message += controlPoints.get(j) + "累计变化量（"+accumControlWarningOffset.get(j)+"mm）"+"已超过控制值（"+accumControlWarningVal.get(j)+"mm);";
				}
			}
			
			if(accumWarningOffset.size() > 0){
				//工程状态，超报警值。如果已经有报警状态，不更新
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < warnPoints.size(); j++){
					if(message.contains(warnPoints.get(j))){
						//如已超控制值，累计变化报警值就不输出这个点
						continue;
					}
					message += warnPoints.get(j) + "累计变化量（"+accumWarningOffset.get(j)+"mm）"+"已超过报警值（"+accumWarningVal.get(j)+"mm);";
				}
			}
			
			if(gapRateWarningOffset.size() > 0){
				//工程状态，超控制值
				if(project.getWarningStatus() == "" || project.getWarningStatus().equals("0")){
					project.setWarningStatus("1");
				}
				for(int j = 0; j < gapWarnPoints.size(); j++){
					message += gapWarnPoints.get(j) + "单次变化速率（"+gapRateWarningOffset.get(j)+"mm/d）"+"已超过报警值（"+gapRateWarningVal.get(j)+"mm/d);";
				}
			}

			if(phoneNums != "" && warnPoints.size() > 0){
				String[] params = {"MonitorItemName","ProjectName","Adress","Phones","SMSContent","OrganName","Contacts","SurveyTime"};
				String[] value = {monitorItemName, project.getProjectName(),project.getAddress(),phoneNums,message,project.getOrgan().getOrganName(),project.getMonitorLeader(),surveyTime.toString()};
				WebServiceUtil.invokeService(iisRemoteIpPort, "http://tempuri.org/", "SendMsg", params, value);
			}					
			data.put("code", warnPoints);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);                                                                                                                                                                                                            
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
//			dataList.put("CX", data);
		}
		//更新一下工程状态
		projectService.updateWarningStatus(project.getProjectUuid(), project.getWarningStatus());
	}
	
	@Override
	public void calWarnningOffset(Map<String, Object> dataList,Project project) {
		//工程是否超限
		String isSafe = "0";
		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		//水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//单次变化速率超过预警值部分
//			List<String> gapRateEarlyWarningPoints = new ArrayList<>();
//			List<Double> gapRateEarlyWarningOffset = new ArrayList<>();
//			List<Float>  gapRateEarlyWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过控制值部分
//			List<String> gapRateControlWarningPoints = new ArrayList<>();
//			List<Double> gapRateControlWarningOffset = new ArrayList<>();
//			List<Float>  gapRateControlWarningVal = new ArrayList<>();
			//累计变化量超过预警值部分
//			List<String> accumEarlyWarningPoints = new ArrayList<>();
//			List<Double> accumEarlyWarningOffset = new ArrayList<>();
//			List<Float>  accumEarlyWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlWarningPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
//			Map<String, Object> accumPointsExpect2Process = new HashMap<>();
			Map<String, Object> pointsExpect2Process = new HashMap<>();
//			Map<String, Object> gapChangeRatePointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<WYS_CoordData> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(1).getMonitorItemUuid());
			
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<WYS_CoordData> firstData = new ArrayList<>();
				for(int k = 0; k < sps.size(); k++){
					List<String> spUuids = new ArrayList<>();
					spUuids.add(sps.get(k).getSurveyPointUuid());
					List<WYS_CoordData> tempList = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spUuids);
					if(tempList.size() > 0){
						firstData.add(tempList.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					double aveX =0.0;
					double aveY = 0.0;
					List<WYS_CoordData> firstThreeData = wysCoordDataDao.getFirstThreeDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveX += firstThreeData.get(kk).getCaculateN();
							aveY += firstThreeData.get(kk).getCaculateE();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCaculateE(aveY / firstThreeData.size());
						firstThreeData.get(0).setCaculateN(aveX / firstThreeData.size());
					}
					List<WYS_CoordData> latestTwoData = wysCoordDataDao.getLatestTwoWYSCoordDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					
					
					Section section = latestTwoData.get(0).getSurveyPoint().getSection();
					//根据断面设置的点名得到以第一次测量为基准的断面的起始点和终止点的坐标数据
					//端面起始点
					List<WYS_CoordData> start = null;
					start = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getStartPointName())).collect(Collectors.toList());
					List<WYS_CoordData> end = null;
					//断面终止点
					end = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getEndPointName())).collect(Collectors.toList());
					if(start.size() == 0 || end.size() == 0){
						//如果断面选择的起始点和终止点没有测到数据
						continue;
					}
					double sectionAzimuth = GtMath.calculateSectionAngle(start.get(0), end.get(0));
					if(latestTwoData.size() == 2){
						// 东单次位移,化为毫米
						gapOffset = GtMath.y_Displacement(latestTwoData.get(0).getCaculateN(),
								latestTwoData.get(0).getCaculateE(), sectionAzimuth,
								latestTwoData.get(1).getCaculateN(), latestTwoData.get(1).getCaculateE())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<WYS_CoordData> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						// 东累计位移，化为毫米
						accumOffset = GtMath.y_Displacement(latestTwoData.get(0).getCaculateN(),
								latestTwoData.get(0).getCaculateE(), sectionAzimuth,
								firstThreeData.get(0).getCaculateN(), firstThreeData.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumEOffset(accumOffset);
						latestTwoData.get(0).setGapEOffset(gapOffset);
						latestTwoData.get(0).setChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
							//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
							if(!latestTwoData.get(0).getCoordDataUuid().equals(sps.get(i).getProcessedDataUuid())){
								//累计变化量绝对值超过累计变化量报警值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumEOffset()) > Math.abs(warning.getWarnAccum())){
									isSafe = "1";
									accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumEOffset()));
									accumWarningVal.add(warning.getWarnAccum());
									pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getCoordDataUuid());
									if(!points.contains(sps.get(i).getCode())){
										points.add(sps.get(i).getCode());
									}
									//累计变化量绝对值超过累计变化量控制值绝对值
									if(Math.abs(latestTwoData.get(0).getAccumEOffset()) > Math.abs(warning.getControlAccum())){
										isSafe = "2";
										accumControlWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
										accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumEOffset()));
										accumControlWarningVal.add(warning.getControlAccum());
									}else{
										//不存在的值
										accumControlWarningOffset.add(INVALID_VALUE);
										accumControlWarningVal.add(warning.getControlAccum());
									}
								}
							
								if(Math.abs(latestTwoData.get(0).getChangeRate()) > Math.abs(warning.getWarnSingleRate())){
									if(!isSafe.equals("2")){
										isSafe = "1";
									}
									gapRateWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getChangeRate()));
									gapRateWarningVal.add(warning.getWarnSingleRate());
									pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getCoordDataUuid());
									if(!points.contains(sps.get(i).getCode())){
										//速率超了，累计没超
										points.add(sps.get(i).getCode());
										accumWarningOffset.add(INVALID_VALUE);
										accumWarningVal.add(warning.getWarnAccum());
										accumControlWarningOffset.add(INVALID_VALUE);
										accumControlWarningVal.add(warning.getControlAccum());
									}
								}else{
									if(points.contains(sps.get(i).getCode())){
										//速率没超限，但累计变化超限了
										gapRateWarningOffset.add(INVALID_VALUE);
										gapRateWarningVal.add(warning.getWarnSingleRate());
									}
								}
							}
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			data.put("code", points);
//			data.put("accumEarlyWarningPoints", accumEarlyWarningPoints);
//			data.put("accumEarlyWarningOffset", accumEarlyWarningOffset);
//			data.put("accumEarlyWarningVal", accumEarlyWarningVal);
			
			
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
//			data.put("gapChangeRateEarlyWarningPoints", gapRateEarlyWarningPoints);
//			data.put("gapChangeRateEarlyWarningOffset", gapRateEarlyWarningOffset);
//			data.put("gapChangeRateEarlyWarningVal", gapRateEarlyWarningVal);
//			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("gapChangeRateWarningPoints", gapRateWarningPoints);
			data.put("accumControlWarningPoints", accumControlWarningPoints);
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
//			data.put("gapChangeRateControlWarningPoints", gapRateControlWarningPoints);
//			data.put("gapChangeRateControlWarningOffset", gapRateControlWarningOffset);
//			data.put("gapChangeRateControlWarningVal", gapRateControlWarningVal);
//			data.put("accumPointsExpect2Process", accumPointsExpect2Process);
//			data.put("gapChangeRatePointsExpect2Process", gapChangeRatePointsExpect2Process);
			data.put("pointsExpect2Process", pointsExpect2Process);
			dataList.put("WYS", data);
		}//围护墙(边坡)顶部竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//超过报警值部分
			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlWarningPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<WYD_LevelData> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(5).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<WYD_LevelData> firstData = lzDataDao.getFirstLZ_DataBySurveyPoint(spUuids);
				
				List<WYD_LevelData> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<WYD_LevelData> firstThreeData = wydDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					double aveH =0.0;
					List<WYD_LevelData> firstThreeData = wydDataDao.getFirstThreeDataBySurveyPoint(sp);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的高赋成头三条高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
					}
					List<WYD_LevelData> latestTwoData = wydDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<WYD_LevelData> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getLevelDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								isSafe = "1";
								accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								accumWarningVal.add(warning.getWarnAccum());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLevelDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									isSafe = "2";
									accumControlWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}else{
									//不存在的值
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						
							if(Math.abs(latestTwoData.get(0).getGapHChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								if(!isSafe.equals("2")){
									isSafe = "1";
								}
								gapRateWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHChangeRate()));
								gapRateWarningVal.add(warning.getWarnSingleRate());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLevelDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									points.add(sps.get(i).getCode());
									accumWarningOffset.add(INVALID_VALUE);
									accumWarningVal.add(warning.getWarnAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}else{
								if(points.contains(sps.get(i).getCode())){
									//速率没超限，但累计变化超限了
									gapRateWarningOffset.add(INVALID_VALUE);
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("gapChangeRateWarningPoints", gapRateWarningPoints);
			data.put("accumControlWarningPoints", accumControlWarningPoints);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
			dataList.put("WYD", data);
		}//立柱竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//超过报警值部分
			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlWarningPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<LZ_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(6).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<LZ_Data> firstData = lzDataDao.getFirstLZ_DataBySurveyPoint(spUuids);
				
				List<LZ_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<LZ_Data> firstThreeData = lzDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<LZ_Data> latestTwoData = lzDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
		//			List<WYS_CoordData> firstData = ((IWYSCoordDataDao)getDao()).getFirstWYSCoordDataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<LZ_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getLzDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								isSafe = "1";
								accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								accumWarningVal.add(warning.getWarnAccum());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLzDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									isSafe = "2";
									accumControlWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}else{
									//不存在的值
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						
							if(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								if(!isSafe.equals("2")){
									isSafe = "1";
								}
								gapRateWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()));
								gapRateWarningVal.add(warning.getWarnSingleRate());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getLzDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									points.add(sps.get(i).getCode());
									accumWarningOffset.add(INVALID_VALUE);
									accumWarningVal.add(warning.getWarnAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}else{
								if(points.contains(sps.get(i).getCode())){
									//速率没超限，但累计变化超限了
									gapRateWarningOffset.add(INVALID_VALUE);
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("gapChangeRateWarningPoints", gapRateWarningPoints);
			data.put("accumControlWarningPoints", accumControlWarningPoints);
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
			dataList.put("LZ", data);
		}//周边建筑物竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//超过报警值部分
			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlWarningPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<SM_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SM> sps = sp_SMService.getSP_SMs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(8).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<SM_Data> firstData = smDataDao.getFirstSM_DataBySurveyPoint(spUuids);
				
				List<SM_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<SM_Data> firstThreeData = smDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SM_Data> latestTwoData = smDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<SM_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						// 东累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getSmDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								isSafe = "1";
								accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								accumWarningVal.add(warning.getWarnAccum());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSmDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									isSafe = "2";
									accumControlWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}else{
									//不存在的值
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						
							if(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								if(!isSafe.equals("2")){
									isSafe = "1";
								}
								gapRateWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHOffsetChangeRate()));
								gapRateWarningVal.add(warning.getWarnSingleRate());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSmDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									points.add(sps.get(i).getCode());
									accumWarningOffset.add(INVALID_VALUE);
									accumWarningVal.add(warning.getWarnAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}else{
								if(points.contains(sps.get(i).getCode())){
									//速率没超限，但累计变化超限了
									gapRateWarningOffset.add(INVALID_VALUE);
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("gapChangeRateWarningPoints", gapRateWarningPoints);
			data.put("accumControlWarningPoints", accumControlWarningPoints);
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
			dataList.put("SM", data);
		}//周边管线竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//超过报警值部分
			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlWarningPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<ZGD_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZGD> sps = sp_ZGDService.getSP_ZGDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(10).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				List<ZGD_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<ZGD_Data> firstThreeData = zgdDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getLevelH();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setLevelH(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				
				
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZGD_Data> latestTwoData = zgdDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<ZGD_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						// 东累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getZgdDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getWarnAccum())){
								isSafe = "1";
								accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
								accumWarningVal.add(warning.getWarnAccum());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZgdDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumHOffset()) > Math.abs(warning.getControlAccum())){
									isSafe = "2";
									accumControlWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumHOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}else{
									//不存在的值
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						
							if(Math.abs(latestTwoData.get(0).getGapHChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								if(!isSafe.equals("2")){
									isSafe = "1";
								}
								gapRateWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapHChangeRate()));
								gapRateWarningVal.add(warning.getWarnSingleRate());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZgdDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									points.add(sps.get(i).getCode());
									accumWarningOffset.add(INVALID_VALUE);
									accumWarningVal.add(warning.getWarnAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}else{
								if(points.contains(sps.get(i).getCode())){
									//速率没超限，但累计变化超限了
									gapRateWarningOffset.add(INVALID_VALUE);
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("gapChangeRateWarningPoints", gapRateWarningPoints);
			data.put("accumControlWarningPoints", accumControlWarningPoints);
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
			dataList.put("ZGD", data);
		}//水位
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//超过报警值部分
			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlWarningPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<SW_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SW> sps = sp_SWService.getSP_SWs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(12).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<SW_Data> firstData = swDataDao.getFirstOneSWDatasBySurveyPoints(spUuids);
				
				List<SW_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<SW_Data> firstThreeData = swDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getCalValue();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCalValue(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SW_Data> latestTwoData = swDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<SW_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						// 东累计位移，化为毫米
						accumOffset = latestTwoData.get(0).getCalValue() - tempFirst.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumOffset(accumOffset);
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getSwDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestTwoData.get(0).getAccumOffset()) > Math.abs(warning.getWarnAccum())){
								isSafe = "1";
								accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSwDataUuid());
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
								accumWarningVal.add(warning.getWarnAccum());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
								}
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getAccumOffset()) > Math.abs(warning.getControlAccum())){
									isSafe = "2";
									accumControlWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}else{
									//没超累计控制值
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						}
						//单次变化速率绝对值超过单次变化速率预警部分
							//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getSwDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								if(!isSafe.equals("2")){
									isSafe = "1";
								}
								gapRateWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								gapRateWarningOffset.add(Math.abs(latestTwoData.get(0).getGapChangeRate()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getSwDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									//速率超了，累计没超
									points.add(sps.get(i).getCode());
									accumWarningOffset.add(INVALID_VALUE);
									accumWarningVal.add(warning.getWarnAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
								gapRateWarningVal.add(warning.getWarnSingleRate());
							}else{
								if(points.contains(sps.get(i).getCode())){
									//速率没超限，但累计变化超限了
									gapRateWarningOffset.add(INVALID_VALUE);
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("gapChangeRateWarningPoints", gapRateWarningPoints);
			data.put("accumControlWarningPoints", accumControlWarningPoints);
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
			dataList.put("SW", data);
		}//轴力
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 15)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//本次內力變化速率超过报警值部分
			List<String> calValWarningPoints = new ArrayList<>();
			List<Double> calValWarningOffset = new ArrayList<>();
			List<Float>  calValWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> calValPointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<ZC_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZC> sps = sp_ZCService.getSP_ZCs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(15).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<ZC_Data> firstData = zcDataDao.getFirstOneZCDatasBySurveyPoints(spUuids);
				
				List<ZC_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<ZC_Data> firstThreeData = zcDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getCalValue();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCalValue(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZC_Data> latestTwoData = zcDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<ZC_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						// 东累计位移，化为毫米
//						accumOffset = latestTwoData.get(0).getCalValue() - tempFirst.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
//						BigDecimal bb = new BigDecimal(accumOffset); 
//						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
//						latestTwoData.get(0).setAccumOffset(accumOffset);
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//累计变化量绝对值超过累计变化量预警值绝对值
//						if(Math.abs(latestTwoData.get(0).getAccumOffset()) > Math.abs(warning.getEarlyAccum())){
							//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
							if(!latestTwoData.get(0).getZcDataUuid().equals(sps.get(i).getProcessedDataUuid())){
//								accumEarlyWarningPoints.add(sps.get(i).getCode());
//								accumEarlyWarningOffset.add(Math.abs(latestTwoData.get(0).getAccumOffset()));
//								accumEarlyWarningVal.add(warning.getEarlyAccum());
								//累计变化量绝对值超过累计变化量报警值绝对值
								if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getWarnAccum())){
									isSafe = "1";
									points.add(sps.get(i).getCode());
									accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
									accumWarningVal.add(warning.getWarnAccum());
	//								//累计变化量绝对值超过累计变化量控制值绝对值
									if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getControlAccum())){
										isSafe = "2";
										points.add(sps.get(i).getCode());
										accumControlPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
										accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
										accumControlWarningVal.add(warning.getControlAccum());
									}else{
										//不存在的值
										accumControlWarningOffset.add(INVALID_VALUE);
										accumControlWarningVal.add(warning.getControlAccum());
									}
								}
							}
							calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZcDataUuid());
//						}
						//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestTwoData.get(0).getZcDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								if(!isSafe.equals("2")){	
									isSafe = "1";
								}
								calValWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								calValWarningOffset.add(Math.abs(latestTwoData.get(0).getGapChangeRate()));
								calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getZcDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
								calValWarningVal.add(warning.getWarnSingleRate());
							}else{
								if(points.contains(sps.get(i).getCode())){
									//速率没超限，但累计变化超限了
									calValWarningOffset.add(INVALID_VALUE);
									calValWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			
			if(accumWarningOffset.size() > 0 || calValWarningOffset.size() > 0 ){
				//工程状态，超报警值
				project.setWarningStatus("1");
			}
			if(accumControlWarningOffset.size() > 0){
				//工程状态，超控制值
				project.setWarningStatus("2");
			}
			
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("calValWarningPoints", calValWarningPoints);
			data.put("accumControlPoints", accumControlPoints);
			
			data.put("gapChangeRateWarningOffset", calValWarningOffset);
			data.put("gapChangeRateWarningVal", calValWarningVal);
			
			data.put("pointsExpect2Process", calValPointsExpect2Process);
			
			dataList.put("ZC", data);
		}//锚索
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//本次內力變化速率超过报警值部分
			List<String> calValWarningPoints = new ArrayList<>();
			List<Double> calValWarningOffset = new ArrayList<>();
			List<Float>  calValWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> calValPointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<MT_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_MT> sps = sp_MTService.getSP_MTs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(18).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
//				List<MT_Data> firstData = mtDataDao.getFirstOneMTDatasBySurveyPoints(spUuids);
				
				List<MT_Data> firstData = new ArrayList<>();
				
				for (int i = 0; i < sps.size(); i++) {
					String spUuid = sps.get(i).getSurveyPointUuid();
						
					List<String> spId = new ArrayList<String>();
					spId.add(spUuid);
					double aveH = 0.0;
					//头三条数据
					List<MT_Data> firstThreeData = mtDataDao.getFirstThreeDataBySurveyPoint(spId);
					if(firstThreeData.size() > 0){
						for(int kk = 0; kk < firstThreeData.size(); kk ++){
							aveH += firstThreeData.get(kk).getCalValue();
						}
						//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
						firstThreeData.get(0).setCalValue(aveH / firstThreeData.size());
						firstData.add(firstThreeData.get(0));
					}
				}
				
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<MT_Data> latestTwoData = mtDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(latestTwoData.get(1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						//取整数位，进1法。1.1->2,0.9->1;
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						changeRate = gapOffset / days;
						bb = new BigDecimal(gapOffset);
						//精确到0.1mm
						gapOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.1mm
						changeRate = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
					String pn = sps.get(i).getCode();
					List<MT_Data> tempFirst = firstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(pn)).collect(Collectors.toList());
					if(tempFirst.size() > 0 && latestTwoData.size() > 0){
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						//报警情况
						Warning warning = latestTwoData.get(0).getSurveyPoint().getWarning();
						//找出这个点下锚索的个数
						List<CableMeter> cableMeters = cableMeterService.getCableMeterBySP_mt(sps.get(i).getSurveyPointUuid());
						//累计变化量绝对值超过累计变化量预警值绝对值
							//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestTwoData.get(0).getMtDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值,模数值返回不全不报警
							if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getWarnAccum()) && (latestTwoData.get(0).getModuleData().split(",")).length == cableMeters.size()){
								isSafe = "1";
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
								}
								accumWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								accumWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
								accumWarningVal.add(warning.getWarnAccum());
//								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestTwoData.get(0).getCalValue()) > Math.abs(warning.getControlAccum())){
									isSafe = "2";
									if(!points.contains(sps.get(i).getCode())){
										points.add(sps.get(i).getCode());
									}
									accumControlPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
									accumControlWarningOffset.add(Math.abs(latestTwoData.get(0).getCalValue()));
									accumControlWarningVal.add(warning.getControlAccum());
								}else{
									//不存在的值
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
							}
						}
						calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getMtDataUuid());
						if(!latestTwoData.get(0).getMtDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestTwoData.get(0).getGapChangeRate()) > Math.abs(warning.getWarnSingleRate()) && (latestTwoData.get(0).getModuleData().split(",")).length == cableMeters.size()){
								if(!isSafe.equals("2")){
									isSafe = "1";
								}
								calValWarningPoints.add(latestTwoData.get(0).getSurveyPoint().getCode());
								calValWarningOffset.add(Math.abs(latestTwoData.get(0).getGapChangeRate()));
								calValPointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestTwoData.get(0).getMtDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
									accumWarningOffset.add(INVALID_VALUE);
									accumWarningVal.add(warning.getWarnAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
								calValWarningVal.add(warning.getWarnSingleRate());
							}else{
								if(points.contains(sps.get(i).getCode())){
									//速率没超限，但累计变化超限了
									calValWarningOffset.add(INVALID_VALUE);
									calValWarningVal.add(warning.getWarnSingleRate());
								}
							}
						}
						
						recentData.add(latestTwoData.get(0));
					}
				}
			}
			
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("calValWarningPoints", calValWarningPoints);
			data.put("accumControlPoints", accumControlPoints);
			
			data.put("gapChangeRateWarningOffset", calValWarningOffset);
			data.put("gapChangeRateWarningVal", calValWarningVal);
			
			data.put("pointsExpect2Process", calValPointsExpect2Process);
			dataList.put("MT", data);
		}//深层水平测斜
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4)){
			Map<String, Object> data = new HashMap<String, Object>();
			//點號列表
			List<String> points = new ArrayList<>();
			//超过报警值部分
			List<String> gapRateWarningPoints = new ArrayList<>();
			List<Double> gapRateWarningOffset = new ArrayList<>();
			List<Float>  gapRateWarningVal = new ArrayList<>();
			//超过报警值部分
			List<String> accumWarningPoints = new ArrayList<>();
			List<Double> accumWarningOffset = new ArrayList<>();
			List<Float>  accumWarningVal = new ArrayList<>();
			//超过控制值部分
			List<String> accumControlWarningPoints = new ArrayList<>();
			List<Double> accumControlWarningOffset = new ArrayList<>();
			List<Float>  accumControlWarningVal = new ArrayList<>();
			//将要处理的点Uuid和对应的数据Uuid
			Map<String, Object> pointsExpect2Process = new HashMap<>();
			//最新的一条数据
			List<CX_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_CX> sps = sp_CXService.getSP_CXs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(4).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<CX_Data> latestCXData = cxDataDao.getLatestOneCXDataBySurveyPoint(sp);
					if(latestCXData != null && latestCXData.size() > 0){
						//报警情况
						Warning warning = latestCXData.get(0).getSurveyPoint().getWarning();
						//对比监测点的数据。如果不相等，就是没有处理的，若超限正常报警
						if(!latestCXData.get(0).getCxDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//累计变化量绝对值超过累计变化量报警值绝对值
							if(Math.abs(latestCXData.get(0).getAccumOffset()) > Math.abs(warning.getWarnAccum())){
								isSafe = "1";
								accumWarningPoints.add(latestCXData.get(0).getSurveyPoint().getCode());
								accumWarningOffset.add(Math.abs(latestCXData.get(0).getAccumOffset()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestCXData.get(0).getCxDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
								}
								accumWarningVal.add(warning.getWarnAccum());
								//累计变化量绝对值超过累计变化量控制值绝对值
								if(Math.abs(latestCXData.get(0).getAccumOffset()) > Math.abs(warning.getControlAccum())){
									isSafe = "2";
									accumControlWarningPoints.add(latestCXData.get(0).getSurveyPoint().getCode());
									accumControlWarningOffset.add(Math.abs(latestCXData.get(0).getAccumOffset()));
									accumControlWarningVal.add(warning.getControlAccum());
								}else{
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
								
							}
						}
							
						//对比监测点的数据id?？？？看这个点有没有处理？？？？
						if(!latestCXData.get(0).getCxDataUuid().equals(sps.get(i).getProcessedDataUuid())){
							//单次变化速率绝对值超过单次变化速率报警值绝对值
							if(Math.abs(latestCXData.get(0).getChangeRate()) > Math.abs(warning.getWarnSingleRate())){
								if(!isSafe.equals("2")){
									isSafe = "1";
								}
								gapRateWarningPoints.add(latestCXData.get(0).getSurveyPoint().getCode());
								gapRateWarningOffset.add(Math.abs(latestCXData.get(0).getChangeRate()));
								pointsExpect2Process.put(sps.get(i).getCode(),sps.get(i).getSurveyPointUuid() + ',' + latestCXData.get(0).getCxDataUuid());
								if(!points.contains(sps.get(i).getCode())){
									points.add(sps.get(i).getCode());
									accumWarningOffset.add(INVALID_VALUE);
									accumWarningVal.add(warning.getWarnAccum());
									accumControlWarningOffset.add(INVALID_VALUE);
									accumControlWarningVal.add(warning.getControlAccum());
								}
								gapRateWarningVal.add(warning.getWarnSingleRate());
							}else{
								if(points.contains(sps.get(i).getCode())){
									gapRateWarningOffset.add(INVALID_VALUE);
									gapRateWarningVal.add(warning.getWarnSingleRate());
								}
							}
						
							recentData.add(latestCXData.get(0));
						}
					}
				}
			}
			data.put("code", points);
			data.put("accumWarningOffset", accumWarningOffset);
			data.put("accumWarningVal", accumWarningVal);
			
			data.put("accumControlWarningOffset", accumControlWarningOffset);
			data.put("accumControlWarningVal", accumControlWarningVal);
			
			data.put("accumWarningPoints", accumWarningPoints);
			data.put("gapChangeRateWarningPoints", gapRateWarningPoints);
			data.put("accumControlWarningPoints", accumControlWarningPoints);
			
			data.put("gapChangeRateWarningOffset", gapRateWarningOffset);
			data.put("gapChangeRateWarningVal", gapRateWarningVal);
			
			data.put("pointsExpect2Process", pointsExpect2Process);
			dataList.put("CX", data);
		}
		dataList.put("isSafe",isSafe);
	}

	@Override
	public void processPoints(Project project,String point, String flag) {
		String[] pointAndData = point.split(","); 
		if(pointAndData.length == 2){
			if(flag.equals("WYS")){
				SurveyPoint_WYS sp = sp_WYSService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_WYSService.updateEntity(sp);
			}
			if(flag.equals("WYD")){
				SurveyPoint_WYD sp = sp_WYDService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_WYDService.updateEntity(sp);
			}
			if(flag.equals("CX")){
				SurveyPoint_CX sp = sp_CXService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_CXService.updateEntity(sp);
			}
			if(flag.equals("SM")){
				SurveyPoint_SM sp = sp_SMService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_SMService.updateEntity(sp);
			}
			if(flag.equals("LZ")){
				SurveyPoint_LZ sp = sp_LZService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_LZService.updateEntity(sp);
			}
			if(flag.equals("ZGD")){
				SurveyPoint_ZGD sp = sp_ZGDService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_ZGDService.updateEntity(sp);
			}
			if(flag.equals("SW")){
				SurveyPoint_SW sp = sp_SWService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_SWService.updateEntity(sp);
			}
			if(flag.equals("ZC")){
				SurveyPoint_ZC sp = sp_ZCService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_ZCService.updateEntity(sp);
			}
			if(flag.equals("MT")){
				SurveyPoint_MT sp = sp_MTService.getEntity(pointAndData[0]);
				//处理过的点，记录数据uuid
				sp.setProcessedDataUuid(pointAndData[1]);
				sp_MTService.updateEntity(sp);
			}
			Map<String,Object> dataList = new HashMap();
			//处理完后，调一次计算超限的方法
			calWarnningOffset(dataList, project);
			String isSafe = (String)dataList.get("isSafe");
			//处理后，更新一下工程超限状态
			project.setWarningStatus(isSafe);
			projectService.updateWarningStatus(project.getProjectUuid(), isSafe);
		}
		
	}
}
