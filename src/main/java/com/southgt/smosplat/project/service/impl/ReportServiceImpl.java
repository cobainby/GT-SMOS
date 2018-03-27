package com.southgt.smosplat.project.service.impl;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import com.southgt.smosplat.data.vo.DailyReport_CX;
import com.southgt.smosplat.data.vo.DailyReport_DeepOffset;
import com.southgt.smosplat.data.vo.WeeklyReport_CX;
import com.southgt.smosplat.data.vo.WeeklyReport_DeepOffset;
import com.southgt.smosplat.data.vo.WeeklyReport_HorizontalOffset;
import com.southgt.smosplat.data.vo.WeeklyReport_LZ;
import com.southgt.smosplat.data.vo.WeeklyReport_MT;
import com.southgt.smosplat.data.vo.WeeklyReport_SM;
import com.southgt.smosplat.data.vo.WeeklyReport_SW;
import com.southgt.smosplat.data.vo.WeeklyReport_VerticalOffset;
import com.southgt.smosplat.data.vo.WeeklyReport_WYD;
import com.southgt.smosplat.data.vo.WeeklyReport_WYS;
import com.southgt.smosplat.data.vo.WeeklyReport_ZGD;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.project.dao.IProjectReportDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;
import com.southgt.smosplat.project.entity.ProjectReport;
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
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.IProjectMonitorItemService;
import com.southgt.smosplat.project.service.IReportService;
import com.southgt.smosplat.project.service.ISurveyPoint_CXService;
import com.southgt.smosplat.project.service.ISurveyPoint_LZService;
import com.southgt.smosplat.project.service.ISurveyPoint_MTService;
import com.southgt.smosplat.project.service.ISurveyPoint_SMService;
import com.southgt.smosplat.project.service.ISurveyPoint_SWService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYDService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZCService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZGDService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

/**
 * 报表导出接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年6月14日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("reportService")
public class ReportServiceImpl implements IReportService {

	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;
	
	//报表配置实例，所有请求使用同一个配置实例
	private Configuration configuration;
	
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
	IDeviceService deviceService;
	
	@Resource
	IWYSCoordDataDao wysCoordDataDao;
	
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
	
	@Resource 
	IProjectReportDao projectReportDao;
	
	
	@Override
	public ProjectReport getProjectReportByProject(String projectUuid) {
		return projectReportDao.getProjectReportByProject(projectUuid);
	}

	@Override
	public void operateProjectReport(ProjectReport projectReport,String projectUuid) {
		boolean b=projectReportDao.existOrNotProjectReport(projectUuid);
		if(b){
			projectReportDao.updateEntity(projectReport);
			
		}else{
			
			projectReportDao.saveEntity(projectReport);
		}
		
	}
	
	/**
	 * 在构造函数里面初始化报表配置实例
	 */
	public ReportServiceImpl() {
		configuration=new Configuration(new Version("2.3.23"));
		configuration.setClassForTemplateLoading(this.getClass(), "/");
	}
	
	@Override
	public String dailyReport(Project project, Date sDate, Date eDate,ProjectReport pr) {
		List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
		String fileUrl="";
		String dateStr =(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(eDate);
		String dateCHStr =(new SimpleDateFormat("yyyy年MM月dd日")).format(eDate);
		try {
			//获取数据
			Map<String, Object> data= getLimitAccumOffsetAndBiggestChangeRateByPeriod(project,sDate,eDate);
			data.put("pr", pr);
			data.put("project", project);
			data.put("monitorDate", dateStr.split(" ")[0]);
			data.put("monitorDateCH", dateCHStr);
			Template t=configuration.getTemplate("dailyReport.ftl","utf-8");
			File directory=new File(uploadFileSrc+"/dailyReport/"+project.getProjectUuid());
			if(!(directory.exists())){
				directory.mkdirs();
			}else{
				deleteAllFilesOfDir(directory);
				directory.mkdirs();
			}
			String fileName=dateStr.split(" ")[0];
			String path=directory+"/"+project.getProjectName()+"日报"+fileName+".doc";
			fileUrl="/smosplatUploadFiles"+path.split("smosplatUploadFiles")[1];
			File outputFile=new File(path);
			BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"utf-8"));
			//所有工程图
			getMultiImageBase64(project,imgList);
			if(imgList.size()>0){
				data.put("imgList", imgList);
			}
			t.process(data, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	@Override
	public String weeklyReport(Project project,String bDate, String endDate,ProjectReport pr) {
		Map<String, Object> data=new HashMap<String,Object>();
		List<Map<String,Object>> devMap2=new ArrayList<>();
		List<Map<String,Object>> imgList = new ArrayList<Map<String,Object>>();
		Map<String,Object> siteImageList = new HashMap<>();
		//PR:周报的统一的记录   spDataMap:该段时间内的监测点统计数据 imgList：工程图list; siteImageList:最新一张现场图
		String fileUrl="";
		try {
			//获取数据
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sdf3 = new SimpleDateFormat("MM月dd日");
			data=getLimitAccumOffsetAndBiggestChangeRateByPeriod(project, sdf.parse(pr.getP1p5()+" 00:00:00"), sdf.parse(pr.getP1p6()+" 23:59:59"));
			//将device两两组合，ftl模板将以两列用list循环
			List<Device> devList=deviceService.getDeviceByOrganUuid(project.getOrgan().getOrganUuid(), -1);
			for(int i=0;i<devList.size();i++){
				Map<String, Object> devMap=new HashMap<String,Object>();
				if(i%2==0&&i<devList.size()-1){
					devMap.put("fstDev", devList.get(i));
					devMap.put("secondDev", devList.get(i+1));
					devMap2.add(devMap);
				}else if (i%2==0&&i==devList.size()-1){
					devMap.put("fstDev", devList.get(i));
					devMap.put("secondDev", null);
					devMap2.add(devMap);
				}
			}
			data.put("device", devMap2);
			//周报模板
			Template t=configuration.getTemplate("weekly.ftl","utf-8");
			File directory=new File(uploadFileSrc+"/weeklyReport/"+project.getProjectUuid());
			if(!(directory.exists())){
				directory.mkdirs();
			}else{
				deleteAllFilesOfDir(directory);
				directory.mkdirs();
			}
			String fileName=pr.getP1p5().split(" ")[0]+"至"+pr.getP1p6().split(" ")[0];
			String path=directory+"/"+pr.getP1p2()+"周报"+fileName+".doc";
			fileUrl="/smosplatUploadFiles"+path.split("smosplatUploadFiles")[1];
			File outputFile=new File(path);
			BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"utf-8"));
			Date formBDate=sdf.parse(pr.getP1p5()+" 00:00:00");
			Date formEDate=sdf.parse(pr.getP1p6()+" 00:00:00");
			data.put("formBDate", sdf2.format(formBDate));
			data.put("formEDate", sdf3.format(formEDate));
			data.put("formEDate2", sdf2.format(formEDate));
			pr.setP1p5(pr.getP1p5().replaceAll("-", "."));
			pr.setP1p6(pr.getP1p6().replaceAll("-", "."));	
			data.put("monthDay", pr.getP1p6().substring(5));
			if(pr.getP4p18()==""||pr.getP4p18()==null){
				pr.setP4p18("------");
			}
			data.put("pr", pr);
			
			//重要提示
			String[] tips=pr.getP2p6().split("\n");
			if(tips.length>0){
				data.put("tips", tips);
			}
			//监测标准
			String[] standard=pr.getP5p25().split("\n");
			if(standard.length>0){
				data.put("standard", standard);
			}
			//分析及建议
			String[] suggest=pr.getP8p1().split("\n");
			if(suggest.length>0){
				data.put("suggest", suggest);
			}

			//所有工程图
			getMultiImageBase64(project,imgList);
			if(imgList.size()>0){
				data.put("imgList", imgList);
			}
			
			//现场图
			String sitePath=uploadFileSrc+"/"+project.getOrgan().getOrganUuid()+"/project/"+project.getProjectUuid()+"/sitePic";
			getImageBase64(sitePath,siteImageList);
			if(siteImageList.size()>0){
				data.put("siteImage", siteImageList);
			}
			t.process(data, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	private void getMultiImageBase64(Project project,List<Map<String,Object>> imgList) {
		String organUuid  = project.getOrgan().getOrganUuid();
		String imgPath=uploadFileSrc+"/"+organUuid+"/project/"+project.getProjectUuid()+"/projectPic";
		File f = new File(imgPath);
		//取出图片文件
		if (f.isDirectory()) 
		{
			File[] fList = f.listFiles();
			for (int j = 0; j < fList.length; j++) {
				File file = fList[j];
				if (file.isFile()&&isImage(file)) {
					Map<String, Object> imgMap=new HashMap<String,Object>();
					InputStream in = null;
			        byte[] data = null;
			        try {
			            in = new FileInputStream(file);
			            data = new byte[in.available()];
			            in.read(data);
			            in.close();
			            String imageCodeBase64 = Base64.encodeBase64String(data);
				        imgMap.put("img", imageCodeBase64);
				        imgMap.put("pkgName","/word/media/image0"+j);
				        imgMap.put("rId", "imgId"+j);
				        imgMap.put("target", "media/image0"+j);
				        imgMap.put("imageHeigh", ImageIO.read(file).getHeight()*427.8/ImageIO.read(file).getWidth());
				        imgList.add(imgMap);
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
				}
			}
		}
    }

	private void getImageBase64(String path,Map<String,Object> siteImageList){
		File f = new File(path);
		//取出图片文件
		if (f.isDirectory()){
			File[] fList = f.listFiles();
			File file = null;
			for (int j = 0; j < fList.length; j++) {
				//判断是否图片
				if (fList[j].isFile()&&isImage(fList[j])) {
					if(file==null){
						file=fList[j];
					}else{
						//取到时间最新的一张图 file
						String a=file.getName().substring(0,file.getName().lastIndexOf(".")).trim();
						String b=fList[j].getName().substring(0,fList[j].getName().lastIndexOf(".")).trim();
						if(Long.parseLong(a)<Long.parseLong(b)){
							file=fList[j];
						}
					}
				}
			}
			if(file!=null){
				InputStream in = null;
		        byte[] data = null;
				try {
		            in = new FileInputStream(file);
		            data = new byte[in.available()];
		            in.read(data);
		            in.close();
		            siteImageList.put("image", Base64.encodeBase64String(data)) ;
					siteImageList.put("imageWidth", ImageIO.read(file).getWidth()*100/ImageIO.read(file).getHeight());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}
		}
	}
	
	private boolean isImage(File imageFile) {
		if (!imageFile.exists()) {
			return false;
		}
		Image img = null;
		try {
			img = ImageIO.read(imageFile);
			if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			img = null;
		}
	}
	
	public void deleteAllFilesOfDir(File file){
		if(!file.exists())
			return;
		if(file.isFile()){
			file.delete();
			return;
		}
		File[] files = file.listFiles();
		for(File f : files){
			deleteAllFilesOfDir(f);
		}
		file.delete();
	}

	
	private boolean hasMonitorItemByNumber(List<ProjectMonitorItem> projectMonitorItems,byte number){
		for (ProjectMonitorItem projectMonitorItem : projectMonitorItems) {
			if(projectMonitorItem.getMonitorItem().getNumber()==number){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private String getMonitorItemUuid(List<ProjectMonitorItem> projectMonitorItems,byte number){
		for (ProjectMonitorItem projectMonitorItem : projectMonitorItems) {
			if(projectMonitorItem.getMonitorItem().getNumber()==number){
				return projectMonitorItem.getMonitorItem().getMonitorItemUuid();
			}
		}
		return "";
	}
	@Override
	public void getDailyReportDate(Map<String, Object> data,Project project, String dateStr) throws ParseException{
		//导出的日报的日期
		//2017-09-30 00:00:00     ~     2017-10-01 23:59:59
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date eDate = sdf.parse(dateStr);
		Calendar date = Calendar.getInstance();
		date.setTime(eDate);
		date.add(date.DATE, - 1);
		date.add(Calendar.SECOND, 1);
		//起始日期就是选中日期的前一天
		Date sDate = sdf.parse(sdf.format(date.getTime()));
		data.put("projectName", project.getProjectName());
		data.put("address", project.getAddress());
		data.put("buildCompany", project.getBuildCompany());
		data.put("monitorDate", new SimpleDateFormat("yyyy-MM-dd").format(eDate));
		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		//围护墙(边坡)顶部水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1)){
			List<WYS_CoordData> wysData=new ArrayList<WYS_CoordData>();
			//测点
			List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(1).getMonitorItemUuid());
			List<String> spUuid = new ArrayList<>();
			sps.forEach(p->spUuid.add(p.getSurveyPointUuid()));
			//第一条数据
			List<WYS_CoordData> firstCoordData = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spUuid);
			for(int i = 0; i < sps.size(); i++){
				List<String> ps = new ArrayList<>();
				ps.add(sps.get(i).getSurveyPointUuid());
				List<WYS_CoordData> firstThreeData = wysCoordDataDao.getFirstThreeDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
				double aveX =0.0;
				double aveY =0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveX += firstThreeData.get(kk).getCaculateN();
						aveY += firstThreeData.get(kk).getCaculateE();
					}
					//把头三条数据的第一条的东北高赋成头三条东北高的平均值。
					firstThreeData.get(0).setCaculateE(aveY / firstThreeData.size());
					firstThreeData.get(0).setCaculateN(aveX / firstThreeData.size());
				}else{
					continue;
				}
				
				//选中日期和前一天两天内最新两条数据
				List<WYS_CoordData> coordData = wysCoordDataDao.getTwoWYSCoordDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				
				if(coordData.size() == 0){
					continue;
				}
				Section section = coordData.get(0).getSurveyPoint().getSection();
				//根据断面设置的点名得到以第一次测量为基准的断面的起始点和终止点的坐标数据
				//端面起始点
				List<WYS_CoordData> start = null;
				start = firstCoordData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getStartPointName())).collect(Collectors.toList());
				List<WYS_CoordData> end = null;
				//断面终止点
				end = firstCoordData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getEndPointName())).collect(Collectors.toList());
				if(start.size() == 0 || end.size() == 0){
					//如果断面选择的起始点和终止点没有测到数据
					continue;
				}
				double sectionAzimuth = GtMath.calculateSectionAngle(start.get(0), end.get(0));
				if(coordData.size() == 2){
					//东单次变化量，化为毫米
					gapOffset = GtMath.y_Displacement(coordData.get(0).getCaculateN(),
							coordData.get(0).getCaculateE(), sectionAzimuth,
							coordData.get(1).getCaculateN(), coordData.get(1).getCaculateE())*1000;
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(coordData.get(0).getSurveyTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(coordData.get(1).getSurveyTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapOffset); 
					gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				}
				if(firstCoordData.size() > 0){
					// 东累计位移，化为毫米
					accumOffset = GtMath.y_Displacement(coordData.get(0).getCaculateN(),
							coordData.get(0).getCaculateE(), sectionAzimuth,
							firstThreeData.get(0).getCaculateN(), firstThreeData.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					coordData.get(0).setAccumEOffset(accumOffset);
					coordData.get(0).setGapEOffset(gapOffset);
					coordData.get(0).setChangeRate(gapChangeRate);
					wysData.add(coordData.get(0));
				}
			} 
			data.put("WYS", wysData);
		}
		//围护墙(边坡)顶部竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5)){
			List<WYD_LevelData> wydData = new ArrayList<>();
			List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(5).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				//第一条数据
				List<WYD_LevelData> firstData = wydDataDao.getFirstWYDLevelDataBySurveyPoint(spUuid);
				//头3次数据，求平均作为初始值
				List<WYD_LevelData> firstThreeData = wydDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				double aveH = 0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
					}
				}
				//选中日期和前一天两天内最新两条数据
				List<WYD_LevelData> latestTwoData = wydDataDao.getTwoWYDDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				double gapHOffset = 0.0;
				double accumHOffset = 0.0;
				double gapChangeRate = 0.0;
				if(latestTwoData.size() == 2){
					gapHOffset = (latestTwoData.get(1).getLevelH() - latestTwoData.get(0).getLevelH()) * 1000;
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapHOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapHOffset); 
					gapHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				}
				if(firstData.size() > 0 && latestTwoData.size() > 0){
					accumHOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH() + sps.get(i).getOriginalTotalValue()) * 1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumHOffset); 
					accumHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					latestTwoData.get(0).setAccumHOffset(accumHOffset);
					latestTwoData.get(0).setGapHOffset(gapHOffset);
					latestTwoData.get(0).setGapHChangeRate(gapChangeRate);
					latestTwoData.get(0).setOriginalAveH(aveH / firstThreeData.size());
					wydData.add(latestTwoData.get(0));
				}
			} 
			data.put("WYD", wydData);		
		}
		//周边管线竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10)){
			List<ZGD_Data> zgdData = new ArrayList<>();
			List<SurveyPoint_ZGD> sps = sp_ZGDService.getSP_ZGDs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(10).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				//第一条数据
				List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuid);
				//选中日期和前一天两天内两条数据。开始时间那天的最后一条，和前一天的最后一条。
				List<ZGD_Data> latestTwoData = zgdDataDao.getTwoZGDDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				//头3次数据，求平均作为初始值
				List<ZGD_Data> firstThreeData = zgdDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				double aveH = 0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
					}
				}
				double gapHOffset = 0.0;
				double accumHOffset = 0.0;
				double gapChangeRate = 0.0;
				if(latestTwoData != null && latestTwoData.size() == 2){
					gapHOffset = (latestTwoData.get(1).getLevelH() - latestTwoData.get(0).getLevelH()) * 1000;
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapHOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapHOffset); 
					gapHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				}
				if(firstData != null && firstData.size() > 0 && latestTwoData.size() > 0){
					accumHOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH()) * 1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumHOffset); 
					accumHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					latestTwoData.get(0).setAccumHOffset(accumHOffset);
					latestTwoData.get(0).setGapHOffset(gapHOffset);
					latestTwoData.get(0).setGapHChangeRate(gapChangeRate);
					latestTwoData.get(0).setOriginalAveH(aveH / firstThreeData.size());
					zgdData.add(latestTwoData.get(0));
				}
			} 
			data.put("ZGD", zgdData);
			
		}
		//立柱竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6)){
			List<LZ_Data> lzData = new ArrayList<>();
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(6).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				//第一条数据
				List<LZ_Data> firstData = lzDataDao.getFirstLZ_DataBySurveyPoint(spUuid);
				//选中日期和前一天两天内最新两条数据
				List<LZ_Data> latestTwoData = lzDataDao.getTwoLZDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				//头3次数据，求平均作为初始值
				List<LZ_Data> firstThreeData = lzDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				double aveH = 0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
					}
				}
				double gapHOffset = 0.0;
				double accumHOffset = 0.0;
				double gapChangeRate = 0.0;
				if(latestTwoData != null && latestTwoData.size() == 2){
					gapHOffset = (latestTwoData.get(1).getLevelH() - latestTwoData.get(0).getLevelH()) * 1000;
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapHOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapHOffset); 
					gapHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				}
				if(firstData != null && firstData.size() > 0 && latestTwoData.size() > 0){
					accumHOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH()) * 1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumHOffset); 
					accumHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					latestTwoData.get(0).setAccumHOffset(accumHOffset);
					latestTwoData.get(0).setGapHOffset(gapHOffset);
					latestTwoData.get(0).setGapHOffsetChangeRate(gapChangeRate);
					latestTwoData.get(0).setOriginalAveH(aveH / firstThreeData.size());
					lzData.add(latestTwoData.get(0));
				}
			} 
			data.put("LZ", lzData);	
		}
		//周边建筑物竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8)){
			List<SM_Data> smData = new ArrayList<>();
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(8).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				//第一条数据
				List<SM_Data> firstData = smDataDao.getFirstSM_DataBySurveyPoint(spUuid);
				//选中日期和前一天两天内最新两条数据
				List<SM_Data> latestTwoData = smDataDao.getTwoSMDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				//头3次数据，求平均作为初始值
				List<SM_Data> firstThreeData = smDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				double aveH = 0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
					}
				}
				double gapHOffset = 0.0;
				double accumHOffset = 0.0;
				double gapChangeRate = 0.0;
				if(latestTwoData != null && latestTwoData.size() == 2){
					gapHOffset = (latestTwoData.get(1).getLevelH() - latestTwoData.get(0).getLevelH()) * 1000;
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapHOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapHOffset); 
					gapHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				}
				if(firstData != null && firstData.size() > 0 && latestTwoData.size() > 0){
					accumHOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH()) * 1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumHOffset); 
					accumHOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					latestTwoData.get(0).setAccumHOffset(accumHOffset);
					latestTwoData.get(0).setGapHOffset(gapHOffset);
					latestTwoData.get(0).setGapHOffsetChangeRate(gapChangeRate);
					latestTwoData.get(0).setOriginalAveH(aveH / firstThreeData.size());
					smData.add(latestTwoData.get(0));
				}
			} 
			data.put("SM", smData);	
		}		
		//支护结构深层水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4)){
			List<DailyReport_DeepOffset> cxData=new ArrayList<>();
			//测点
			List<SurveyPoint_CX> sps = sp_CXService.getSP_CXs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(4).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				DailyReport_DeepOffset deepData = new DailyReport_CX();
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
//				//这个时间段内最新一条数据
				//测斜的单次变化量，累计变化量，变化速率计算方式比较特殊，都是计算好了存库里，所以只要取特定时间段内的一条，不需要取两条作计算。
				List<CX_Data> latestData = cxDataDao.getOneCXDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				if(latestData.size() > 0){
					deepData.setCode(sps.get(i).getCode());
					//根据变化速率和累计变化量(绝对值)排序,大的在上小的在下
					latestData.sort(new Comparator<CX_Data>() {
						@Override
						public int compare(CX_Data sp1, CX_Data sp2) {  
			                if(Math.abs(sp1.getChangeRate()) > Math.abs(sp2.getChangeRate())) {
			                	return -1;
			                }else if(sp1.getChangeRate() == sp2.getChangeRate()){
			                	return 0;
			                }else{
			                	return 1;
			                }
			            }
					});
					deepData.setMaxChangeRate(latestData.get(0).getChangeRate());
					latestData.sort(new Comparator<CX_Data>() {
						@Override
						public int compare(CX_Data sp1, CX_Data sp2) {  
			                if(Math.abs(sp1.getAccumOffset()) > Math.abs(sp2.getAccumOffset())) {
			                	return -1;
			                }else if(sp1.getAccumOffset() == sp2.getAccumOffset()){
			                	return 0;
			                }else{
			                	return 1;
			                }
			            }
					});
					deepData.setMaxAccumOffset(latestData.get(0).getAccumOffset());
					cxData.add(deepData);
				}
			} 
			data.put("CX", cxData);
		}
		//支撑内力
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 15)){
			List<ZC_Data> zcData=new ArrayList<ZC_Data>();
			//测点
			List<SurveyPoint_ZC> sps = sp_ZCService.getSP_ZCs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(15).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				//第一条数据
				List<ZC_Data> firstData = zcDataDao.getFirstOneZCDatasBySurveyPoints(spUuid);
				//选中日期和前一天两天内最新两条数据
				List<ZC_Data> latestTwoData = zcDataDao.getTwoZCDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				//头3次数据，求平均作为初始值
				List<ZC_Data> firstThreeData = zcDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				double aveVal = 0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveVal += firstThreeData.get(kk).getCalValue();
					}
				}
				double gapOffset = 0.0;
//				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				if(latestTwoData != null && latestTwoData.size() == 2){
					gapOffset = latestTwoData.get(1).getCalValue() - latestTwoData.get(0).getCalValue();
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapOffset); 
					gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				}
				if(firstData != null && firstData.size() > 0 && latestTwoData.size() > 0){
//					accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
//					BigDecimal b = new BigDecimal(accumOffset); 
//					accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
//					latestTwoData.get(0).setAccumOffset(accumOffset);
					latestTwoData.get(0).setGapOffset(gapOffset);
					latestTwoData.get(0).setGapChangeRate(gapChangeRate);
					latestTwoData.get(0).setOriCalVal(aveVal / firstThreeData.size());
					zcData.add(latestTwoData.get(0));
				}
			} 
			data.put("ZC", zcData);
		}
		//锚杆内力
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18)){
			List<MT_Data> mtData=new ArrayList<MT_Data>();
			//测点
			List<SurveyPoint_MT> sps = sp_MTService.getSP_MTs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(18).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				//第一条数据
				List<MT_Data> firstData = mtDataDao.getFirstOneMTDatasBySurveyPoints(spUuid);
				//选中日期和前一天两天内最新两条数据
				List<MT_Data> latestTwoData = mtDataDao.getTwoMTDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				//头3次数据，求平均作为初始值
				List<MT_Data> firstThreeData = mtDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				double aveVal = 0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveVal += firstThreeData.get(kk).getCalValue();
					}
				}
				double gapOffset = 0.0;
//				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				if(latestTwoData != null && latestTwoData.size() == 2){
					gapOffset = latestTwoData.get(1).getCalValue() - latestTwoData.get(0).getCalValue();
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapOffset); 
					gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					latestTwoData.get(0).setLastCalValue(latestTwoData.get(1).getCalValue());
				}
				if(firstData != null && firstData.size() > 0 && latestTwoData.size() > 0){
//					accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
//					BigDecimal b = new BigDecimal(accumOffset); 
//					accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
//					latestTwoData.get(0).setAccumOffset(accumOffset);
					latestTwoData.get(0).setGapOffset(gapOffset);
					latestTwoData.get(0).setGapChangeRate(gapChangeRate);
					latestTwoData.get(0).setOriCalVal(aveVal / firstThreeData.size());
					mtData.add(latestTwoData.get(0));
				}
			} 
			data.put("MT", mtData);
		}
		//地下水位
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12)){
			List<SW_Data> swData=new ArrayList<SW_Data>();
			//测点
			List<SurveyPoint_SW> sps = sp_SWService.getSP_SWs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(12).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				//第一条数据
				List<SW_Data> firstData = swDataDao.getFirstOneSWDatasBySurveyPoints(spUuid);
				//选中日期和前一天两天内最新两条数据
				List<SW_Data> latestTwoData = swDataDao.getTwoSWDataBySurveyPointAndDate(sps.get(i).getSurveyPointUuid(), sDate, eDate);
				//头3次数据，求平均作为初始值
				List<SW_Data> firstThreeData = swDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				double aveVal = 0.0;
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveVal += firstThreeData.get(kk).getCalValue();
					}
				}
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				if(latestTwoData != null && latestTwoData.size() == 2){
					gapOffset = latestTwoData.get(1).getCalValue() - latestTwoData.get(0).getCalValue();
					String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
					long today = GtMath.fromDateStringToLong(dd);
					String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
					long lastTime = GtMath.fromDateStringToLong(dd1);
					double days = ((today - lastTime)/(1000*60*60*24));
					BigDecimal bb = new BigDecimal(days); 
					days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
					if(days == 0.0){
						days = 1.0;
					}
					//单次变化速率
					gapChangeRate = gapOffset/days;
					BigDecimal b = new BigDecimal(gapChangeRate); 
					gapChangeRate = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					b = new BigDecimal(gapOffset); 
					gapOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				}
				if(firstData != null && firstData.size() > 0 && latestTwoData.size() > 0){
					accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					latestTwoData.get(0).setAccumOffset(accumOffset);
					latestTwoData.get(0).setGapOffset(gapOffset);
					latestTwoData.get(0).setGapChangeRate(gapChangeRate);
					latestTwoData.get(0).setOriCalVal(aveVal / firstThreeData.size());
					swData.add(latestTwoData.get(0));
				}
			} 
			data.put("SW", swData);
		}
		
	}
	
	
	@Override
	public Map<String, Object> getLimitAccumOffsetAndBiggestChangeRate(Project project) {
		Map<String, Object> dataList = new HashMap<String, Object>();

		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		//水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1)){
			Map<String, Object> wys = new HashMap<String, Object>();
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
					List<WYS_CoordData> latestTwoData = wysCoordDataDao.getLatestTwoWYSCoordDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
		//			List<WYS_CoordData> firstData = ((IWYSCoordDataDao)getDao()).getFirstWYSCoordDataBySurveyPoint(sp);
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
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
								tempFirst.get(0).getCaculateN(), tempFirst.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumEOffset(accumOffset);
						latestTwoData.get(0).setGapEOffset(gapOffset);
						latestTwoData.get(0).setChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<WYS_CoordData>() {
			
						@Override
						public int compare(WYS_CoordData o1, WYS_CoordData o2) {
							double n1 = Math.abs(o1.getAccumEOffset());
							double n2 = Math.abs(o2.getAccumEOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					//累计变化量最小值
//					minWYSAccum.put(wysRecentData.get(0).getSurveyPoint().getCode(), wysRecentData.get(0).getAccumEOffset());
					//累计变化量最大值
//					maxWYSAccum.put(wysRecentData.get(wysRecentData.size() - 1).getSurveyPoint().getCode(), wysRecentData.get(wysRecentData.size() - 1).getAccumEOffset());
					wys.put("minAccumOffset", recentData.get(0).getAccumEOffset());
					wys.put("minAccumOffsetCode",recentData.get(0).getSurveyPoint().getCode());
					wys.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumEOffset());
					wys.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					wys.put("warnSingleRate", warning.getWarnSingleRate());
					wys.put("warnAccum", warning.getWarnAccum());
					wys.put("controlAccum", warning.getControlAccum());
					//根据变化速率排序
					recentData.sort(new Comparator<WYS_CoordData>() {
			
						@Override
						public int compare(WYS_CoordData o1, WYS_CoordData o2) {
							double n1 = Math.abs(o1.getChangeRate());
							double n2 = Math.abs(o2.getChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxWYSChangeRate.put(wysRecentData.get(wysRecentData.size() - 1).getSurveyPoint().getCode(), wysRecentData.get(wysRecentData.size() - 1).getAccumEOffset());
					wys.put("maxChangeRate", recentData.get(recentData.size() - 1).getChangeRate());
					wys.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
					wys.put("minAccumOffset","");
					wys.put("maxAccumOffset","");
					wys.put("maxChangeRate","");
					wys.put("minAccumOffsetCode","");
					wys.put("maxAccumOffsetCode","");
					wys.put("maxChangeRateCode","");
				}
			}
			dataList.put("WYS", wys);
		}
		//支护结构深层水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4)){
			Map<String, Object> cx = new HashMap<String, Object>();
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
					List<CX_Data> latestData = cxDataDao.getLatestOneCXDataBySurveyPoint(sp);
					if(latestData.size() == 0){
						continue;
					}
					recentData.addAll(latestData);
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<CX_Data>() {
			
						@Override
						public int compare(CX_Data o1, CX_Data o2) {
							double n1 = Math.abs(o1.getAccumOffset());
							double n2 = Math.abs(o2.getAccumOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					cx.put("minAccumOffset",recentData.get(0).getAccumOffset());
					cx.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					cx.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumOffset());
					cx.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					cx.put("warnSingleRate", warning.getWarnSingleRate());
					cx.put("warnAccum", warning.getWarnAccum());
					cx.put("controlAccum", warning.getControlAccum());
					
					//根据变化速率排序
					recentData.sort(new Comparator<CX_Data>() {
			
						@Override
						public int compare(CX_Data o1, CX_Data o2) {
							double n1 = Math.abs(o1.getChangeRate());
							double n2 = Math.abs(o2.getChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					cx.put("maxChangeRate", recentData.get(recentData.size() - 1).getChangeRate());
					cx.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
					cx.put("minAccumOffset","");
					cx.put("maxAccumOffset","");
					cx.put("maxChangeRate","");
					cx.put("minAccumOffsetCode","");
					cx.put("maxAccumOffsetCode","");
					cx.put("maxChangeRateCode","");
				}
			}
			dataList.put("CX", cx);
		}
		//顶部竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5)){
			Map<String, Object> wyd = new HashMap<String, Object>();
			//最新的一条数据
			List<WYD_LevelData> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(5).getMonitorItemUuid());
			if(sps.size() > 0){
				List<WYD_LevelData> firstData = new ArrayList<>();
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				for(int k = 0; k < sps.size(); k++){
					List<String> spid = new ArrayList<>();
					spid.add(sps.get(k).getSurveyPointUuid());
					List<WYD_LevelData> tempList = wydDataDao.getFirstThreeDataBySurveyPoint(spid);
					if(tempList.size() > 0){
						firstData.add(tempList.get(0));
					}
				}
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<WYD_LevelData> latestTwoData = wydDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
		//			List<WYS_CoordData> firstData = ((IWYSCoordDataDao)getDao()).getFirstWYSCoordDataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<WYD_LevelData>() {
			
						@Override
						public int compare(WYD_LevelData o1, WYD_LevelData o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					wyd.put("minAccumOffset", recentData.get(0).getAccumHOffset());
					wyd.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					wyd.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumHOffset());
					wyd.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					wyd.put("warnSingleRate", warning.getWarnSingleRate());
					wyd.put("warnAccum", warning.getWarnAccum());
					wyd.put("controlAccum", warning.getControlAccum());
					//根据变化速率排序
					recentData.sort(new Comparator<WYD_LevelData>() {
			
						@Override
						public int compare(WYD_LevelData o1, WYD_LevelData o2) {
							double n1 = Math.abs(o1.getGapHChangeRate());
							double n2 = Math.abs(o2.getGapHChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxChangeRate.put(recentData.get(recentData.size() - 1).getSurveyPoint().getCode(), recentData.get(recentData.size() - 1).getAccumHOffset());
					wyd.put("maxChangeRate",  recentData.get(recentData.size() - 1).getGapHChangeRate());
					wyd.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
					wyd.put("minAccumOffset","");
					wyd.put("maxAccumOffset","");
					wyd.put("maxChangeRate","");
					wyd.put("minAccumOffsetCode","");
					wyd.put("maxAccumOffsetCode","");
					wyd.put("maxChangeRateCode","");
				}
			}
			dataList.put("WYD", wyd);
		}
		//立柱竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6)){
			Map<String, Object> lz = new HashMap<String, Object>();
			//最新的一条数据
			List<LZ_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(6).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<LZ_Data> firstData = lzDataDao.getFirstLZ_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<LZ_Data> latestTwoData = lzDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					List<LZ_Data> firstData = lzDataDao.getFirstLZ_DataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<LZ_Data>() {
			
						@Override
						public int compare(LZ_Data o1, LZ_Data o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					lz.put("minAccumOffset", recentData.get(0).getAccumHOffset());
					lz.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					lz.put("maxAccumOffset",recentData.get(recentData.size() - 1).getAccumHOffset());
					lz.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					lz.put("warnSingleRate", warning.getWarnSingleRate());
					lz.put("warnAccum", warning.getWarnAccum());
					lz.put("controlAccum", warning.getControlAccum());
					//根据变化速率排序
					recentData.sort(new Comparator<LZ_Data>() {
			
						@Override
						public int compare(LZ_Data o1, LZ_Data o2) {
							double n1 = Math.abs(o1.getGapHOffsetChangeRate());
							double n2 = Math.abs(o2.getGapHOffsetChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxChangeRate.put(recentData.get(recentData.size() - 1).getSurveyPoint().getCode(), recentData.get(recentData.size() - 1).getAccumHOffset());
					lz.put("maxChangeRate",recentData.get(recentData.size() - 1).getGapHOffsetChangeRate());
					lz.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
					lz.put("minAccumOffset","");
					lz.put("maxAccumOffset","");
					lz.put("maxChangeRate","");
					lz.put("minAccumOffsetCode","");
					lz.put("maxAccumOffsetCode","");
					lz.put("maxChangeRateCode","");
				}
			}
			dataList.put("LZ", lz);
		}
		//周边建筑物竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8)){
			Map<String, Object> sm = new HashMap<String, Object>();
			//最新的一条数据
			List<SM_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SM> sps = sp_SMService.getSP_SMs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(8).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SM_Data> latestTwoData = smDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					List<SM_Data> firstData = smDataDao.getFirstSM_DataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<SM_Data>() {
			
						@Override
						public int compare(SM_Data o1, SM_Data o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					sm.put("minAccumOffset",recentData.get(0).getAccumHOffset());
					sm.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					sm.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumHOffset());
					sm.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					sm.put("warnSingleRate", warning.getWarnSingleRate());
					sm.put("warnAccum", warning.getWarnAccum());
					sm.put("controlAccum", warning.getControlAccum());
					//根据变化速率排序
					recentData.sort(new Comparator<SM_Data>() {
			
						@Override
						public int compare(SM_Data o1, SM_Data o2) {
							double n1 = Math.abs(o1.getGapHOffsetChangeRate());
							double n2 = Math.abs(o2.getGapHOffsetChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxChangeRate.put(recentData.get(recentData.size() - 1).getSurveyPoint().getCode(), recentData.get(recentData.size() - 1).getAccumHOffset());
					sm.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapHOffsetChangeRate());
					sm.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
					sm.put("minAccumOffset","");
					sm.put("maxAccumOffset","");
					sm.put("maxChangeRate","");
					sm.put("minAccumOffsetCode","");
					sm.put("maxAccumOffsetCode","");
					sm.put("maxChangeRateCode","");
				}
			}
			dataList.put("SM", sm);
		}
		//周边管线竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10)){
			Map<String, Object> zgd = new HashMap<String, Object>();
			//最新的一条数据
			List<ZGD_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZGD> sps = sp_ZGDService.getSP_ZGDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(10).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZGD_Data> latestTwoData = zgdDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<ZGD_Data>() {
			
						@Override
						public int compare(ZGD_Data o1, ZGD_Data o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					zgd.put("minAccumOffset", recentData.get(0).getAccumHOffset());
					zgd.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					zgd.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumHOffset());
					zgd.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					zgd.put("warnSingleRate", warning.getWarnSingleRate());
					zgd.put("warnAccum", warning.getWarnAccum());
					zgd.put("controlAccum", warning.getControlAccum());
					//根据变化速率排序
					recentData.sort(new Comparator<ZGD_Data>() {
			
						@Override
						public int compare(ZGD_Data o1, ZGD_Data o2) {
							double n1 = Math.abs(o1.getGapHChangeRate());
							double n2 = Math.abs(o2.getGapHChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					zgd.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapHChangeRate());
					zgd.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
					zgd.put("minAccumOffset","");
					zgd.put("maxAccumOffset","");
					zgd.put("maxChangeRate","");
					zgd.put("minAccumOffsetCode","");
					zgd.put("maxAccumOffsetCode","");
					zgd.put("maxChangeRateCode","");
				}
			}
			dataList.put("ZGD", zgd);
		}
		//地下水位
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12)){
			Map<String, Object> sw = new HashMap<String, Object>();
			//最新的一条数据
			List<SW_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SW> sps = sp_SWService.getSP_SWs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(12).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SW_Data> latestTwoData = swDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					List<SW_Data> firstData = swDataDao.getFirstOneSWDatasBySurveyPoints(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						//单位毫米
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumOffset(accumOffset);
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<SW_Data>() {
			
						@Override
						public int compare(SW_Data o1, SW_Data o2) {
							double n1 = Math.abs(o1.getAccumOffset());
							double n2 = Math.abs(o2.getAccumOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					sw.put("minAccumOffset", recentData.get(0).getAccumOffset());
					sw.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					sw.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumOffset());
					sw.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					sw.put("warnSingleRate", warning.getWarnSingleRate());
					sw.put("warnAccum", warning.getWarnAccum());
					sw.put("controlAccum", warning.getControlAccum());
					
					//根据变化速率排序
					recentData.sort(new Comparator<SW_Data>() {
			
						@Override
						public int compare(SW_Data o1, SW_Data o2) {
							double n1 = Math.abs(o1.getGapChangeRate());
							double n2 = Math.abs(o2.getGapChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					sw.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapChangeRate());
					sw.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
					sw.put("minAccumOffset","");
					sw.put("maxAccumOffset","");
					sw.put("maxChangeRate","");
					sw.put("minAccumOffsetCode","");
					sw.put("maxAccumOffsetCode","");
					sw.put("maxChangeRateCode","");
				}
			}
			dataList.put("SW", sw);
		}
		//支撑内力
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 15)){
			Map<String, Object> zc = new HashMap<String, Object>();
			//最新的一条数据
			List<ZC_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZC> sps = sp_ZCService.getSP_ZCs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(15).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZC_Data> latestTwoData = zcDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
//					List<ZC_Data> firstData = zcDataDao.getFirstOneZCDatasBySurveyPoints(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						//单位kN
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
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
						//精确到0.01
						gapOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.01
						changeRate = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
//					if(firstData.size() > 0 ){
						if(latestTwoData.size() > 0){
	//						accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
	//						BigDecimal bb = new BigDecimal(accumOffset); 
	//						accumOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
	//						latestTwoData.get(0).setAccumOffset(accumOffset);
							latestTwoData.get(0).setGapOffset(gapOffset);
							latestTwoData.get(0).setGapChangeRate(changeRate);
							recentData.add(latestTwoData.get(0));
						}
//					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
//					recentData.sort(new Comparator<ZC_Data>() {
//			
//						@Override
//						public int compare(ZC_Data o1, ZC_Data o2) {
//							double n1 = Math.abs(o1.getAccumOffset());
//							double n2 = Math.abs(o2.getAccumOffset());
//							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
//						}
//					});
//					zc.put("minAccumOffset", recentData.get(0).getSurveyPoint().getCode()+','+recentData.get(0).getAccumOffset());
//					zc.put("maxAccumOffset", recentData.get(recentData.size() - 1).getSurveyPoint().getCode()+','+recentData.get(recentData.size() - 1).getAccumOffset());
					
					//根据变化速率排序
					recentData.sort(new Comparator<ZC_Data>() {
			
						@Override
						public int compare(ZC_Data o1, ZC_Data o2) {
							double n1 = Math.abs(o1.getGapChangeRate());
							double n2 = Math.abs(o2.getGapChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					zc.put("warnSingleRate", warning.getWarnSingleRate());
					zc.put("warnAccum", warning.getWarnAccum());
					zc.put("controlAccum", warning.getControlAccum());
					
					zc.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapChangeRate());
					zc.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					//根据本次内力值排序
					recentData.sort(new Comparator<ZC_Data>() {
			
						@Override
						public int compare(ZC_Data o1, ZC_Data o2) {
							double n1 = Math.abs(o1.getCalValue());
							double n2 = Math.abs(o2.getCalValue());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					zc.put("maxCalValCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					zc.put("maxCalVal", recentData.get(recentData.size() - 1).getCalValue());
				}else{
//					zc.put("minAccumOffset","");
//					zc.put("maxAccumOffset","");
//					zc.put("maxChangeRate","");
				}
			}
			dataList.put("ZC", zc);
		}
		//锚索
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18)){
			Map<String, Object> mt = new HashMap<String, Object>();
			//最新的一条数据
			List<MT_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_MT> sps = sp_MTService.getSP_MTs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(18).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<MT_Data> latestTwoData = mtDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
//					List<MT_Data> firstData = mtDataDao.getFirstOneMTDatasBySurveyPoints(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						//单位kN
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
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
						//精确到0.01
						gapOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.01
						changeRate = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
//					if(firstData.size() > 0){
						if(latestTwoData.size() > 0){
	//						accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
	//						BigDecimal bb = new BigDecimal(accumOffset); 
	//						accumOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
	//						latestTwoData.get(0).setAccumOffset(accumOffset);
							latestTwoData.get(0).setGapOffset(gapOffset);
							latestTwoData.get(0).setGapChangeRate(changeRate);
							recentData.add(latestTwoData.get(0));
						}
//					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
//					recentData.sort(new Comparator<MT_Data>() {
//			
//						@Override
//						public int compare(MT_Data o1, MT_Data o2) {
//							double n1 = Math.abs(o1.getAccumOffset());
//							double n2 = Math.abs(o2.getAccumOffset());
//							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
//						}
//					});
//					mt.put("minAccumOffset", recentData.get(0).getSurveyPoint().getCode()+','+recentData.get(0).getAccumOffset());
//					mt.put("maxAccumOffset", recentData.get(recentData.size() - 1).getSurveyPoint().getCode()+','+recentData.get(recentData.size() - 1).getAccumOffset());
					
					//根据变化速率排序
					recentData.sort(new Comparator<MT_Data>() {
			
						@Override
						public int compare(MT_Data o1, MT_Data o2) {
							double n1 = Math.abs(o1.getGapChangeRate());
							double n2 = Math.abs(o2.getGapChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					mt.put("warnSingleRate", warning.getWarnSingleRate());
					mt.put("warnAccum", warning.getWarnAccum());
					mt.put("controlAccum", warning.getControlAccum());
					mt.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapChangeRate());
					mt.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					//根据本次内力值排序
					recentData.sort(new Comparator<MT_Data>() {
			
						@Override
						public int compare(MT_Data o1, MT_Data o2) {
							double n1 = Math.abs(o1.getCalValue());
							double n2 = Math.abs(o2.getCalValue());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					mt.put("maxCalValCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					mt.put("maxCalVal", recentData.get(recentData.size() - 1).getCalValue());
				}else{
//					mt.put("minAccumOffset","");
//					mt.put("maxAccumOffset","");
//					mt.put("maxChangeRate","");
				}
			}
			dataList.put("MT", mt);
		}
		return dataList;
	}
	
	
	@Override
	public void timeReport(Map<String, Object> data, Project project,String bDate, String endDate ,String flag) throws Exception {
		project.setProjectMonitorItems(null);
		data.put("project", project);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date firstDay = sdf.parse(bDate);
		Date lastDay = sdf.parse(endDate);
		//周报时间段内的天数
		int differentDays = GtMath.differentDays(firstDay, lastDay) + 1;
		
		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		//围护墙(边坡)顶部水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1) && flag.equals("WYS")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(1).getMonitorItemUuid());
			//第一条数据
			List<WYS_CoordData> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<WYS_CoordData> temp = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				WYS_CoordData beyondCoordData = wysCoordDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveX = 0.0;
				double aveY = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<WYS_CoordData> coordData = wysCoordDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<WYS_CoordData> firstThreeData = wysCoordDataDao.getFirstThreeDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveX += firstThreeData.get(kk).getCaculateN();
						aveY += firstThreeData.get(kk).getCaculateE();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j< coordData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == coordData.size() - 1){
						if(beyondCoordData != null){
							Section section = coordData.get(0).getSurveyPoint().getSection();
							List<WYS_CoordData> start = null;
							start = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getStartPointName())).collect(Collectors.toList());
							List<WYS_CoordData> end = null;
							//断面终止点
							end = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getEndPointName())).collect(Collectors.toList());
							if(start.size() == 0 || end.size() == 0){
								//如果断面选择的起始点和终止点没有测到数据
								continue;
							}
							double sectionAzimuth = GtMath.calculateSectionAngle(start.get(0), end.get(0));
							gapOffset = GtMath.y_Displacement(coordData.get(j).getCaculateN(),
									coordData.get(j).getCaculateE(), sectionAzimuth,
									beyondCoordData.getCaculateN(), beyondCoordData.getCaculateE())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(coordData.get(j).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							accumOffset = GtMath.y_Displacement(coordData.get(j).getCaculateN(),
									coordData.get(j).getCaculateE(), sectionAzimuth,firstThreeData.size() > 0 ? aveX / firstThreeData.size():0, firstThreeData.size() > 0 ? aveY / firstThreeData.size():0)*1000 + sps.get(i).getOriginalTotalValue();
							b = new BigDecimal(accumOffset); 
							accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate = accumOffset = 0.0;
						}
					}else{
						Section section = coordData.get(j).getSurveyPoint().getSection();
						List<WYS_CoordData> start = null;
						start = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getStartPointName())).collect(Collectors.toList());
						List<WYS_CoordData> end = null;
						//断面终止点
						end = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getEndPointName())).collect(Collectors.toList());
						if(start.size() == 0 || end.size() == 0){
							//如果断面选择的起始点和终止点没有测到数据
							continue;
						}
						double sectionAzimuth = GtMath.calculateSectionAngle(start.get(0), end.get(0));
						//不是第一条，跟前一条作对比
						gapOffset = GtMath.y_Displacement(coordData.get(j).getCaculateN(),
								coordData.get(j).getCaculateE(), sectionAzimuth,
								coordData.get(j + 1).getCaculateN(), coordData.get(j + 1).getCaculateE())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(coordData.get(j).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(coordData.get(j + 1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						
						accumOffset = GtMath.y_Displacement(coordData.get(j).getCaculateN(),
								coordData.get(j).getCaculateE(), sectionAzimuth,firstThreeData.size() > 0 ? aveX / firstThreeData.size():0, firstThreeData.size() > 0 ? aveY / firstThreeData.size():0)*1000 + sps.get(i).getOriginalTotalValue();
						b = new BigDecimal(accumOffset); 
						accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 

					}
					coordData.get(j).setGapEOffset(gapOffset);
					coordData.get(j).setChangeRate(gapChangeRate);
					coordData.get(j).setAccumEOffset(accumOffset);
				}
				//排序，从旧到新
//				coordData.sort(new Comparator<WYS_CoordData>() {
//					@Override
//					public int compare(WYS_CoordData sp1, WYS_CoordData sp2) {
//						if (sp1.getSurveyTime().getTime() < sp2.getSurveyTime().getTime()) {
//							return -1;
//						} else if (sp1 == sp2) {
//							return 0;
//						} else {
//							return 1;
//						}
//					}
//				});
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<WYS_CoordData> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<WYS_CoordData> coords = coordData.stream().filter(p -> GtMath.sameDate(tempDate, p.getSurveyTime())).sorted(Comparator.comparing(WYS_CoordData::getSurveyTime)).collect(Collectors.toList());
			        if(coords.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(coords.get(coords.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalE",new BigDecimal(aveY / (firstThreeData.size()>0?firstThreeData.size():1)).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue());
				dMap.put("originalN",aveX / (firstThreeData.size()>0?firstThreeData.size():1));
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("WYS", dataMap);
		}
		//围护墙(边坡)顶部竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5) && flag.equals("WYD")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(5).getMonitorItemUuid());
			//第一条数据
			List<WYD_LevelData> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<WYD_LevelData> temp = wydDataDao.getFirstWYDLevelDataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				WYD_LevelData beyondCoordData = wydDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveH = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<WYD_LevelData> oriData = wydDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<WYD_LevelData> firstThreeData = wydDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j< oriData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == oriData.size() - 1){
						if(beyondCoordData != null){
							gapOffset = (oriData.get(0).getLevelH() - beyondCoordData.getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate =0.0;
						}
					}else{
						//不是第一条，跟前一条作对比
						gapOffset = (oriData.get(j).getLevelH() - oriData.get(j + 1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j + 1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						


					}
					accumOffset = (oriData.get(j).getLevelH() - (firstThreeData.size() > 0 ? aveH / firstThreeData.size():0))*1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					oriData.get(j).setGapHOffset(gapOffset);
					oriData.get(j).setGapHChangeRate(gapChangeRate);
					oriData.get(j).setAccumHOffset(accumOffset);
				}
				
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<WYD_LevelData> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<WYD_LevelData> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getSurveyTime())).sorted(Comparator.comparing(WYD_LevelData::getSurveyTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalH",new BigDecimal(aveH / (firstThreeData.size()>0?firstThreeData.size():1)).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue());
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("WYD", dataMap);
		}
		//立柱竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6) && flag.equals("LZ")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(6).getMonitorItemUuid());
			//第一条数据
			List<LZ_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<LZ_Data> temp = lzDataDao.getFirstLZ_DataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				LZ_Data beyondCoordData = lzDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveH = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<LZ_Data> oriData = lzDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<LZ_Data> firstThreeData = lzDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j< oriData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == oriData.size() - 1){
						if(beyondCoordData != null){
							gapOffset = (oriData.get(0).getLevelH() - beyondCoordData.getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate =0.0;
						}
					}else{
						//不是第一条，跟前一条作对比
						gapOffset = (oriData.get(j).getLevelH() - oriData.get(j + 1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j + 1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						

					}
					accumOffset = (oriData.get(j).getLevelH() - (firstThreeData.size() > 0 ? aveH / firstThreeData.size():0))*1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					oriData.get(j).setGapHOffset(gapOffset);
					oriData.get(j).setGapHOffsetChangeRate(gapChangeRate);
					oriData.get(j).setAccumHOffset(accumOffset);
				}
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<LZ_Data> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<LZ_Data> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getSurveyTime())).sorted(Comparator.comparing(LZ_Data::getSurveyTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalH",new BigDecimal(aveH / (firstThreeData.size()>0?firstThreeData.size():1)).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue());
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("LZ", dataMap);
		}
		//周边建筑竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8) && flag.equals("SM")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_SM> sps = sp_SMService.getSP_SMs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(8).getMonitorItemUuid());
			//第一条数据
			List<SM_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<SM_Data> temp = smDataDao.getFirstSM_DataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				SM_Data beyondCoordData = smDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveH = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<SM_Data> oriData = smDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<SM_Data> firstThreeData = smDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j< oriData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == oriData.size() - 1){
						if(beyondCoordData != null){
							gapOffset = (oriData.get(0).getLevelH() - beyondCoordData.getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate =0.0;
						}
					}else{
						//不是第一条，跟前一条作对比
						gapOffset = (oriData.get(j).getLevelH() - oriData.get(j + 1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j + 1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						

					}
					accumOffset = (oriData.get(j).getLevelH() - (firstThreeData.size() > 0 ? aveH / firstThreeData.size():0))*1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					oriData.get(j).setGapHOffset(gapOffset);
					oriData.get(j).setGapHOffsetChangeRate(gapChangeRate);
					oriData.get(j).setAccumHOffset(accumOffset);
				}
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<SM_Data> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<SM_Data> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getSurveyTime())).sorted(Comparator.comparing(SM_Data::getSurveyTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalH",new BigDecimal(aveH / (firstThreeData.size()>0?firstThreeData.size():1)).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue());
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("SM", dataMap);
		}
		//周边管线竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10) && flag.equals("ZGD")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_ZGD> sps = sp_ZGDService.getSP_ZGDs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(10).getMonitorItemUuid());
			//第一条数据
			List<ZGD_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<ZGD_Data> temp = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				ZGD_Data beyondCoordData = zgdDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveH = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<ZGD_Data> oriData = zgdDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<ZGD_Data> firstThreeData = zgdDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j< oriData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == oriData.size() - 1){
						if(beyondCoordData != null){
							gapOffset = (oriData.get(0).getLevelH() - beyondCoordData.getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate =0.0;
						}
					}else{
						//不是第一条，跟前一条作对比
						gapOffset = (oriData.get(j).getLevelH() - oriData.get(j + 1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j + 1).getSurveyTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						

					}
					accumOffset = (oriData.get(j).getLevelH() - (firstThreeData.size() > 0 ? aveH / firstThreeData.size():0))*1000 + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					oriData.get(j).setGapHOffset(gapOffset);
					oriData.get(j).setGapHChangeRate(gapChangeRate);
					oriData.get(j).setAccumHOffset(accumOffset);
				}
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<ZGD_Data> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<ZGD_Data> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getSurveyTime())).sorted(Comparator.comparing(ZGD_Data::getSurveyTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalH",new BigDecimal(aveH / (firstThreeData.size()>0?firstThreeData.size():1)).setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue());
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("ZGD", dataMap);
		}
		//水位
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12) && flag.equals("SW")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_SW> sps = sp_SWService.getSP_SWs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(12).getMonitorItemUuid());
			//第一条数据
			List<SW_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<SW_Data> temp = swDataDao.getFirstOneSWDatasBySurveyPoints(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				SW_Data beyondCoordData = swDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveCalVal = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<SW_Data> oriData = swDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<SW_Data> firstThreeData = swDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveCalVal += firstThreeData.get(kk).getCalValue();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getCollectTime()) + ','; 
						
					}
				}
				for(int j = 0; j< oriData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == oriData.size() - 1){
						if(beyondCoordData != null){
							gapOffset = oriData.get(0).getCalValue() - beyondCoordData.getCalValue();
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(0).getCollectTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getCollectTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate =0.0;
						}
					}else{
						//不是第一条，跟前一条作对比
						gapOffset = oriData.get(j).getCalValue() - oriData.get(j + 1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j + 1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						

					}
					accumOffset = oriData.get(j).getCalValue() - (firstThreeData.size() > 0 ? aveCalVal / firstThreeData.size():0) + sps.get(i).getOriginalTotalValue();
					BigDecimal b = new BigDecimal(accumOffset); 
					accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
					oriData.get(j).setGapOffset(gapOffset);
					oriData.get(j).setGapChangeRate(gapChangeRate);
					oriData.get(j).setAccumOffset(accumOffset);
				}
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<SW_Data> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<SW_Data> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getCollectTime())).sorted(Comparator.comparing(SW_Data::getCollectTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalCalVal",new DecimalFormat("#.00").format(aveCalVal / (firstThreeData.size()>0?firstThreeData.size():1)));
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("SW", dataMap);
		}
		//轴力支撑
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 15) && flag.equals("ZC")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_ZC> sps = sp_ZCService.getSP_ZCs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(15).getMonitorItemUuid());
			//第一条数据
			List<ZC_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<ZC_Data> temp = zcDataDao.getFirstOneZCDatasBySurveyPoints(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				ZC_Data beyondCoordData = zcDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
//				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveCalVal = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<ZC_Data> oriData = zcDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<ZC_Data> firstThreeData = zcDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveCalVal += firstThreeData.get(kk).getCalValue();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getCollectTime()) + ','; 
						
					}
				}
				for(int j = 0; j< oriData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == oriData.size() - 1){
						if(beyondCoordData != null){
							gapOffset = oriData.get(0).getCalValue() - beyondCoordData.getCalValue();
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(0).getCollectTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getCollectTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate =0.0;
						}
					}else{
						//不是第一条，跟前一条作对比
						gapOffset = oriData.get(j).getCalValue() - oriData.get(j + 1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j + 1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						
//						accumOffset = oriData.get(j).getCalValue() - (firstThreeData.size() > 0 ? aveCalVal / firstThreeData.size():0) + sps.get(i).getOriginalTotalValue();
//						b = new BigDecimal(accumOffset); 
//						accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 

//						oriData.get(j).setAccumOffset(accumOffset);
					}
					oriData.get(j).setGapOffset(gapOffset);
					oriData.get(j).setGapChangeRate(gapChangeRate);
				}
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<ZC_Data> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<ZC_Data> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getCollectTime())).sorted(Comparator.comparing(ZC_Data::getCollectTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalCalVal",new DecimalFormat("#.00").format(aveCalVal / (firstThreeData.size()>0?firstThreeData.size():1)));
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("ZC", dataMap);
		}
		//锚索
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18) && flag.equals("MT")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_MT> sps = sp_MTService.getSP_MTs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(18).getMonitorItemUuid());
			//第一条数据
			List<MT_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<MT_Data> temp = mtDataDao.getFirstOneMTDatasBySurveyPoints(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内前的最新一条数据
				MT_Data beyondCoordData = mtDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), firstDay);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
//				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double aveCalVal = 0.0;

				String first3OriginalDate = "";
				//选中时间段内的数据,从新到旧
				List<MT_Data> oriData = mtDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//头三条数据
				List<MT_Data> firstThreeData = mtDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveCalVal += firstThreeData.get(kk).getCalValue();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getCollectTime()) + ','; 
						
					}
				}
				for(int j = 0; j< oriData.size(); j ++){
					//第一条，跟时间段前一条作对比
					if(j == oriData.size() - 1){
						if(beyondCoordData != null){
							gapOffset = oriData.get(0).getCalValue() - beyondCoordData.getCalValue();
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(0).getCollectTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getCollectTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else{
							//没有上一条，单次变化为零
							gapOffset = gapChangeRate =0.0;
						}
					}else{
						//不是第一条，跟前一条作对比
						gapOffset = oriData.get(j).getCalValue() - oriData.get(j + 1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(oriData.get(j + 1).getCollectTime());
						long lastTime = GtMath.fromDateStringToLong(dd1);
						double days = ((today - lastTime)/(1000*60*60*24));
						BigDecimal bb = new BigDecimal(days); 
						days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
						if(days == 0.0){
							days = 1.0;
						}
						//单次变化速率
						gapChangeRate = gapOffset/days;
						BigDecimal b = new BigDecimal(gapChangeRate); 
						gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						b = new BigDecimal(gapOffset); 
						gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						
//						accumOffset = oriData.get(j).getCalValue() - (firstThreeData.size() > 0 ? aveCalVal / firstThreeData.size():0) + sps.get(i).getOriginalTotalValue();
//						b = new BigDecimal(accumOffset); 
//						accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 

//						oriData.get(j).setAccumOffset(accumOffset);
					}
					oriData.get(j).setGapOffset(gapOffset);
					oriData.get(j).setGapChangeRate(gapChangeRate);
				}
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<MT_Data> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<MT_Data> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getCollectTime())).sorted(Comparator.comparing(MT_Data::getCollectTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				dMap.put("originalCalVal",new DecimalFormat("#.00").format(aveCalVal / (firstThreeData.size()>0?firstThreeData.size():1)));
				dMap.put("originalDate",first3OriginalDate);
				dataMap.put(sps.get(i).getCode(), dMap);
			} 
			data.put("MT", dataMap);
		}
		//深层水平位移测斜
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4) && flag.equals("CX")){
			Map<String,Object> dataMap = new HashMap<>();
			//测点
			List<SurveyPoint_CX> sps = sp_CXService.getSP_CXs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(4).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				Map<String,Object> dMap = new HashMap<>();
				//选中时间段内的数据,从新到旧
				List<CX_Data> oriData = cxDataDao.getDataBySurveyPointInOnePeroid(sps.get(i).getSurveyPointUuid(), firstDay, lastDay);
				//记录周报搜索时间段当中有那些日期
				List<String> days = new ArrayList<>();
				List<CX_Data> dataByDay = new ArrayList<>();
				DateFormat df1 = DateFormat.getDateInstance();//日期格式，精确到日
				for(int k = 0; k < differentDays; k++){
			        Calendar cal = Calendar.getInstance();
			        cal.setTime(firstDay);
			        cal.add(Calendar.DAY_OF_YEAR, k);
			        Date tempDate = cal.getTime();
			        //找出周报时间段内每一天的数据，并按从旧到新排序
			        List<CX_Data> datas = oriData.stream().filter(p -> GtMath.sameDate(tempDate, p.getCollectTime())).sorted(Comparator.comparing(CX_Data::getCollectTime)).collect(Collectors.toList());
			        if(datas.size() > 0){
			        	//这一天有数据
			        	dataByDay.add(datas.get(datas.size() - 1));
			        }
				}
				
				dMap.put("data",dataByDay);
				
				dataMap.put(sps.get(i).getCode(), oriData);
			} 
			data.put("CX", dataMap);
		}
	}

	@Override
	public void weeklyReport(Map<String, Object> data, Project project, String bDate, String endDate) throws ParseException {
		//导出的日报的日期
		//一周的日期
		List<String> dateOfData = new ArrayList<>();
		//2017-09-30 00:00:00     ~     2017-10-01 23:59:59
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sunday = sdf.parse(endDate);
		Date monday = sdf.parse(bDate);
		List<Date> week = new ArrayList<>();
		week.add(monday);
		dateOfData.add(new SimpleDateFormat("yyyy-MM-dd").format(monday)); 
		Calendar date = Calendar.getInstance();
		date.setTime(monday);
		for(int i = 0; i < 5; i++){
			//加一天
			date.add(date.DATE, 1);
			week.add(sdf.parse(sdf.format(date.getTime())));
			dateOfData.add(new SimpleDateFormat("yyyy-MM-dd").format(sdf.parse(sdf.format(date.getTime())))); 
		}
		week.add(sunday);
		dateOfData.add(new SimpleDateFormat("yyyy-MM-dd").format(sunday)); 
		data.put("project", project);
		data.put("dateOfData", dateOfData);
		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		//围护墙(边坡)顶部水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1)){
			List<WeeklyReport_HorizontalOffset> dataList = new ArrayList<>();
			//测点
			List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(1).getMonitorItemUuid());
			//第一条数据
			List<WYS_CoordData> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<WYS_CoordData> temp = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				//周一前的最新一条数据
				WYS_CoordData beyondCoordData = wysCoordDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), monday);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double sumChange = 0.0;
				double aveX = 0.0;
				double aveY = 0.0;
				String first3OriginalDate = "";
				WeeklyReport_HorizontalOffset weekData = new WeeklyReport_WYS();
				//选中日期一周内的数据,从新到旧
				List<WYS_CoordData> coordData = wysCoordDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), monday, sunday);
				//头三条数据
				List<WYS_CoordData> firstThreeData = wysCoordDataDao.getFirstThreeDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
				Calendar cal = Calendar.getInstance();
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveX += firstThreeData.get(kk).getCaculateN();
						aveY += firstThreeData.get(kk).getCaculateE();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<WYS_CoordData> dataInOneDay = new ArrayList<>();
					coordData.stream().forEach((p) -> {
						cal.setTime(p.getSurveyTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if(dataInOneDay.size() > 0){
						Section section = dataInOneDay.get(0).getSurveyPoint().getSection();
						List<WYS_CoordData> start = null;
						start = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getStartPointName())).collect(Collectors.toList());
						List<WYS_CoordData> end = null;
						//断面终止点
						end = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(section.getEndPointName())).collect(Collectors.toList());
						if(start.size() == 0 || end.size() == 0){
							//如果断面选择的起始点和终止点没有测到数据
							continue;
						}
						double sectionAzimuth = GtMath.calculateSectionAngle(start.get(0), end.get(0));
						//当天只有1条，跟前一天作对比
						List<WYS_CoordData> dataInLastDay = new ArrayList<>();
						List<WYS_CoordData> firstData = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(dataInOneDay.get(0).getSurveyPoint().getCode())).collect(Collectors.toList());
						if(dataInOneDay.size() > 1){
							//当天不止1条，当天的作对比
							gapOffset = GtMath.y_Displacement(dataInOneDay.get(1).getCaculateN(),
									dataInOneDay.get(1).getCaculateE(), sectionAzimuth,
									dataInOneDay.get(0).getCaculateN(), dataInOneDay.get(0).getCaculateE())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(1).getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else if(dataInOneDay.size() == 1 && j != 0){
							//不是星期一
							
							int k = 1;
							while(dataInLastDay.size() == 0){
								int day = day1 - k;
								if(day < 0){
									break;
								}
								coordData.stream().forEach((p) -> {
									cal.setTime(p.getSurveyTime());
									int day2 = cal.get(Calendar.DATE);
									if(day == day2){
										dataInLastDay.add(p);
									}
								});
								k++;
							}
							if(dataInLastDay.size() > 0){
								gapOffset = GtMath.y_Displacement(dataInLastDay.get(0).getCaculateN(),
										dataInLastDay.get(0).getCaculateE(), sectionAzimuth,
										dataInOneDay.get(0).getCaculateN(), dataInOneDay.get(0).getCaculateE())*1000;
								String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
								long today = GtMath.fromDateStringToLong(dd);
								String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInLastDay.get(0).getSurveyTime());
								long lastTime = GtMath.fromDateStringToLong(dd1);
								double days = ((today - lastTime)/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1.0;
								}
								//单次变化速率
								gapChangeRate = gapOffset/days;
								BigDecimal b = new BigDecimal(gapChangeRate); 
								gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								b = new BigDecimal(gapOffset); 
								gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							}
						}else if((dataInOneDay.size() == 1 || dataInLastDay.size() == 0) && beyondCoordData != null){
							//星期一
							//或者前一条没有
							gapOffset = GtMath.y_Displacement(beyondCoordData.getCaculateN(),
									beyondCoordData.getCaculateE(), sectionAzimuth,
									dataInOneDay.get(0).getCaculateN(), dataInOneDay.get(0).getCaculateE())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondCoordData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}
						if(firstData.size() > 0){
							accumOffset = GtMath.y_Displacement(firstThreeData.size() > 0 ? aveX / firstThreeData.size():0,
									firstThreeData.size() > 0 ? aveY / firstThreeData.size():0, sectionAzimuth,
									dataInOneDay.get(0).getCaculateN(), dataInOneDay.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
//							accumOffset = GtMath.y_Displacement(firstData.get(0).getCaculateN(),
//									firstData.get(0).getCaculateE(), sectionAzimuth,
//									dataInOneDay.get(0).getCaculateN(), dataInOneDay.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
							BigDecimal b = new BigDecimal(accumOffset); 
							accumOffset = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							dataInOneDay.get(0).setGapEOffset(gapOffset);
							dataInOneDay.get(0).setChangeRate(gapChangeRate);
							dataInOneDay.get(0).setAccumEOffset(accumOffset);
							sumChange += gapOffset;
							weekData.setTotalChange(accumOffset);
							switch(j){
							case 0:
								weekData.setMonVal(gapOffset);
								break;
							case 1:
								weekData.setTueVal(gapOffset);
								break;
							case 2:
								weekData.setWedVal(gapOffset);
								break;
							case 3:
								weekData.setThurVal(gapOffset);
								break;
							case 4:
								weekData.setFriVal(gapOffset);
								break;
							case 5:
								weekData.setSatVal(gapOffset);
								break;
							case 6:
								weekData.setSunVal(gapOffset);
								break;
							}
						}
					} 
				}
				weekData.setCode(sps.get(i).getCode());
				weekData.setSumChange(sumChange);
				double sumChangeRate = 0.0;
				sumChangeRate = sumChange / 7;
				BigDecimal b = new BigDecimal(sumChangeRate); 
				sumChangeRate = b.setScale(1,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
				weekData.setSumChangeRate(sumChangeRate);
				weekData.setAverageOriginalDate(first3OriginalDate);
				weekData.setAverageOriginalX(firstThreeData.size() > 0 ? aveX / firstThreeData.size():0);
				weekData.setAverageOriginalY(firstThreeData.size() > 0 ? aveY / firstThreeData.size():0); 
				dataList.add(weekData);
			} 
			data.put("WYS", dataList);
		}//围护墙(边坡)顶部竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5)){
			//存放星期几，数据
			List<WeeklyReport_VerticalOffset> dataList = new ArrayList<>();
			//测点
			List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(5).getMonitorItemUuid());
			//第一条数据
			List<WYD_LevelData> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<WYD_LevelData> temp = wydDataDao.getFirstWYDLevelDataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				//周一前的最新一条数据
				WYD_LevelData beyondData = wydDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), monday);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double sumChange = 0.0;
				double aveH = 0.0;
				String first3OriginalDate = "";
				WeeklyReport_VerticalOffset weekData = new WeeklyReport_WYD();
				//当天只有1条，跟前一天作对比
				List<WYD_LevelData> dataInLastDay = new ArrayList<>();
				//选中日期一周内的所有数据,从新到旧
				List<WYD_LevelData> allDataInOneWeek = wydDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), monday, sunday);
				//头3条数据
				List<WYD_LevelData> firstThreeData = wydDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				Calendar cal = Calendar.getInstance();
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<WYD_LevelData> dataInOneDay = new ArrayList<>();
					allDataInOneWeek.stream().forEach((p) -> {
						cal.setTime(p.getSurveyTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if(dataInOneDay.size() > 0){
						List<WYD_LevelData> firstData = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(dataInOneDay.get(0).getSurveyPoint().getCode())).collect(Collectors.toList());
						if(dataInOneDay.size() > 1){
							//当天不止1条，当天的作对比
							gapOffset = (dataInOneDay.get(0).getLevelH() - dataInOneDay.get(1).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(1).getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else if(dataInOneDay.size() == 1 && j != 0){
							//不是星期一
							
							int k = 1;
							while(dataInLastDay.size() == 0){
								int day = day1 - k;
								if(day < 0){
									break;
								}
								allDataInOneWeek.stream().forEach((p) -> {
									cal.setTime(p.getSurveyTime());
									int day2 = cal.get(Calendar.DATE);
									if(day == day2){
										dataInLastDay.add(p);
									}
								});
								k++;
							}
							if(dataInLastDay.size() > 0){
								gapOffset = (dataInLastDay.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
								String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
								long today = GtMath.fromDateStringToLong(dd);
								String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInLastDay.get(0).getSurveyTime());
								long lastTime = GtMath.fromDateStringToLong(dd1);
								double days = ((today - lastTime)/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1.0;
								}
								//单次变化速率
								gapChangeRate = gapOffset/days;
								BigDecimal b = new BigDecimal(gapChangeRate); 
								gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								b = new BigDecimal(gapOffset); 
								gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							}
						}else if((dataInOneDay.size() == 1 || dataInLastDay.size() == 0) && beyondData != null){
							//星期一
							gapOffset = (beyondData.getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}
						if(firstData.size() > 0){
							accumOffset = (firstData.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
							BigDecimal b = new BigDecimal(accumOffset); 
							accumOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							sumChange += gapOffset;
							weekData.setTotalChange(accumOffset);
							switch(j){
							case 0:
								weekData.setMonVal(gapOffset);
								break;
							case 1:
								weekData.setTueVal(gapOffset);
								break;
							case 2:
								weekData.setWedVal(gapOffset);
								break;
							case 3:
								weekData.setThurVal(gapOffset);
								break;
							case 4:
								weekData.setFriVal(gapOffset);
								break;
							case 5:
								weekData.setSatVal(gapOffset);
								break;
							case 6:
								weekData.setSunVal(gapOffset);
								break;
							}
						}
					} 
				}
				weekData.setCode(sps.get(i).getCode());
				weekData.setSumChange(sumChange);
				weekData.setSumChangeRate(sumChange / 7);
				weekData.setOriginalAveDate(first3OriginalDate);
				weekData.setOriginalAveH(firstThreeData.size() > 0 ? aveH / firstThreeData.size():0);
				dataList.add(weekData);
			} 
			data.put("WYD", dataList);
		}
		//立柱竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6)){
			//存放星期几，数据
			List<WeeklyReport_VerticalOffset> dataList = new ArrayList<>();
			//测点
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(6).getMonitorItemUuid());
			//第一条数据
			List<LZ_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<LZ_Data> temp = lzDataDao.getFirstLZ_DataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				//周一前的最新一条数据
				LZ_Data beyondData = lzDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), monday);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double sumChange = 0.0;
				double aveH = 0.0;
				String first3OriginalDate = "";
				WeeklyReport_VerticalOffset weekData = new WeeklyReport_LZ();
				//当天只有1条，跟前一天作对比
				List<LZ_Data> dataInLastDay = new ArrayList<>();
				//选中日期一周内的所有数据,从新到旧
				List<LZ_Data> allDataInOneWeek = lzDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), monday, sunday);
				//头3条数据
				List<LZ_Data> firstThreeData = lzDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				Calendar cal = Calendar.getInstance();
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<LZ_Data> dataInOneDay = new ArrayList<>();
					allDataInOneWeek.stream().forEach((p) -> {
						cal.setTime(p.getSurveyTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if(dataInOneDay.size() > 0){
						List<LZ_Data> firstData = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(dataInOneDay.get(0).getSurveyPoint().getCode())).collect(Collectors.toList());
						if(dataInOneDay.size() > 1){
							//当天不止1条，当天的作对比
							gapOffset = (dataInOneDay.get(0).getLevelH() - dataInOneDay.get(1).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(1).getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else if(dataInOneDay.size() == 1 && j != 0){
							//不是星期一
							
							int k = 1;
							while(dataInLastDay.size() == 0){
								int day = day1 - k;
								if(day < 0){
									break;
								}
								allDataInOneWeek.stream().forEach((p) -> {
									cal.setTime(p.getSurveyTime());
									int day2 = cal.get(Calendar.DATE);
									if(day == day2){
										dataInLastDay.add(p);
									}
								});
								k++;
							}
							if(dataInLastDay.size() > 0){
								gapOffset = (dataInLastDay.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
								String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
								long today = GtMath.fromDateStringToLong(dd);
								String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInLastDay.get(0).getSurveyTime());
								long lastTime = GtMath.fromDateStringToLong(dd1);
								double days = ((today - lastTime)/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1.0;
								}
								//单次变化速率
								gapChangeRate = gapOffset/days;
								BigDecimal b = new BigDecimal(gapChangeRate); 
								gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								b = new BigDecimal(gapOffset); 
								gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							}
						}else if((dataInOneDay.size() == 1 || dataInLastDay.size() == 0) && beyondData != null){
							//星期一
							gapOffset = (beyondData.getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}
						if(firstData.size() > 0){
							accumOffset = (firstData.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
							BigDecimal b = new BigDecimal(accumOffset); 
							accumOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							sumChange += gapOffset;
							weekData.setTotalChange(accumOffset);
							switch(j){
							case 0:
								weekData.setMonVal(gapOffset);
								break;
							case 1:
								weekData.setTueVal(gapOffset);
								break;
							case 2:
								weekData.setWedVal(gapOffset);
								break;
							case 3:
								weekData.setThurVal(gapOffset);
								break;
							case 4:
								weekData.setFriVal(gapOffset);
								break;
							case 5:
								weekData.setSatVal(gapOffset);
								break;
							case 6:
								weekData.setSunVal(gapOffset);
								break;
							}
						}
					} 
				}
				weekData.setCode(sps.get(i).getCode());
				weekData.setSumChange(sumChange);
				weekData.setSumChangeRate(sumChange / 7);
				weekData.setOriginalAveDate(first3OriginalDate);
				weekData.setOriginalAveH(firstThreeData.size() > 0 ? aveH / firstThreeData.size():0);
				dataList.add(weekData);
			} 
			data.put("LZ", dataList);
		}
		//周边建筑物竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8)){
			//存放星期几，数据
			List<WeeklyReport_VerticalOffset> dataList = new ArrayList<>();
			//测点
			List<SurveyPoint_SM> sps = sp_SMService.getSP_SMs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(8).getMonitorItemUuid());
			//第一条数据
			List<SM_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<SM_Data> temp = smDataDao.getFirstSM_DataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				//周一前的最新一条数据
				SM_Data beyondData = smDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), monday);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double sumChange = 0.0;
				double aveH = 0.0;
				//头三次数据的日期
				String first3OriginalDate = "";
				WeeklyReport_VerticalOffset weekData = new WeeklyReport_SM();
				//当天只有1条，跟前一天作对比
				List<SM_Data> dataInLastDay = new ArrayList<>();
				//选中日期一周内的所有数据,从新到旧
				List<SM_Data> allDataInOneWeek = smDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), monday, sunday);
				//头3条数据
				List<SM_Data> firstThreeData = smDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				Calendar cal = Calendar.getInstance();
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<SM_Data> dataInOneDay = new ArrayList<>();
					allDataInOneWeek.stream().forEach((p) -> {
						cal.setTime(p.getSurveyTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if(dataInOneDay.size() > 0){
						List<SM_Data> firstData = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(dataInOneDay.get(0).getSurveyPoint().getCode())).collect(Collectors.toList());
						if(dataInOneDay.size() > 1){
							//当天不止1条，当天的作对比
							gapOffset = (dataInOneDay.get(0).getLevelH() - dataInOneDay.get(1).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(1).getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else if(dataInOneDay.size() == 1 && j != 0){
							//不是星期一
							
							int k = 1;
							while(dataInLastDay.size() == 0){
								int day = day1 - k;
								if(day < 0){
									break;
								}
								allDataInOneWeek.stream().forEach((p) -> {
									cal.setTime(p.getSurveyTime());
									int day2 = cal.get(Calendar.DATE);
									if(day == day2){
										dataInLastDay.add(p);
									}
								});
								k++;
							}
							if(dataInLastDay.size() > 0){
								gapOffset = (dataInLastDay.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
								String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
								long today = GtMath.fromDateStringToLong(dd);
								String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInLastDay.get(0).getSurveyTime());
								long lastTime = GtMath.fromDateStringToLong(dd1);
								double days = ((today - lastTime)/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1.0;
								}
								//单次变化速率
								gapChangeRate = gapOffset/days;
								BigDecimal b = new BigDecimal(gapChangeRate); 
								gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								b = new BigDecimal(gapOffset); 
								gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							}
						}else if((dataInOneDay.size() == 1 || dataInLastDay.size() == 0) && beyondData != null){
							//星期一
							gapOffset = (beyondData.getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}
						if(firstData.size() > 0){
							accumOffset = (firstData.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
							BigDecimal b = new BigDecimal(accumOffset); 
							accumOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							sumChange += gapOffset;
							weekData.setTotalChange(accumOffset);
							switch(j){
							case 0:
								weekData.setMonVal(gapOffset);
								break;
							case 1:
								weekData.setTueVal(gapOffset);
								break;
							case 2:
								weekData.setWedVal(gapOffset);
								break;
							case 3:
								weekData.setThurVal(gapOffset);
								break;
							case 4:
								weekData.setFriVal(gapOffset);
								break;
							case 5:
								weekData.setSatVal(gapOffset);
								break;
							case 6:
								weekData.setSunVal(gapOffset);
								break;
							}
						}
					} 
				}
				weekData.setCode(sps.get(i).getCode());
				weekData.setSumChange(sumChange);
				weekData.setSumChangeRate(sumChange / 7);
				weekData.setOriginalAveDate(first3OriginalDate);
				weekData.setOriginalAveH(firstThreeData.size() > 0 ? aveH / firstThreeData.size():0);
				dataList.add(weekData);
			} 
			data.put("SM", dataList);
		}
		//周边管线竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10)){
			//存放星期几，数据
			List<WeeklyReport_VerticalOffset> dataList = new ArrayList<>();
			//测点
			List<SurveyPoint_ZGD> sps = sp_ZGDService.getSP_ZGDs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(10).getMonitorItemUuid());
			//第一条数据
			List<ZGD_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<ZGD_Data> temp = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				//周一前的最新一条数据
				ZGD_Data beyondData = zgdDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), monday);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double sumChange = 0.0;
				double aveH = 0.0;
				//头三次数据的日期
				String first3OriginalDate = "";
				WeeklyReport_VerticalOffset weekData = new WeeklyReport_ZGD();
				//当天只有1条，跟前一天作对比
				List<ZGD_Data> dataInLastDay = new ArrayList<>();
				//选中日期一周内的所有数据,从新到旧
				List<ZGD_Data> allDataInOneWeek = zgdDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), monday, sunday);
				//头3条数据
				List<ZGD_Data> firstThreeData = zgdDataDao.getFirstThreeDataBySurveyPoint(spUuid);
				Calendar cal = Calendar.getInstance();
				if(firstThreeData.size() > 0){
					for(int kk = 0; kk < firstThreeData.size(); kk ++){
						aveH += firstThreeData.get(kk).getLevelH();
						first3OriginalDate += new SimpleDateFormat("yyyy-MM-dd").format(firstThreeData.get(kk).getSurveyTime()) + ','; 
						
					}
				}
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<ZGD_Data> dataInOneDay = new ArrayList<>();
					allDataInOneWeek.stream().forEach((p) -> {
						cal.setTime(p.getSurveyTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if(dataInOneDay.size() > 0){
						List<ZGD_Data> firstData = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(dataInOneDay.get(0).getSurveyPoint().getCode())).collect(Collectors.toList());
						if(dataInOneDay.size() > 1){
							//当天不止1条，当天的作对比
							gapOffset = (dataInOneDay.get(0).getLevelH() - dataInOneDay.get(1).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(1).getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else if(dataInOneDay.size() == 1 && j != 0){
							//不是星期一
							
							int k = 1;
							while(dataInLastDay.size() == 0){
								int day = day1 - k;
								if(day < 0){
									break;
								}
								allDataInOneWeek.stream().forEach((p) -> {
									cal.setTime(p.getSurveyTime());
									int day2 = cal.get(Calendar.DATE);
									if(day == day2){
										dataInLastDay.add(p);
									}
								});
								k++;
							}
							if(dataInLastDay.size() > 0){
								gapOffset = (dataInLastDay.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
								String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
								long today = GtMath.fromDateStringToLong(dd);
								String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInLastDay.get(0).getSurveyTime());
								long lastTime = GtMath.fromDateStringToLong(dd1);
								double days = ((today - lastTime)/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1.0;
								}
								//单次变化速率
								gapChangeRate = gapOffset/days;
								BigDecimal b = new BigDecimal(gapChangeRate); 
								gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								b = new BigDecimal(gapOffset); 
								gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							}
						}else if((dataInOneDay.size() == 1 || dataInLastDay.size() == 0) && beyondData != null){
							//星期一
							gapOffset = (beyondData.getLevelH() - dataInOneDay.get(0).getLevelH())*1000;
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getSurveyTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondData.getSurveyTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}
						if(firstData.size() > 0){
							accumOffset = (firstData.get(0).getLevelH() - dataInOneDay.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
							BigDecimal b = new BigDecimal(accumOffset); 
							accumOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							sumChange += gapOffset;
							weekData.setTotalChange(accumOffset);
							switch(j){
							case 0:
								weekData.setMonVal(gapOffset);
								break;
							case 1:
								weekData.setTueVal(gapOffset);
								break;
							case 2:
								weekData.setWedVal(gapOffset);
								break;
							case 3:
								weekData.setThurVal(gapOffset);
								break;
							case 4:
								weekData.setFriVal(gapOffset);
								break;
							case 5:
								weekData.setSatVal(gapOffset);
								break;
							case 6:
								weekData.setSunVal(gapOffset);
								break;
							}
						}
					} 
				}
				weekData.setCode(sps.get(i).getCode());
				weekData.setSumChange(sumChange);
				weekData.setSumChangeRate(sumChange / 7);
				weekData.setOriginalAveDate(first3OriginalDate);
				weekData.setOriginalAveH(firstThreeData.size() > 0 ? aveH / firstThreeData.size():0);
				dataList.add(weekData);
			} 
			data.put("ZGD", dataList);
		}
		
		//水位
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12)){
			//存放星期几，数据
			List<WeeklyReport_SW> dataList = new ArrayList<>();
			//测点
			List<SurveyPoint_SW> sps = sp_SWService.getSP_SWs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(12).getMonitorItemUuid());
			//第一条数据
			List<SW_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<SW_Data> temp = swDataDao.getFirstOneSWDatasBySurveyPoints(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				//周一前的最新一条数据
				SW_Data beyondData = swDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), monday);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double sumChange = 0.0;
				WeeklyReport_SW weekData = new WeeklyReport_SW();
				//当天只有1条，跟前一天作对比
				List<SW_Data> dataInLastDay = new ArrayList<>();
				//选中日期一周内的所有数据,从新到旧
				List<SW_Data> allDataInOneWeek = swDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), monday, sunday);
				Calendar cal = Calendar.getInstance();
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<SW_Data> dataInOneDay = new ArrayList<>();
					allDataInOneWeek.stream().forEach((p) -> {
						cal.setTime(p.getCollectTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if(dataInOneDay.size() > 0){
						List<SW_Data> firstData = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(dataInOneDay.get(0).getSurveyPoint().getCode())).collect(Collectors.toList());
						if(dataInOneDay.size() > 1){
							//当天不止1条，当天的作对比
							gapOffset = dataInOneDay.get(0).getCalValue() - dataInOneDay.get(1).getCalValue();
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getCollectTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(1).getCollectTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else if(dataInOneDay.size() == 1 && j != 0){
							//不是星期一
							
							int k = 1;
							while(dataInLastDay.size() == 0){
								int day = day1 - k;
								if(day < 0){
									break;
								}
								allDataInOneWeek.stream().forEach((p) -> {
									cal.setTime(p.getCollectTime());
									int day2 = cal.get(Calendar.DATE);
									if(day == day2){
										dataInLastDay.add(p);
									}
								});
								k++;
							}
							if(dataInLastDay.size() > 0){
								gapOffset = dataInLastDay.get(0).getCalValue() - dataInOneDay.get(0).getCalValue();
								String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getCollectTime());
								long today = GtMath.fromDateStringToLong(dd);
								String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInLastDay.get(0).getCollectTime());
								long lastTime = GtMath.fromDateStringToLong(dd1);
								double days = ((today - lastTime)/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1.0;
								}
								//单次变化速率
								gapChangeRate = gapOffset/days;
								BigDecimal b = new BigDecimal(gapChangeRate); 
								gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								b = new BigDecimal(gapOffset); 
								gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							}
						}else if((dataInOneDay.size() == 1 || dataInLastDay.size() == 0) && beyondData != null){
							//星期一
							gapOffset = beyondData.getCalValue() - dataInOneDay.get(0).getCalValue();
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getCollectTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondData.getCollectTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}
						if(firstData.size() > 0){
							accumOffset = firstData.get(0).getCalValue() - dataInOneDay.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
							BigDecimal b = new BigDecimal(accumOffset); 
							accumOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							sumChange += gapOffset;
							weekData.setTotalChange(accumOffset);
							switch(j){
							case 0:
								weekData.setMonVal(gapOffset);
								break;
							case 1:
								weekData.setTueVal(gapOffset);
								break;
							case 2:
								weekData.setWedVal(gapOffset);
								break;
							case 3:
								weekData.setThurVal(gapOffset);
								break;
							case 4:
								weekData.setFriVal(gapOffset);
								break;
							case 5:
								weekData.setSatVal(gapOffset);
								break;
							case 6:
								weekData.setSunVal(gapOffset);
								break;
							}
						}
					} 
				}
				weekData.setCode(sps.get(i).getCode());
				weekData.setSumChange(sumChange);
				weekData.setSumChangeRate(sumChange / 7);
				dataList.add(weekData);
			} 
			data.put("SW", dataList);
		}
		
		//测斜
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4)){
			List<Map<String, List<WeeklyReport_DeepOffset>>> dataMap = new ArrayList<>();
			List<Double> depth = new ArrayList<>();
			//测点
			List<SurveyPoint_CX> sps = sp_CXService.getSP_CXs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(4).getMonitorItemUuid());
			for(int i = 0; i < sps.size(); i++){
				//存放星期几，数据
				List<WeeklyReport_DeepOffset> dataList = new ArrayList<>();
				Map<String, List<WeeklyReport_DeepOffset>> cx = new HashMap<String, List<WeeklyReport_DeepOffset>>();
				List<CX_Data> allData = new ArrayList<>();
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
				//首次数据，深度由浅到深
				List<CX_Data> firstData = cxDataDao.getFirstOneDataBySurveyPoint(spUuid);
				//选中日期一周内的所有数据,从新到旧
				List<CX_Data> allDataInOneWeek = cxDataDao.getDataBySurveyPointInOnePeroid(sps.get(i).getSurveyPointUuid(), monday, sunday);
				Calendar cal = Calendar.getInstance();
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<CX_Data> dataInOneDay = new ArrayList<>();
					allDataInOneWeek.stream().forEach((p) -> {
						cal.setTime(p.getCollectTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if (dataInOneDay.size() > 0) {
						// 若一天当中采了几次测斜，就取当天最后一次的
						List<Date> dateInSameDay = new ArrayList<>();
						for (int jj = 0; jj < dataInOneDay.size(); jj++) {
							if (!dateInSameDay.contains(dataInOneDay.get(jj).getCollectTime())) {
								// 从新倒旧
								dateInSameDay.add(dataInOneDay.get(jj).getCollectTime());
							}
						}
						Date d = dateInSameDay.get(0);
						// 筛选出当天最新一次的
						List<CX_Data> latestDataInOneDay = dataInOneDay.stream().filter(p -> p.getCollectTime().equals(d)).collect(Collectors.toList());
						for (int n = 0; n < latestDataInOneDay.size(); n++) {
							allData.add(latestDataInOneDay.get(n));
							if (!depth.contains(latestDataInOneDay.get(n).getDepth())) {
								depth.add(latestDataInOneDay.get(n).getDepth());
							}
						}
					}
				}
				// 深度排序
				depth.sort(new Comparator<Double>() {
					@Override
					public int compare(Double sp1, Double sp2) {
						if (sp1 < sp2) {
							return -1;
						} else if (sp1 == sp2) {
							return 0;
						} else {
							return 1;
						}
					}
				});

				for (int l = 0; l < depth.size(); l++) {
					double di = depth.get(l);
					// 根据深度循环,筛选出统一深度的，已按时间排序，从新到旧
					List<CX_Data> dataInSameDepth = allData.stream().filter(p -> p.getDepth() == di).collect(Collectors.toList());
					WeeklyReport_DeepOffset weekData = new WeeklyReport_CX();
					for (int j = 0; j < week.size(); j++) {
						cal.setTime(week.get(j));
						int day1 = cal.get(Calendar.DATE);// 获取日
						List<CX_Data> temp = new ArrayList<>();
						dataInSameDepth.stream().forEach((p) -> {
							cal.setTime(p.getCollectTime());
							int day2 = cal.get(Calendar.DATE);
							if (day1 == day2) {
								temp.add(p);
							}
						});
						if (temp.size() > 0) {
							weekData.setCode(temp.get(0).getSurveyPoint().getCode());
							gapOffset = temp.get(0).getGapOffset();
							switch (j) {
							case 0:
								weekData.setMonVal(Double.toString(gapOffset));
								break;
							case 1:
								weekData.setTueVal(Double.toString(gapOffset));
								break;
							case 2:
								weekData.setWedVal(Double.toString(gapOffset));
								break;
							case 3:
								weekData.setThurVal(Double.toString(gapOffset));
								break;
							case 4:
								weekData.setFriVal(Double.toString(gapOffset));
								break;
							case 5:
								weekData.setSatVal(Double.toString(gapOffset));
								break;
							case 6:
								weekData.setSunVal(Double.toString(gapOffset));
								break;
							}
						}
					}
					
					if(firstData.size() > 0){
						double d = depth.get(l);
						List<CX_Data> dataAtDepth = firstData.stream().filter(p -> p.getDepth() == d).collect(Collectors.toList());
						if(dataAtDepth.size() > 0){
							double oriVal = 0.0;
							oriVal = dataAtDepth.get(0).getCalValue();
							BigDecimal b = new BigDecimal(oriVal); 
							oriVal = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							weekData.setOriginalVal(Double.toString(oriVal));
							SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
							weekData.setOriginalDate(sd.format(dataAtDepth.get(0).getCollectTime()));
						}
					}
					weekData.setDepth(depth.get(l));
					dataList.add(weekData);
				}
				cx.put("dataList", dataList);
				dataMap.add(cx);
			}
			data.put("CX", dataMap);
		}
		
		//锚索
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18)){
			//存放星期几，数据
			List<WeeklyReport_MT> dataList = new ArrayList<>();
			//测点
			List<SurveyPoint_MT> sps = sp_MTService.getSP_MTs(project.getProjectUuid(), monitorItemService.getMonitorItemByNumber(18).getMonitorItemUuid());
			//第一条数据
			List<MT_Data> totalFirstData = new ArrayList<>();
			for(int ii = 0; ii < sps.size(); ii ++){
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(ii).getSurveyPointUuid());
				List<MT_Data> temp = mtDataDao.getFirstOneMTDatasBySurveyPoints(spUuid);
				if(temp.size() > 0){
					totalFirstData.add(temp.get(0));
				}
			}
			for(int i = 0; i < sps.size(); i++){
				//周一前的最新一条数据
				MT_Data beyondData = mtDataDao.getLatestDataBySurveyPointBeforeOneDateTime(sps.get(i).getSurveyPointUuid(), monday);
				List<String> spUuid = new ArrayList<>();
				spUuid.add(sps.get(i).getSurveyPointUuid());
				double gapOffset = 0.0;
//				double accumOffset = 0.0;
				double gapChangeRate = 0.0;
				double sumChange = 0.0;
				//当前应力值
				double currentVal = 0.0;
				WeeklyReport_MT weekData = new WeeklyReport_MT();
				//当天只有1条，跟前一天作对比
				List<MT_Data> dataInLastDay = new ArrayList<>();
				//选中日期一周内的所有数据,从新到旧
				List<MT_Data> allDataInOneWeek = mtDataDao.getDataBySurveyPointInOnePeriod(sps.get(i).getSurveyPointUuid(), monday, sunday);
				Calendar cal = Calendar.getInstance();
				for(int j = 0; j < week.size(); j++){
					 cal.setTime(week.get(j));
				        int day1 = cal.get(Calendar.DATE);//获取日 
					List<MT_Data> dataInOneDay = new ArrayList<>();
					allDataInOneWeek.stream().forEach((p) -> {
						cal.setTime(p.getCollectTime());
						int day2 = cal.get(Calendar.DATE);
						if(day1 == day2){
							dataInOneDay.add(p);
						}
					});
					if(dataInOneDay.size() > 0){
						List<MT_Data> firstData = totalFirstData.stream().filter(p -> p.getSurveyPoint().getCode().equals(dataInOneDay.get(0).getSurveyPoint().getCode())).collect(Collectors.toList());
						if(dataInOneDay.size() > 1){
							currentVal = dataInOneDay.get(0).getCalValue();
							//当天不止1条，当天的作对比
							gapOffset = dataInOneDay.get(0).getCalValue() - dataInOneDay.get(1).getCalValue();
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getCollectTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(1).getCollectTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}else if(dataInOneDay.size() == 1 && j != 0){
							//不是星期一
							
							int k = 1;
							while(dataInLastDay.size() == 0){
								int day = day1 - k;
								if(day < 0){
									break;
								}
								allDataInOneWeek.stream().forEach((p) -> {
									cal.setTime(p.getCollectTime());
									int day2 = cal.get(Calendar.DATE);
									if(day == day2){
										dataInLastDay.add(p);
									}
								});
								k++;
							}
							if(dataInLastDay.size() > 0){
								currentVal = dataInLastDay.get(0).getCalValue();
								gapOffset = dataInLastDay.get(0).getCalValue() - dataInOneDay.get(0).getCalValue();
								String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getCollectTime());
								long today = GtMath.fromDateStringToLong(dd);
								String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInLastDay.get(0).getCollectTime());
								long lastTime = GtMath.fromDateStringToLong(dd1);
								double days = ((today - lastTime)/(1000*60*60*24));
								BigDecimal bb = new BigDecimal(days); 
								days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
								if(days == 0.0){
									days = 1.0;
								}
								//单次变化速率
								gapChangeRate = gapOffset/days;
								BigDecimal b = new BigDecimal(gapChangeRate); 
								gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
								b = new BigDecimal(gapOffset); 
								gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							}
						}else if((dataInOneDay.size() == 1 || dataInLastDay.size() == 0) && beyondData != null){
							//星期一
							currentVal = dataInOneDay.get(0).getCalValue();
							gapOffset = beyondData.getCalValue() - dataInOneDay.get(0).getCalValue();
							String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dataInOneDay.get(0).getCollectTime());
							long today = GtMath.fromDateStringToLong(dd);
							String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(beyondData.getCollectTime());
							long lastTime = GtMath.fromDateStringToLong(dd1);
							double days = ((today - lastTime)/(1000*60*60*24));
							BigDecimal bb = new BigDecimal(days); 
							days = bb.setScale(0,   BigDecimal.ROUND_UP).doubleValue();  
							if(days == 0.0){
								days = 1.0;
							}
							//单次变化速率
							gapChangeRate = gapOffset/days;
							BigDecimal b = new BigDecimal(gapChangeRate); 
							gapChangeRate = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
							b = new BigDecimal(gapOffset); 
							gapOffset = b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
						}
						if(firstData.size() > 0){
							//内力初始值
							weekData.setOriginalVal(firstData.get(0).getCalValue());
							weekData.setCurrentVal(currentVal);
							sumChange += gapOffset;
							switch(j){
							case 0:
								weekData.setMonVal(gapOffset);
								break;
							case 1:
								weekData.setTueVal(gapOffset);
								break;
							case 2:
								weekData.setWedVal(gapOffset);
								break;
							case 3:
								weekData.setThurVal(gapOffset);
								break;
							case 4:
								weekData.setFriVal(gapOffset);
								break;
							case 5:
								weekData.setSatVal(gapOffset);
								break;
							case 6:
								weekData.setSunVal(gapOffset);
								break;
							}
						}
					} 
				}
				weekData.setCode(sps.get(i).getCode());
				weekData.setSumChange(sumChange);
				weekData.setSumChangeRate(sumChange / 7);
				dataList.add(weekData);
			} 
			data.put("MT", dataList);
		}
		
	}


	@Override
	public void weeklyReport(Map<String, Object> data, Project project) throws Exception {
		data.put("projectReport", projectReportDao.getProjectReportByProject(project.getProjectUuid()));
	}

	@Override
	public Map<String, Object> getLimitAccumOffsetAndBiggestChangeRateByPeriod(Project project,Date sDate, Date eDate) throws ParseException {
		Map<String, Object> dataList = new HashMap<String, Object>();
		//导出的日报的日期
		//2017-10-01 00:00:00     ~     2017-10-01 23:59:59
//		Calendar date = Calendar.getInstance();
//		date.setTime(eDate);
//		date.add(date.DATE, - 1);
//		date.add(Calendar.SECOND, 1);
//		//起始日期就是选中日期的前一天
//		Date sDate = sdf.parse(sdf.format(date.getTime()));
		ProjectReport pr= projectReportDao.getProjectReportByProject(project.getProjectUuid());
		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		int number=0;
		//水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1)){
			Map<String, Object> wys = new HashMap<String, Object>();
			//最新的一条数据
			List<WYS_CoordData> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_WYS> sps = sp_WYSService.getSP_WYSs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(1).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				List<WYS_CoordData> firstData = wysCoordDataDao.getFirstWYSCoordDataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<WYS_CoordData> latestTwoData = wysCoordDataDao.getTwoWYSCoordDataBySurveyPointInTwoSide(sps.get(i).getSurveyPointUuid(), sDate, eDate);
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
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
								tempFirst.get(0).getCaculateN(), tempFirst.get(0).getCaculateE())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumEOffset(accumOffset);
						latestTwoData.get(0).setGapEOffset(gapOffset);
						latestTwoData.get(0).setChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<WYS_CoordData>() {
			
						@Override
						public int compare(WYS_CoordData o1, WYS_CoordData o2) {
							double n1 = Math.abs(o1.getAccumEOffset());
							double n2 = Math.abs(o2.getAccumEOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					//累计变化量最小值
//					minWYSAccum.put(wysRecentData.get(0).getSurveyPoint().getCode(), wysRecentData.get(0).getAccumEOffset());
					//累计变化量最大值
//					maxWYSAccum.put(wysRecentData.get(wysRecentData.size() - 1).getSurveyPoint().getCode(), wysRecentData.get(wysRecentData.size() - 1).getAccumEOffset());
					wys.put("minAccumOffset", recentData.get(0).getAccumEOffset()+"");
					wys.put("minAccumOffsetCode",recentData.get(0).getSurveyPoint().getCode());
					wys.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumEOffset()+"");
					wys.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					wys.put("warnSingleRate", warning.getWarnSingleRate()+"");
					wys.put("warnAccum", warning.getWarnAccum()+"");
					wys.put("controlAccum", warning.getControlAccum()+"");
					//根据变化速率排序
					recentData.sort(new Comparator<WYS_CoordData>() {
			
						@Override
						public int compare(WYS_CoordData o1, WYS_CoordData o2) {
							double n1 = Math.abs(o1.getChangeRate());
							double n2 = Math.abs(o2.getChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxWYSChangeRate.put(wysRecentData.get(wysRecentData.size() - 1).getSurveyPoint().getCode(), wysRecentData.get(wysRecentData.size() - 1).getAccumEOffset());
					wys.put("maxChangeRate", recentData.get(recentData.size() - 1).getChangeRate()+"");
					wys.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					wys.put("remark", pr==null?"":pr.getP6p7());
				}else{
					wys.put("minAccumOffset","");
					wys.put("maxAccumOffset","");
					wys.put("maxChangeRate","");
					wys.put("minAccumOffsetCode","");
					wys.put("maxAccumOffsetCode","");
					wys.put("maxChangeRateCode","");
					wys.put("warnSingleRate", "");
					wys.put("warnAccum","");
					wys.put("controlAccum","");
				}
			}
			number=number+1;
			wys.put("number", number);
			dataList.put("WYS", wys);
		}
		//支护结构深层水平位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4)){
			Map<String, Object> cx = new HashMap<String, Object>();
			//最新的一条数据
			List<CX_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_CX> sps = sp_CXService.getSP_CXs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(4).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				for(int i = 0; i < sps.size(); i++){
					List<CX_Data> latestData = cxDataDao.getDataBySurveyPointInOnePeroid(sps.get(i).getSurveyPointUuid(), sDate, eDate);
					if(latestData.size() == 0){
						continue;
					}
					recentData.addAll(latestData);
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<CX_Data>() {
			
						@Override
						public int compare(CX_Data o1, CX_Data o2) {
							double n1 = Math.abs(o1.getAccumOffset());
							double n2 = Math.abs(o2.getAccumOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					cx.put("minAccumOffset",recentData.get(0).getAccumOffset()+"");
					cx.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					cx.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumOffset()+"");
					cx.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					cx.put("warnSingleRate", warning.getWarnSingleRate()+"");
					cx.put("warnAccum", warning.getWarnAccum()+"");
					cx.put("controlAccum", warning.getControlAccum()+"");
					
					//根据变化速率排序
					recentData.sort(new Comparator<CX_Data>() {
			
						@Override
						public int compare(CX_Data o1, CX_Data o2) {
							double n1 = Math.abs(o1.getChangeRate());
							double n2 = Math.abs(o2.getChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					cx.put("maxChangeRate", recentData.get(recentData.size() - 1).getChangeRate()+"");
					cx.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					cx.put("remark", pr==null?"":pr.getP6p35());
				}else{
					cx.put("minAccumOffset","");
					cx.put("maxAccumOffset","");
					cx.put("maxChangeRate","");
					cx.put("minAccumOffsetCode","");
					cx.put("maxAccumOffsetCode","");
					cx.put("maxChangeRateCode","");
					cx.put("warnSingleRate", "");
					cx.put("warnAccum","");
					cx.put("controlAccum","");
				}
			}
			number=number+1;
			cx.put("number", number);
			dataList.put("CX", cx);
		}
		//顶部竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5)){
			Map<String, Object> wyd = new HashMap<String, Object>();
			//最新的一条数据
			List<WYD_LevelData> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_WYD> sps = sp_WYDService.getSP_WYDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(5).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				List<WYD_LevelData> firstData = wydDataDao.getFirstWYDLevelDataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<WYD_LevelData> latestTwoData = wydDataDao.getTwoDataBySurveyPointInTwoSide(sps.get(i).getSurveyPointUuid(), sDate, eDate);
		//			List<WYS_CoordData> firstData = ((IWYSCoordDataDao)getDao()).getFirstWYSCoordDataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - tempFirst.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<WYD_LevelData>() {
			
						@Override
						public int compare(WYD_LevelData o1, WYD_LevelData o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					wyd.put("minAccumOffset", recentData.get(0).getAccumHOffset()+"");
					wyd.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					wyd.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumHOffset()+"");
					wyd.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					wyd.put("warnSingleRate", warning.getWarnSingleRate()+"");
					wyd.put("warnAccum", warning.getWarnAccum()+"");
					wyd.put("controlAccum", warning.getControlAccum()+"");
					//根据变化速率排序
					recentData.sort(new Comparator<WYD_LevelData>() {
			
						@Override
						public int compare(WYD_LevelData o1, WYD_LevelData o2) {
							double n1 = Math.abs(o1.getGapHChangeRate());
							double n2 = Math.abs(o2.getGapHChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxChangeRate.put(recentData.get(recentData.size() - 1).getSurveyPoint().getCode(), recentData.get(recentData.size() - 1).getAccumHOffset());
					wyd.put("maxChangeRate",  recentData.get(recentData.size() - 1).getGapHChangeRate()+"");
					wyd.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					wyd.put("remark", pr==null?"":pr.getP7p16());
				}else{
					wyd.put("minAccumOffset","");
					wyd.put("maxAccumOffset","");
					wyd.put("maxChangeRate","");
					wyd.put("minAccumOffsetCode","");
					wyd.put("maxAccumOffsetCode","");
					wyd.put("maxChangeRateCode","");
					wyd.put("warnSingleRate","");
					wyd.put("warnAccum", "");
					wyd.put("controlAccum","");
				}
			}
			number=number+1;
			wyd.put("number", number);
			dataList.put("WYD", wyd);
		}
		//立柱竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6)){
			Map<String, Object> lz = new HashMap<String, Object>();
			//最新的一条数据
			List<LZ_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_LZ> sps = sp_LZService.getSP_LZs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(6).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<LZ_Data> firstData = lzDataDao.getFirstLZ_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<LZ_Data> latestTwoData = lzDataDao.getTwoDataBySurveyPointInTwoSide(sps.get(i).getSurveyPointUuid(),sDate,eDate);
					List<LZ_Data> firstData = lzDataDao.getFirstLZ_DataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<LZ_Data>() {
			
						@Override
						public int compare(LZ_Data o1, LZ_Data o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					lz.put("minAccumOffset", recentData.get(0).getAccumHOffset()+"");
					lz.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					lz.put("maxAccumOffset",recentData.get(recentData.size() - 1).getAccumHOffset()+"");
					lz.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					lz.put("warnSingleRate", warning.getWarnSingleRate()+"");
					lz.put("warnAccum", warning.getWarnAccum()+"");
					lz.put("controlAccum", warning.getControlAccum()+"");
					//根据变化速率排序
					recentData.sort(new Comparator<LZ_Data>() {
			
						@Override
						public int compare(LZ_Data o1, LZ_Data o2) {
							double n1 = Math.abs(o1.getGapHOffsetChangeRate());
							double n2 = Math.abs(o2.getGapHOffsetChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxChangeRate.put(recentData.get(recentData.size() - 1).getSurveyPoint().getCode(), recentData.get(recentData.size() - 1).getAccumHOffset());
					lz.put("maxChangeRate",recentData.get(recentData.size() - 1).getGapHOffsetChangeRate()+"");
					lz.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					lz.put("remark", pr==null?"":pr.getP7p64());
				}else{
					lz.put("minAccumOffset","");
					lz.put("maxAccumOffset","");
					lz.put("maxChangeRate","");
					lz.put("minAccumOffsetCode","");
					lz.put("maxAccumOffsetCode","");
					lz.put("maxChangeRateCode","");
					lz.put("warnSingleRate", "");
					lz.put("warnAccum", "");
					lz.put("controlAccum", "");
				}
			}
			number=number+1;
			lz.put("number", number);
			dataList.put("LZ", lz);
		}
		//周边建筑物竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8)){
			Map<String, Object> sm = new HashMap<String, Object>();
			//最新的一条数据
			List<SM_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SM> sps = sp_SMService.getSP_SMs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(8).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SM_Data> latestTwoData = smDataDao.getTwoDataBySurveyPointInTwoSide(sps.get(i).getSurveyPointUuid(),sDate,eDate);
					List<SM_Data> firstData = smDataDao.getFirstSM_DataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHOffsetChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<SM_Data>() {
			
						@Override
						public int compare(SM_Data o1, SM_Data o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					sm.put("minAccumOffset",recentData.get(0).getAccumHOffset()+"");
					sm.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					sm.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumHOffset()+"");
					sm.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					sm.put("warnSingleRate", warning.getWarnSingleRate()+"");
					sm.put("warnAccum", warning.getWarnAccum()+"");
					sm.put("controlAccum", warning.getControlAccum()+"");
					//根据变化速率排序
					recentData.sort(new Comparator<SM_Data>() {
			
						@Override
						public int compare(SM_Data o1, SM_Data o2) {
							double n1 = Math.abs(o1.getGapHOffsetChangeRate());
							double n2 = Math.abs(o2.getGapHOffsetChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
//					maxChangeRate.put(recentData.get(recentData.size() - 1).getSurveyPoint().getCode(), recentData.get(recentData.size() - 1).getAccumHOffset());
					sm.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapHOffsetChangeRate()+"");
					sm.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					sm.put("remark", pr==null?"":pr.getP6p21());
					
				}else{
					sm.put("minAccumOffset","");
					sm.put("maxAccumOffset","");
					sm.put("maxChangeRate","");
					sm.put("minAccumOffsetCode","");
					sm.put("maxAccumOffsetCode","");
					sm.put("maxChangeRateCode","");
					sm.put("warnSingleRate","");
					sm.put("warnAccum", "");
					sm.put("controlAccum", "");
				}
			}
			number=number+1;
			sm.put("number", number);
			dataList.put("SM", sm);
		}
		//周边管线竖向位移
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10)){
			Map<String, Object> zgd = new HashMap<String, Object>();
			//最新的一条数据
			List<ZGD_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZGD> sps = sp_ZGDService.getSP_ZGDs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(10).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZGD_Data> latestTwoData = zgdDataDao.getTwoDataBySurveyPointInTwoSide(sps.get(i).getSurveyPointUuid(),sDate,eDate);
					List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						// 单次位移,化为毫米
						gapOffset = (latestTwoData.get(0).getLevelH() - latestTwoData.get(1).getLevelH())*1000;
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getSurveyTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getSurveyTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = (latestTwoData.get(0).getLevelH() - firstData.get(0).getLevelH())*1000 + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumHOffset(accumOffset);
						latestTwoData.get(0).setGapHOffset(gapOffset);
						latestTwoData.get(0).setGapHChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<ZGD_Data>() {
			
						@Override
						public int compare(ZGD_Data o1, ZGD_Data o2) {
							double n1 = Math.abs(o1.getAccumHOffset());
							double n2 = Math.abs(o2.getAccumHOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					zgd.put("minAccumOffset", recentData.get(0).getAccumHOffset());
					zgd.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					zgd.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumHOffset()+"");
					zgd.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					zgd.put("warnSingleRate", warning.getWarnSingleRate()+"");
					zgd.put("warnAccum", warning.getWarnAccum()+"");
					zgd.put("controlAccum", warning.getControlAccum()+"");
					//根据变化速率排序
					recentData.sort(new Comparator<ZGD_Data>() {
			
						@Override
						public int compare(ZGD_Data o1, ZGD_Data o2) {
							double n1 = Math.abs(o1.getGapHChangeRate());
							double n2 = Math.abs(o2.getGapHChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					zgd.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapHChangeRate()+"");
					zgd.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					zgd.put("remark", pr==null?"":pr.getP6p28());
				}else{
					zgd.put("minAccumOffset","");
					zgd.put("maxAccumOffset","");
					zgd.put("maxChangeRate","");
					zgd.put("minAccumOffsetCode","");
					zgd.put("maxAccumOffsetCode","");
					zgd.put("maxChangeRateCode","");
					zgd.put("warnSingleRate", "");
					zgd.put("warnAccum", "");
					zgd.put("controlAccum", "");
				}
			}
			number=number+1;
			zgd.put("number", number);
			dataList.put("ZGD", zgd);
		}
		//地下水位
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12)){
			Map<String, Object> sw = new HashMap<String, Object>();
			//最新的一条数据
			List<SW_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_SW> sps = sp_SWService.getSP_SWs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(12).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<SW_Data> latestTwoData = swDataDao.getLatestTwoDataBySurveyPoint(sps.get(i).getSurveyPointUuid());
					List<SW_Data> firstData = swDataDao.getFirstOneSWDatasBySurveyPoints(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						//单位毫米
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
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
					if(firstData.size() > 0 && latestTwoData.size() > 0){
						// 累计位移，化为毫米
						accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
						BigDecimal bb = new BigDecimal(accumOffset); 
						accumOffset = bb.setScale(1,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						latestTwoData.get(0).setAccumOffset(accumOffset);
						latestTwoData.get(0).setGapOffset(gapOffset);
						latestTwoData.get(0).setGapChangeRate(changeRate);
						recentData.add(latestTwoData.get(0));
					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
					recentData.sort(new Comparator<SW_Data>() {
			
						@Override
						public int compare(SW_Data o1, SW_Data o2) {
							double n1 = Math.abs(o1.getAccumOffset());
							double n2 = Math.abs(o2.getAccumOffset());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					sw.put("minAccumOffset", recentData.get(0).getAccumOffset());
					sw.put("minAccumOffsetCode", recentData.get(0).getSurveyPoint().getCode());
					sw.put("maxAccumOffset", recentData.get(recentData.size() - 1).getAccumOffset()+"");
					sw.put("maxAccumOffsetCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					sw.put("warnSingleRate", warning.getWarnSingleRate()+"");
					sw.put("warnAccum", warning.getWarnAccum()+"");
					sw.put("controlAccum", warning.getControlAccum()+"");
					
					//根据变化速率排序
					recentData.sort(new Comparator<SW_Data>() {
			
						@Override
						public int compare(SW_Data o1, SW_Data o2) {
							double n1 = Math.abs(o1.getGapChangeRate());
							double n2 = Math.abs(o2.getGapChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					sw.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapChangeRate()+"");
					sw.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					sw.put("remark", pr==null?"":pr.getP6p49());
				}else{
					sw.put("minAccumOffset","");
					sw.put("maxAccumOffset","");
					sw.put("maxChangeRate","");
					sw.put("minAccumOffsetCode","");
					sw.put("maxAccumOffsetCode","");
					sw.put("maxChangeRateCode","");
					sw.put("warnSingleRate","");
					sw.put("warnAccum", "");
					sw.put("controlAccum", "");
				}
			}
			number=number+1;
			sw.put("number", number);
			dataList.put("SW", sw);
		}
		//支撑内力
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 15)){
			Map<String, Object> zc = new HashMap<String, Object>();
			//最新的一条数据
			List<ZC_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_ZC> sps = sp_ZCService.getSP_ZCs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(15).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<ZC_Data> latestTwoData = zcDataDao.getTwoDataBySurveyPointInTwoSide(sps.get(i).getSurveyPointUuid(),sDate,eDate);
//					List<ZC_Data> firstData = zcDataDao.getFirstOneZCDatasBySurveyPoints(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						//单位kN
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
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
						//精确到0.01
						gapOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.01
						changeRate = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
//					if(firstData.size() > 0 ){
						if(latestTwoData.size() > 0){
	//						accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
	//						BigDecimal bb = new BigDecimal(accumOffset); 
	//						accumOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
	//						latestTwoData.get(0).setAccumOffset(accumOffset);
							latestTwoData.get(0).setGapOffset(gapOffset);
							latestTwoData.get(0).setGapChangeRate(changeRate);
							recentData.add(latestTwoData.get(0));
						}
//					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
//					recentData.sort(new Comparator<ZC_Data>() {
//			
//						@Override
//						public int compare(ZC_Data o1, ZC_Data o2) {
//							double n1 = Math.abs(o1.getAccumOffset());
//							double n2 = Math.abs(o2.getAccumOffset());
//							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
//						}
//					});
//					zc.put("minAccumOffset", recentData.get(0).getSurveyPoint().getCode()+','+recentData.get(0).getAccumOffset());
//					zc.put("maxAccumOffset", recentData.get(recentData.size() - 1).getSurveyPoint().getCode()+','+recentData.get(recentData.size() - 1).getAccumOffset());
					
					//根据变化速率排序
					recentData.sort(new Comparator<ZC_Data>() {
			
						@Override
						public int compare(ZC_Data o1, ZC_Data o2) {
							double n1 = Math.abs(o1.getGapChangeRate());
							double n2 = Math.abs(o2.getGapChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					zc.put("warnSingleRate", warning.getWarnSingleRate()+"");
					zc.put("warnAccum", warning.getWarnAccum()+"");
					zc.put("controlAccum", warning.getControlAccum()+"");
					
					zc.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapChangeRate()+"");
					zc.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					zc.put("remark", pr==null?"":pr.getP7p72());
					//根据力大小排序
					recentData.sort(new Comparator<ZC_Data>() {
			
						@Override
						public int compare(ZC_Data o1, ZC_Data o2) {
							double n1 = Math.abs(o1.getCalValue());
							double n2 = Math.abs(o2.getCalValue());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					zc.put("maxCalVal", recentData.get(recentData.size() - 1).getGapChangeRate()+"");
					zc.put("maxCalValCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
//					zc.put("warnSingleRate","");
//					zc.put("warnAccum","");
//					zc.put("controlAccum", "");
//					zc.put("maxChangeRate", "");
//					zc.put("maxChangeRateCode","");
//					zc.put("maxCalVal", "");
//					zc.put("maxCalValCode", "");
				}
			}
			number=number+1;
			zc.put("number", number);
			dataList.put("ZC", zc);
		}
		//锚索
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18)){
			Map<String, Object> mt = new HashMap<String, Object>();
			//最新的一条数据
			List<MT_Data> recentData = new ArrayList<>();
			// 首先获取对应项目下对应监测项的所有监测点
			List<SurveyPoint_MT> sps = sp_MTService.getSP_MTs(project.getProjectUuid(),monitorItemService.getMonitorItemByNumber(18).getMonitorItemUuid());
			if(sps.size() > 0){
				//所有监测点Uuid的List
				List<String> spUuids = new ArrayList<>();
				sps.stream().forEach(p -> spUuids.add(p.getSurveyPointUuid()));
	//			List<ZGD_Data> firstData = zgdDataDao.getFirstZGD_DataBySurveyPoint(spUuids);
				for(int i = 0; i < sps.size(); i++){
					List<String> sp = new ArrayList<>();
					sp.add(sps.get(i).getSurveyPointUuid());
					List<MT_Data> latestTwoData = mtDataDao.getTwoDataBySurveyPointInTwoSide(sps.get(i).getSurveyPointUuid(),sDate,eDate);
//					List<MT_Data> firstData = mtDataDao.getFirstOneMTDatasBySurveyPoints(sp);
					double gapOffset = 0.0;
					double accumOffset = 0.0;
					double changeRate = 0.0;
					if(latestTwoData.size() == 0){
						continue;
					}
					if(latestTwoData.size() == 2){
						//单位kN
						gapOffset = latestTwoData.get(0).getCalValue() - latestTwoData.get(1).getCalValue();
						String dd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(0).getCollectTime());
						long today = GtMath.fromDateStringToLong(dd);
						String dd1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(latestTwoData.get(1).getCollectTime());
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
						//精确到0.01
						gapOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
						bb = new BigDecimal(changeRate);
						//精确到0.01
						changeRate = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
					}
//					if(firstData.size() > 0){
						if(latestTwoData.size() > 0){
	//						accumOffset = latestTwoData.get(0).getCalValue() - firstData.get(0).getCalValue() + sps.get(i).getOriginalTotalValue();
	//						BigDecimal bb = new BigDecimal(accumOffset); 
	//						accumOffset = bb.setScale(2,   BigDecimal.ROUND_HALF_EVEN).doubleValue();  
	//						latestTwoData.get(0).setAccumOffset(accumOffset);
							latestTwoData.get(0).setGapOffset(gapOffset);
							latestTwoData.get(0).setGapChangeRate(changeRate);
							recentData.add(latestTwoData.get(0));
						}
//					}
				}
				if(recentData.size() > 0){
					//根据累计变化量排序
//					recentData.sort(new Comparator<MT_Data>() {
//			
//						@Override
//						public int compare(MT_Data o1, MT_Data o2) {
//							double n1 = Math.abs(o1.getAccumOffset());
//							double n2 = Math.abs(o2.getAccumOffset());
//							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
//						}
//					});
//					mt.put("minAccumOffset", recentData.get(0).getSurveyPoint().getCode()+','+recentData.get(0).getAccumOffset());
//					mt.put("maxAccumOffset", recentData.get(recentData.size() - 1).getSurveyPoint().getCode()+','+recentData.get(recentData.size() - 1).getAccumOffset());
					
					//根据变化速率排序
					recentData.sort(new Comparator<MT_Data>() {
			
						@Override
						public int compare(MT_Data o1, MT_Data o2) {
							double n1 = Math.abs(o1.getGapChangeRate());
							double n2 = Math.abs(o2.getGapChangeRate());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					Warning warning = recentData.get(recentData.size() - 1).getSurveyPoint().getWarning();
					mt.put("warnSingleRate", warning.getWarnSingleRate()+"");
					mt.put("warnAccum", warning.getWarnAccum()+"");
					mt.put("controlAccum", warning.getControlAccum()+"");
					
					mt.put("maxChangeRate", recentData.get(recentData.size() - 1).getGapChangeRate()+"");
					mt.put("maxChangeRateCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
					mt.put("remark", pr==null?"":pr.getP6p42());
					//根据力大小排序
					recentData.sort(new Comparator<MT_Data>() {
			
						@Override
						public int compare(MT_Data o1, MT_Data o2) {
							double n1 = Math.abs(o1.getCalValue());
							double n2 = Math.abs(o2.getCalValue());
							return (n1 == n2 ? 0 : (n1 > n2 ? 1 : -1));
						}
					});
					mt.put("maxCalVal", recentData.get(recentData.size() - 1).getCalValue()+"");
					mt.put("maxCalValCode", recentData.get(recentData.size() - 1).getSurveyPoint().getCode());
				}else{
//					mt.put("warnSingleRate", "");
//					mt.put("warnAccum", "");
//					mt.put("controlAccum", "");
//					mt.put("maxChangeRate", "");
//					mt.put("maxChangeRateCode", "");
//					mt.put("maxChangeRate", "");
//					mt.put("maxChangeRateCode", "");
				}
			}
			number=number+1;
			mt.put("number", number);
			dataList.put("MT", mt);
		}
		return dataList;
	}

}
