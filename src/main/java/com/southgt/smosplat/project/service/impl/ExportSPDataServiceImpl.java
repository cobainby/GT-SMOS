package com.southgt.smosplat.project.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.ShapeTypes;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.southgt.smosplat.data.entity.CX_Data;
import com.southgt.smosplat.data.entity.LZ_Data;
import com.southgt.smosplat.data.entity.MT_Data;
import com.southgt.smosplat.data.entity.SM_Data;
import com.southgt.smosplat.data.entity.SW_Data;
import com.southgt.smosplat.data.entity.WYD_LevelData;
import com.southgt.smosplat.data.entity.WYS_CoordData;
import com.southgt.smosplat.data.entity.ZC_Data;
import com.southgt.smosplat.data.entity.ZGD_Data;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.ProjectMonitorItem;
import com.southgt.smosplat.project.entity.ProjectReport;
import com.southgt.smosplat.project.service.IExportSPDataService;
import com.southgt.smosplat.project.service.IProjectMonitorItemService;
import com.southgt.smosplat.project.service.IReportService;

@Service("exportSPDataService")
public class ExportSPDataServiceImpl implements IExportSPDataService {
	@Value("#{uploadProperties['uploadFileSrc']}")
	private  String uploadFileSrc;
	
	@Resource
	IReportService reportService;
	
	@Resource
	IProjectMonitorItemService projectMonitorItemService;
	
	private boolean hasMonitorItemByNumber(List<ProjectMonitorItem> projectMonitorItems,byte number){
		for (ProjectMonitorItem projectMonitorItem : projectMonitorItems) {
			if(projectMonitorItem.getMonitorItem().getNumber()==number){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String exportExcelCoreService(boolean isDaily, List<String> templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,ProjectReport pr) {
		String path=uploadFileSrc+"/dataReport/"+project.getProjectUuid();
		String tempPath=uploadFileSrc+"/temp/";
		String urlPath="/smosplatUploadFiles"+uploadFileSrc.split("smosplatUploadFiles")[1]+"/temp/";
		String zipName="";
		if(isDaily){
			tempPath=tempPath+project.getProjectUuid()+"/daily/";
			urlPath=urlPath+project.getProjectUuid()+"/daily/";
			zipName=project.getProjectName()+"日报数据报表"+startTime.split(" ")[0];
		}else{
			tempPath=tempPath+project.getProjectUuid()+"/weekly/";
			urlPath=urlPath+project.getProjectUuid()+"/weekly/";
			zipName=project.getProjectName()+"周报数据报表"+startTime.split(" ")[0]+"至"+endTime.split(" ")[0];
		}
		
		File directory=new File(path);
		if(!(directory.exists())){
			directory.mkdirs();
		}else{
			deleteAllFilesOfDir(directory);
			directory.mkdirs();
		}
		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		int fulu=2;
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1)){
			exportWYSDataToExcel(isDaily,templatePath.get(0), pjName, pjAddress,pr.getP1p78(), project, pr.getP1p79(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4)){
			exportCXDataToExcel(isDaily,templatePath.get(3), pjName, pjAddress,pr.getP1p90(), project, pr.getP1p91(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5)){
			exportWYDDataToExcel(isDaily,templatePath.get(0), pjName, pjAddress,pr.getP1p80(), project, pr.getP1p81(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6)){
			exportLZataToExcel(isDaily,templatePath.get(0), pjName, pjAddress,pr.getP1p92(), project, pr.getP1p93(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8)){
			exportSMDataToExcel(isDaily,templatePath.get(0), pjName, pjAddress,pr.getP1p84(), project, pr.getP1p85(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10)){
			exportZGDDataToExcel(isDaily,templatePath.get(0), pjName, pjAddress,pr.getP1p86(), project, pr.getP1p87(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12)){
			exportSWDataToExcel(isDaily,templatePath.get(0), pjName, pjAddress,pr.getP1p88(), project, pr.getP1p89(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 15)){
			exportZCDataToExcel(isDaily,templatePath.get(2), pjName, pjAddress,pr.getP1p94(), project, pr.getP1p95(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18)){
			exportMTDataToExcel(isDaily,templatePath.get(1), pjName, pjAddress,pr.getP1p82(), project, pr.getP1p83(), startTime, endTime,fulu);
			fulu=fulu+1;
		}
		
		boolean f=fileToZip(path,tempPath,zipName);
		if(f){
			return urlPath+"/"+zipName+".zip";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportWYSDataToExcel(boolean isDaily,String templatePath, String pjName, String pjAddress, String deviceName,
			Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "WYS");
			dataMap=(Map<String, Object>) map.get("WYS");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<WYS_CoordData> coordDataList = (List<WYS_CoordData>) dMap.get("data");
				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
					}
				}
			}
			String spCodeCharStr="";
			for(int i=0;i<spCodeChar.size();i++){
				if(i<spCodeChar.size()-1){
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i)+"、";
				}else{
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i);
				}
			}
			spCodeCharStr=spCodeCharStr+"表示围护墙(边坡)顶部水平位移";

			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("围护墙(边坡)顶部水平位移");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "围护墙(边坡)顶部水平位移");
			}
			//往折线图的数据源表dataSheet里填数据
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<WYS_CoordData> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<WYS_CoordData>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getSurveyTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getAccumEOffset());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "围护墙(边坡)顶部水平位移监测结果表", "围护墙(边坡)顶部水平位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<WYS_CoordData> coordDataList = (List<WYS_CoordData>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<coordDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalE")==null? "":dMap.get("originalE").toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumEOffset());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getSurveyTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getGapEOffset());
							sheet.getRow(dataRow).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumEOffset());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("WYS", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="围护墙(边坡)顶部水平位移监测结果"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportWYDDataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "WYD");
			dataMap=(Map<String, Object>) map.get("WYD");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<WYD_LevelData> coordDataList = (List<WYD_LevelData>) dMap.get("data");
				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
					}
				}
			}
			String spCodeCharStr="";
			for(int i=0;i<spCodeChar.size();i++){
				if(i<spCodeChar.size()-1){
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i)+"、";
				}else{
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i);
				}
			}
			spCodeCharStr=spCodeCharStr+"表示围护墙(边坡)顶部竖向位移";

			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("围护墙(边坡)顶部竖向位移");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "围护墙(边坡)顶部竖向位移");
			}
			//往折线图的数据源表dataSheet里填数据
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<WYD_LevelData> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<WYD_LevelData>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getSurveyTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getAccumHOffset());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "围护墙(边坡)顶部竖向位移监测结果表", "围护墙(边坡)顶部竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<WYD_LevelData> coordDataList = (List<WYD_LevelData>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<coordDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getSurveyTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getGapHOffset());
							sheet.getRow(dataRow).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumHOffset());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapHChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("WYD", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="围护墙(边坡)顶部竖向位移监测结果"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportLZataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "LZ");
			dataMap=(Map<String, Object>) map.get("LZ");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<LZ_Data> coordDataList = (List<LZ_Data>) dMap.get("data");
				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
					}
				}
			}
			String spCodeCharStr="";
			for(int i=0;i<spCodeChar.size();i++){
				if(i<spCodeChar.size()-1){
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i)+"、";
				}else{
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i);
				}
			}
			spCodeCharStr=spCodeCharStr+"表示立柱竖向位移";

			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("立柱竖向位移");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "立柱竖向位移");
			}
			//往折线图的数据源表dataSheet里填数据
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<LZ_Data> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<LZ_Data>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getSurveyTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getAccumHOffset());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "立柱竖向位移监测结果表", "立柱竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<LZ_Data> coordDataList = (List<LZ_Data>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<coordDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getSurveyTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getGapHOffset());
							sheet.getRow(dataRow).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumHOffset());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapHOffsetChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("LZ", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="立柱竖向位移位移监测结果"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportSMDataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "SM");
			dataMap=(Map<String, Object>) map.get("SM");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<SM_Data> coordDataList = (List<SM_Data>) dMap.get("data");
				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
					}
				}
			}
			String spCodeCharStr="";
			for(int i=0;i<spCodeChar.size();i++){
				if(i<spCodeChar.size()-1){
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i)+"、";
				}else{
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i);
				}
			}
			spCodeCharStr=spCodeCharStr+"表示周边建筑物竖向位移";

			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("周边建筑物竖向位移");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "周边建筑物竖向位移");
			}
			//往折线图的数据源表dataSheet里填数据
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<SM_Data> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<SM_Data>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getSurveyTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getAccumHOffset());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "周边建筑物竖向位移监测结果表", "周边建筑物竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<SM_Data> coordDataList = (List<SM_Data>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<coordDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getSurveyTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getGapHOffset());
							sheet.getRow(dataRow).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumHOffset());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapHOffsetChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("SM", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="周边建筑物竖向位移监测结果"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportZGDDataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "ZGD");
			dataMap=(Map<String, Object>) map.get("ZGD");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<ZGD_Data> coordDataList = (List<ZGD_Data>) dMap.get("data");
				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
					}
				}
			}
			String spCodeCharStr="";
			for(int i=0;i<spCodeChar.size();i++){
				if(i<spCodeChar.size()-1){
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i)+"、";
				}else{
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i);
				}
			}
			spCodeCharStr=spCodeCharStr+"表示周边管线竖向位移";

			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("周边管线竖向位移");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "周边管线竖向位移");
			}
			//往折线图的数据源表dataSheet里填数据
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<ZGD_Data> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<ZGD_Data>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getSurveyTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getAccumHOffset());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "周边管线竖向位移监测结果表", "周边管线竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<ZGD_Data> coordDataList = (List<ZGD_Data>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<coordDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getSurveyTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getGapHOffset());
							sheet.getRow(dataRow).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumHOffset());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapHChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("ZGD", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="周边管线竖向位移监测结果"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportSWDataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "SW");
			dataMap=(Map<String, Object>) map.get("SW");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<SW_Data> coordDataList = (List<SW_Data>) dMap.get("data");
				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
					}
				}
			}
			String spCodeCharStr="";
			for(int i=0;i<spCodeChar.size();i++){
				if(i<spCodeChar.size()-1){
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i)+"、";
				}else{
					spCodeCharStr=spCodeCharStr+spCodeChar.get(i);
				}
			}
			spCodeCharStr=spCodeCharStr+"表示地下水位";

			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("地下水位");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "地下水位");
			}
			//往折线图的数据源表dataSheet里填数据
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<SW_Data> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<SW_Data>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getCollectTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getAccumOffset());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "地下水位监测结果表", "地下水位监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<SW_Data> coordDataList = (List<SW_Data>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<coordDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumOffset());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getCollectTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getGapOffset());
							sheet.getRow(dataRow).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumOffset());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("SW", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="地下水位监测结果"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportMTDataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "MT");
			dataMap=(Map<String, Object>) map.get("MT");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
//			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
//				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
//				List<MT_Data> coordDataList = (List<MT_Data>) dMap.get("data");
//				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
//					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
//						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
//					}
//				}
			}
			String spCodeCharStr="";
			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("锚杆内力");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "锚杆内力");
			}
			//往折线图的数据源表dataSheet里填数据，多了一行表头
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<MT_Data> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<MT_Data>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getCollectTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getCalValue());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "锚杆内力监测结果表", "锚杆内力监测", "初始读数(kN)", "初始读数(kN)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<MT_Data> coordDataList = (List<MT_Data>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
//				int pFlag=0;
//				for(int j=0 ;j<coordDataList.size();j++){
//					for(int p=pFlag;p<periods.size();p++){
//						pFlag+=1;
//						//先填初始读数或上次累计值
//						if(p%5==0){
//							if(p==0){
//								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
//							}else{
//								if(j!=0){
//									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
//								}
//							}
//						}
//						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getCollectTime())).equals(periods.get(p))){
//							int pageNum=p/5+1;
//							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
//							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getCalValue());
//							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapChangeRate());
//							break;
//						}
//					}
//				}
				
				for(int p=0;p<periods.size();p++){
					for(int j=0 ;j<coordDataList.size();j++){
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getCollectTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getCalValue());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("MT", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="锚杆内力"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportZCDataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {

		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "ZC");
			dataMap=(Map<String, Object>) map.get("ZC");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			List<String> codes=new ArrayList<String>();
//			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
//				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
//				List<ZC_Data> coordDataList = (List<ZC_Data>) dMap.get("data");
//				for (int i = 0; i < coordDataList.size(); i++) {
//					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
//						periods.add(coordDataList.get(i).getSurveyTime());
//					}
//					if (!(spCodeChar.contains(coordDataList.get(i).getSurveyPoint().getCodeChar()))) {
//						spCodeChar.add(coordDataList.get(i).getSurveyPoint().getCodeChar());
//					}
//				}
			}
			String spCodeCharStr="";
			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//找到模板sheet
			XSSFSheet templateSheet=null;
			XSSFSheet sheet=null;
			XSSFSheet dataSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("grid")){
					sheet=tempSheet;
				}
				if(tempSheet.getSheetName().equals("图的数据源")){
					dataSheet=tempSheet;
//					for(int i=0;i<8;i++){
//						if(dataSheet.getRow(0)!=null){
//							dataSheet.removeRow(dataSheet.getRow(0));
//						}
//					}
				}
			}
			if(isDaily){
				workbook.removeSheetAt(workbook.getSheetIndex(sheet));
				sheet=workbook.createSheet("支撑内力");
			}else{
				workbook.setSheetName(workbook.getSheetIndex("grid"), "支撑内力");
			}
			//往折线图的数据源表dataSheet里填数据，多了一行表头
			for(int i=0;i<codes.size()+1;i++){
				XSSFRow row=dataSheet.createRow(i);
				Map<String, Object> dMap=new HashMap<>();
				List<ZC_Data> coordDataList=new ArrayList<>();
				if(i>0){
					dMap = (Map<String, Object>)dataMap.get(codes.get(i-1));
					coordDataList=(List<ZC_Data>) dMap.get("data");
				}
				for(int j=0;j<periods.size()+1;j++){
					XSSFCell cell=row.createCell(j);
					if(i==0&&j>0){
						//第一行是日期
						cell.setCellValue(periods.get(j-1));
					}else if(i>0&&j==0){
						//第一列是点名
						cell.setCellValue(codes.get(i-1));
					}else if(i>0&&j>0){
						//数据
						for(int l=0;l<coordDataList.size();l++){
							if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(l).getCollectTime())).equals(periods.get(j-1))){
								cell.setCellValue(coordDataList.get(l).getGapOffset());
							}
						}
						
					}
				}
			}
			//以下处理展示用的sheet
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//横着数总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			int dataEachPage=24;
			//创建所有的表头表尾
			createHeader( dataEachPage, sheet, templateSheet, pageCount, columnCount, pjName, pjAddress,
					 deviceName, specification, codes, periods, "支撑内力监测结果表", "支撑内力监测", "初始读数(kN)", "初始读数(kN)",fulu,spCodeCharStr);
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				//该code数据所处的行数
				int dataRow=i%dataEachPage+9+(11+dataEachPage)*(i/24);
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<MT_Data> coordDataList = (List<MT_Data>) dMap.get("data");
				//添加监测点编号到表头
				for(int p=0;p<pageCount;p++){
					sheet.getRow(dataRow).getCell(p*17).setCellValue(codes.get(i));
				}
				for(int p=0;p<periods.size();p++){
					for(int j=0 ;j<coordDataList.size();j++){
						if(p%5==0){
							if(p==0){
								sheet.getRow(dataRow).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
							}else{
								if(j!=0){
									sheet.getRow(dataRow).getCell(p/5*17+1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
								}
							}
						}
						if((new SimpleDateFormat("yyyy.MM.dd").format(coordDataList.get(j).getCollectTime())).equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(dataRow).getCell(columnNum).setCellValue(coordDataList.get(j).getGapOffset());
							sheet.getRow(dataRow).getCell(columnNum+2).setCellValue(coordDataList.get(j).getGapChangeRate());
							break;
						}
					}
				}
			}
			//折线图所在区域的表头
			if(periods.size()!=0){
				if(!isDaily){
					creatLastHeader("ZC", sheet, pageCount);
				}
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			//数据表是否隐藏
//			workbook.setSheetHidden(workbook.getSheetIndex("data"), Workbook.SHEET_STATE_VERY_HIDDEN);
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			String fileName="支撑内力"+periods.get(periods.size()-1);
			excelOutPutter(out,fileName,project);
//			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//根据总共会有Y条数据来创建多少个表头，每页放x行数据就需要Y%X==0?y/x:y/x+1个表头
	//算下来，一页包括表头9行，数据x行，备注1行，页间隔1行，共11+x行
	private void createHeader(int dataEachPage,XSSFSheet sheet,XSSFSheet templateSheet,int pageCount,int columnCount,String pjName,String pjAddress,
			String deviceName,String specification,List<String> codes,List<String> periods,String header,String spType,String duShu,String leiJi,int fulu,String spCodeCharStr){
		//根据监测点数总共会竖下来分columnPageSize页
		int columnPageSize=codes.size()%dataEachPage==0?codes.size()/dataEachPage:codes.size()/dataEachPage+1;
		for(int columnPage=0;columnPage<columnPageSize;columnPage++){
			
			//表头起始行数
			int beginRowIndex=columnPage*(11+dataEachPage);
			sheet.createRow(beginRowIndex).setHeight(templateSheet.getRow(0).getHeight());
			//每一列先建立一个单元格
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				//设置每一列的宽度，使其每一页的内容适应其总宽度,设置一遍后下面不用再设置了
				sheet.setColumnWidth(i, templateSheet.getColumnWidth(index));
				if(sheet.getSheetName().equals("锚杆内力")||sheet.getSheetName().equals("支撑内力")){
					if(index==3||index==6||index==9||index==12||index==15){
						sheet.setColumnWidth(i, 0);
					}
				}
				XSSFCell cell=sheet.getRow(beginRowIndex).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(0).getCell(index).getCellStyle());
				cell.setCellValue(header);
				//合并
				if(index==0){
					cell.setCellValue("附件"+fulu);
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex, beginRowIndex, i, i+1));
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex, beginRowIndex, i+2, i+16));
				}
			}
			
			//处理第二行
			sheet.createRow(beginRowIndex+1).setHeight(templateSheet.getRow(1).getHeight());
			for (int i = 0; i < columnCount; i++) {
				XSSFCell cell=sheet.getRow(beginRowIndex+1).createCell(i);
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(1).getCell(index==12?2:index).getCellStyle());
				if(index==2){
					cell.setCellValue(pjName);
				}else if(index==11){
					cell.setCellValue(spType);
				}else{
					cell.setCellValue(templateSheet.getRow(1).getCell(index).getStringCellValue());
				}
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+1, beginRowIndex+1, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+1, beginRowIndex+1, i*17+2, i*17+2+6));
				if(sheet.getSheetName().equals("锚杆内力")||sheet.getSheetName().equals("支撑内力")){
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+1, beginRowIndex+1, i*17+9, i*17+11));
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+1, beginRowIndex+1, i*17+12, i*17+16));
					sheet.getRow(1).getCell(i*17+12).setCellValue(spType);
				}else{
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+1, beginRowIndex+1, i*17+9, i*17+9+1));
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+1, beginRowIndex+1, i*17+11, i*17+11+5));
				}
			}
			
			//处理第三行
			sheet.createRow(beginRowIndex+2).setHeight(templateSheet.getRow(2).getHeight());
			for (int i = 0; i < columnCount; i++) {
				XSSFCell cell=sheet.getRow(beginRowIndex+2).createCell(i);
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(2).getCell(index==12?2:index).getCellStyle());
				if(index==2){
					cell.setCellValue(pjAddress);
				}else if(index==11){
					cell.setCellValue(deviceName);
				}else{
					cell.setCellValue(templateSheet.getRow(2).getCell(index).getStringCellValue());
				}
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+2, beginRowIndex+2, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+2, beginRowIndex+2, i*17+2, i*17+2+6));
				if(sheet.getSheetName().equals("锚杆内力")||sheet.getSheetName().equals("支撑内力")){
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+2, beginRowIndex+2, i*17+9, i*17+11));
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+2, beginRowIndex+2, i*17+12, i*17+16));
					sheet.getRow(2).getCell(i*17+12).setCellValue(deviceName);
					}else{
						sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+2, beginRowIndex+2, i*17+9, i*17+9+1));
						sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+2, beginRowIndex+2, i*17+11, i*17+11+5));
					}
			}
			
			//处理第四行
			sheet.createRow(beginRowIndex+3).setHeight(templateSheet.getRow(3).getHeight());
			for (int i = 0; i < columnCount; i++) {
				XSSFCell cell=sheet.getRow(beginRowIndex+3).createCell(i);
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(3).getCell(index).getCellStyle());
				if(index==2){
					cell.setCellValue(specification);
				}else{
					cell.setCellValue(templateSheet.getRow(3).getCell(index).getStringCellValue());
				}
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+3, beginRowIndex+3, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+3, beginRowIndex+3, i*17+2, i*17+2+14));
			}
			
			//处理第6~9行，第5行是分割行
			sheet.createRow(beginRowIndex+4).setHeight(templateSheet.getRow(4).getHeight());
			sheet.createRow(beginRowIndex+5).setHeight(templateSheet.getRow(5).getHeight());
			sheet.createRow(beginRowIndex+6).setHeight(templateSheet.getRow(6).getHeight());
			sheet.createRow(beginRowIndex+7).setHeight(templateSheet.getRow(7).getHeight());
			sheet.createRow(beginRowIndex+8).setHeight(templateSheet.getRow(8).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(beginRowIndex+5).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(5).getCell(index).getCellStyle());
				XSSFCell cell1=sheet.getRow(beginRowIndex+6).createCell(i);
				cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell1.setCellStyle(templateSheet.getRow(6).getCell(index).getCellStyle());
				XSSFCell cell2=sheet.getRow(beginRowIndex+7).createCell(i);
				cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell2.setCellStyle(templateSheet.getRow(7).getCell(index).getCellStyle());
				if(i<(periods.size()/5)*17+(periods.size()%5*3==0?0:periods.size()%5*3+2)){
					cell2.setCellValue(templateSheet.getRow(7).getCell(index).getStringCellValue());
				}
				XSSFCell cell3=sheet.getRow(beginRowIndex+8).createCell(i);
				cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell3.setCellStyle(templateSheet.getRow(8).getCell(index).getCellStyle());
				if(index==1){
					if(i==1){
						cell.setCellValue(duShu);
					}else{
						cell.setCellValue(leiJi);
					}
				}
			}
			
			//声明一个画图的顶级管理器，用于画折线
			XSSFDrawing draw=sheet.createDrawingPatriarch();
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+5, beginRowIndex+6, i*17, i*17));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+7, beginRowIndex+8, i*17, i*17));
				//画折线
				XSSFClientAnchor anchor=draw.createAnchor(0, 0, 0, 0, i*17, beginRowIndex+5, i*17+1, beginRowIndex+9);
				XSSFSimpleShape shape=draw.createSimpleShape(anchor);
				shape.setShapeType(ShapeTypes.LINE);
				shape.setLineWidth(1d);
				shape.setLineStyleColor(0, 0, 0);
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+5, beginRowIndex+8, i*17+1, i*17+1));
				//周期,一页显示5个周期，周期是按照顺序递增的
				sheet.getRow(beginRowIndex+5).getCell(i*17).setCellValue("时间");
				sheet.getRow(beginRowIndex+5).getCell(i*17+2).setCellValue(i*5+1>periods.size()+1?"":i*5+1==periods.size()+1?"以下空白":"第"+(i*5+1)+"次");
				sheet.getRow(beginRowIndex+5).getCell(i*17+5).setCellValue(i*5+2>periods.size()+1?"":i*5+2==periods.size()+1?"以下空白":"第"+(i*5+2)+"次");
				sheet.getRow(beginRowIndex+5).getCell(i*17+8).setCellValue(i*5+3>periods.size()+1?"":i*5+3==periods.size()+1?"以下空白":"第"+(i*5+3)+"次");
				sheet.getRow(beginRowIndex+5).getCell(i*17+11).setCellValue(i*5+4>periods.size()+1?"":i*5+4==periods.size()+1?"以下空白":"第"+(i*5+4)+"次");
				sheet.getRow(beginRowIndex+5).getCell(i*17+14).setCellValue(i*5+5>periods.size()+1?"":i*5+5==periods.size()+1?"以下空白":"第"+(i*5+5)+"次");
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+5, beginRowIndex+5, i*17+2, i*17+2+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+5, beginRowIndex+5, i*17+5, i*17+5+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+5, beginRowIndex+5, i*17+8, i*17+8+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+5, beginRowIndex+5, i*17+11, i*17+11+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+5, beginRowIndex+5, i*17+14, i*17+14+2));
				//时间
				
//				String[] times=dataMap.get(type).get(0).getTimes();
				sheet.getRow(beginRowIndex+6).getCell(i*17+2).setCellValue((i*5)>=periods.size()?"": periods.get(i*5));
				sheet.getRow(beginRowIndex+6).getCell(i*17+5).setCellValue((i*5+1)>=periods.size()?"":periods.get(i*5+1));
				sheet.getRow(beginRowIndex+6).getCell(i*17+8).setCellValue((i*5+2)>=periods.size()?"":periods.get(i*5+2));
				sheet.getRow(beginRowIndex+6).getCell(i*17+11).setCellValue((i*5+3)>=periods.size()?"":periods.get(i*5+3));
				sheet.getRow(beginRowIndex+6).getCell(i*17+14).setCellValue((i*5+4)>=periods.size()?"":periods.get(i*5+4));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+6, beginRowIndex+6, i*17+2, i*17+2+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+6, beginRowIndex+6, i*17+5, i*17+5+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+6, beginRowIndex+6, i*17+8, i*17+8+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+6, beginRowIndex+6, i*17+11, i*17+11+2));
				sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+6, beginRowIndex+6, i*17+14, i*17+14+2));
				//合并数据头部，除了前两列，后面的15列都要合并
				for (int j = 0; j < 15; j++) {
					sheet.addMergedRegion(new CellRangeAddress(beginRowIndex+7, beginRowIndex+8, i*17+j+2, i*17+j+2));
				}
			}
			
			//创建数据的行
			for(int i=0;i<dataEachPage;i++){
				XSSFRow row=sheet.createRow(9+i+columnPage*(11+dataEachPage));
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(templateSheet.getRow(5).getCell(1).getCellStyle());
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
			//最后一页数据不满时的处理--以下空白
			if(columnPage==columnPageSize-1){
				if(codes.size()%dataEachPage>0){
					XSSFRow row=sheet.getRow(9+codes.size()%dataEachPage+(11+dataEachPage)*columnPage);
					for(int i=0;i<pageCount;i++){
						row.getCell(0+i*17).setCellValue("以");
						row.getCell(1+i*17).setCellValue("下");
						row.getCell(2+i*17).setCellValue("空");
						if(sheet.getSheetName().equals("锚杆内力")||sheet.getSheetName().equals("支撑内力")){
							row.getCell(4+i*17).setCellValue("白");
						}else{
							row.getCell(3+i*17).setCellValue("白");
						}
					}
				}
			}
			
			//备注所在的行数
			int remarkRow=9+dataEachPage+columnPage*(11+dataEachPage);
			sheet.createRow(remarkRow).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(remarkRow).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue((templateSheet.getRow(9).getCell(index).getStringCellValue()).replace("codeChar", spCodeCharStr));
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(remarkRow, remarkRow, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(remarkRow, remarkRow, i*17+2, i*17+2+14));
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void exportCXDataToExcel(boolean isDaily, String templatePath, String pjName, String pjAddress,
			String deviceName, Project project, String specification, String startTime, String endTime,int fulu) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, List<CX_Data>> dataMap=new HashMap<String,List<CX_Data>>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "CX");
			dataMap=(Map<String, List<CX_Data>>) map.get("CX");
//			if(dataMap==null){
//				return null;
//			}
			//取出所有时间以生成所有列、取出所有监测点编号
			List<String> codes=new ArrayList<String>();
			for (Map.Entry<String, List<CX_Data>> m : dataMap.entrySet()) {
				codes.add(m.getKey());
			}
			//给编号排序、便于按测斜编号建立sheet
			 String codeChar="";
			if(codes.size()>0&&dataMap.get(codes.get(0)).size()>0){
				codeChar=codeChar+dataMap.get(codes.get(0)).get(0).getSurveyPoint().getCodeChar();
			}
			final String cC=codeChar;
			if(!codeChar.equals("")){
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int number1=Integer.parseInt(c1.replace(cC, ""));
	    				int number2=Integer.parseInt(c2.replace(cC, ""));
	                    return (number1 == number2 ? 0 : (number1 > number2 ? 1 : -1));
					}
				});
			}
			List<String> periods=new ArrayList<String>();
			//判断是否日报，根据开始日期取得一周或当天的时间字符串list
			getWeekDateList(periods,startTime,isDaily);
			for (int n = 0; n < codes.size(); n++) {
				XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templatePath)));
				// 找到模板sheet
				XSSFSheet templateSheet = null;
				XSSFSheet sheet = null;
				XSSFSheet dataSheet = null;
				Iterator<Sheet> sheetIterator = workbook.sheetIterator();
				while (sheetIterator.hasNext()) {
					XSSFSheet tempSheet = (XSSFSheet) sheetIterator.next();
					if (tempSheet.getSheetName().equals("cxTemplate")) {
						templateSheet = tempSheet;
					}
					if (tempSheet.getSheetName().equals("grid")) {
						sheet = tempSheet;
					}
					if (tempSheet.getSheetName().equals("图的数据源")) {
						dataSheet = tempSheet;
					}
				}
				List<CX_Data> cxDataList = dataMap.get(codes.get(n));
				Map<String, List<CX_Data>> timeDataMap =new HashMap<String, List<CX_Data>>();
				//处理一下原始数据
				List<String> dataPeriods=new ArrayList<String>();
				for (int i = 0; i < cxDataList.size(); i++) {
					if (!(dataPeriods.contains(new SimpleDateFormat("yyyy.MM.dd").format(cxDataList.get(i).getCollectTime())))) {
						dataPeriods.add(new SimpleDateFormat("yyyy.MM.dd").format(cxDataList.get(i).getCollectTime()));
						timeDataMap.put(new SimpleDateFormat("yyyy.MM.dd").format(cxDataList.get(i).getCollectTime()), new ArrayList<CX_Data>());
					}
					timeDataMap.get(new SimpleDateFormat("yyyy.MM.dd").format(cxDataList.get(i).getCollectTime())).add(cxDataList.get(i));
				}
				//将sheet的名字改为点号
				workbook.setSheetName(workbook.getSheetIndex("grid"), codes.get(n));
				//往折线图的数据源表dataSheet里填数据，多了一行表头
				//深度/0.5即为数据总行数,*2后必然是整数
				int dataRowCount=0;
				if(dataPeriods.size()>0&&timeDataMap.get(dataPeriods.get(0)).size()>0){
					dataRowCount=(int) ((timeDataMap.get(dataPeriods.get(0)).get(0).getSurveyPoint().getDeep())*2);
				}
				//数据源表所需要的单元格和页首的一行一列,填数据
				for (int i = 0; i < dataRowCount+1; i++) {
					XSSFRow row=dataSheet.createRow(i);
					for (int j = 0; j < periods.size()+1; j++) {
						XSSFCell cell=row.createCell(j);
						if(i==0){
							if(j>0){
								cell.setCellValue(periods.get(j-1));
							}
						}else{
							if(j==0){
								cell.setCellValue(-0.5*i);
							}else{
								List<CX_Data> cxdataList = timeDataMap.get(periods.get(j-1));
								if(cxdataList!=null){
									for(int t=0;t<cxdataList.size();t++){
										if(cxdataList.get(t).getDepth()==0.5*i){
											cell.setCellValue(cxdataList.get(t).getAccumOffset());
										}
									}
								}
							}
						}
					}
				}
				
				//一共多少个时间段，即周期，即页数
				int pageCount=periods.size()%4==0?periods.size()/4:periods.size()/4+1;
				pageCount=pageCount==0?1:pageCount;
				//每一页显示一个周期，每页占用12列
				int columnCount=pageCount*12;
				//处理第一行
				sheet.createRow(0).setHeight(templateSheet.getRow(0).getHeight());
				//每一列先建立一个单元格
				for (int i = 0; i < columnCount; i++) {
					int index=i%12;
					//设置每一列的宽度，使其每一页的内容适应其总宽度,设置一遍后下面不用再设置了
					sheet.setColumnWidth(i, templateSheet.getColumnWidth(index));
					XSSFCell cell=sheet.getRow(0).createCell(i);
					//12列为一页，取余数，每一页对应的列所在的索引
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(0).getCell(index).getCellStyle());
					String tempValue="支护结构深层水平位移监测结果表";
					cell.setCellValue(tempValue);
					//合并
					if(index==0){
						cell.setCellValue("附件"+fulu);
						sheet.addMergedRegion(new CellRangeAddress(0, 0, i, i+1));
						sheet.addMergedRegion(new CellRangeAddress(0, 0, i+2, i+11));
					}
				}
				//处理第二行
				sheet.createRow(1).setHeight(templateSheet.getRow(1).getHeight());
				for (int i = 0; i < columnCount; i++) {
					XSSFCell cell=sheet.getRow(1).createCell(i);
					//12列为一页，取余数，即第一页对应的列所在的索引
					int index=i%12;
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(1).getCell(index).getCellStyle());
					if(index==2){
						cell.setCellValue(pjName);
					}else if(index==9){
						cell.setCellValue("支护结构深层水平位移");
					}else{
						cell.setCellValue(templateSheet.getRow(1).getCell(index).getStringCellValue());
					}
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(1, 1, i*12, i*12+1));
					sheet.addMergedRegion(new CellRangeAddress(1, 1, i*12+2, i*12+6));
					sheet.addMergedRegion(new CellRangeAddress(1, 1, i*12+7, i*12+8));
					sheet.addMergedRegion(new CellRangeAddress(1, 1, i*12+9, i*12+11));
				}
				
				//处理第三行
				sheet.createRow(2).setHeight(templateSheet.getRow(2).getHeight());
				for (int i = 0; i < columnCount; i++) {
					XSSFCell cell=sheet.getRow(2).createCell(i);
					//12列为一页，取余数，即第一页对应的列所在的索引
					int index=i%12;
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(2).getCell(index).getCellStyle());
					if(index==2){
						cell.setCellValue(pjAddress);
					}else if(index==9){
						cell.setCellValue(deviceName);
					}else{
						cell.setCellValue(templateSheet.getRow(2).getCell(index).getStringCellValue());
					}
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(2, 2, i*12, i*12+1));
					sheet.addMergedRegion(new CellRangeAddress(2, 2, i*12+2, i*12+6));
					sheet.addMergedRegion(new CellRangeAddress(2, 2, i*12+7, i*12+8));
					sheet.addMergedRegion(new CellRangeAddress(2, 2, i*12+9, i*12+11));
				}
				//处理第四行
				sheet.createRow(3).setHeight(templateSheet.getRow(3).getHeight());
				for (int i = 0; i < columnCount; i++) {
					XSSFCell cell=sheet.getRow(3).createCell(i);
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%12;
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(3).getCell(index).getCellStyle());
					if(index==2){
						cell.setCellValue(specification);
					}else{
						cell.setCellValue(templateSheet.getRow(3).getCell(index).getStringCellValue());
					}
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(3, 3, i*12, i*12+1));
					sheet.addMergedRegion(new CellRangeAddress(3, 3, i*12+2, i*12+11));
				}
				//处理第6~8行，第5行是分割行
				sheet.createRow(4).setHeight(templateSheet.getRow(4).getHeight());
				sheet.createRow(5).setHeight(templateSheet.getRow(5).getHeight());
				sheet.createRow(6).setHeight(templateSheet.getRow(6).getHeight());
				sheet.createRow(7).setHeight(templateSheet.getRow(7).getHeight());
				sheet.createRow(8).setHeight(templateSheet.getRow(8).getHeight());
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%12;
					XSSFCell cell=sheet.getRow(5).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(5).getCell(index).getCellStyle());
					XSSFCell cell1=sheet.getRow(6).createCell(i);
					cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell1.setCellStyle(templateSheet.getRow(6).getCell(index).getCellStyle());
					XSSFCell cell2=sheet.getRow(7).createCell(i);
					cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell2.setCellStyle(templateSheet.getRow(7).getCell(index).getCellStyle());
//					cell2.setCellValue(templateSheet.getRow(7).getCell(index).getStringCellValue());
					if(index==0){
						cell.setCellValue("点号");
					}
					if(index==1){
						cell.setCellValue("深度(m)");
					}
					
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(5, 7, i*12, i*12));
					sheet.addMergedRegion(new CellRangeAddress(5, 7, i*12+1, i*12+1));
					//一页显示四条数据，每页的最后一条数据要显示三项的数据
					if(i<pageCount-1){
						sheet.getRow(5).getCell(i*12+2).setCellValue("第"+(i*4+1)+"次");
						sheet.getRow(5).getCell(i*12+3).setCellValue("第"+(i*4+2)+"次");
						sheet.getRow(5).getCell(i*12+4).setCellValue("第"+(i*4+3)+"次");
						sheet.getRow(5).getCell(i*12+5).setCellValue("第"+(i*4+4)+"次");
						
						sheet.getRow(6).getCell(i*12+2).setCellValue("累积位移(mm)");
						sheet.getRow(6).getCell(i*12+3).setCellValue("累积位移(mm)");
						sheet.getRow(6).getCell(i*12+4).setCellValue("累积位移(mm)");
						sheet.getRow(6).getCell(i*12+5).setCellValue("累积位移(mm)");
						sheet.getRow(6).getCell(i*12+6).setCellValue("本次位移(mm)");
						sheet.getRow(6).getCell(i*12+7).setCellValue("本次位移速率(mm/d)");
						sheet.getRow(7).getCell(i*12+2).setCellValue(periods.size()>0?periods.get(i*4+0):"");
						sheet.getRow(7).getCell(i*12+3).setCellValue(periods.size()>0?periods.get(i*4+1):"");
						sheet.getRow(7).getCell(i*12+4).setCellValue(periods.size()>0?periods.get(i*4+2):"");
						sheet.getRow(7).getCell(i*12+5).setCellValue(periods.size()>0?periods.get(i*4+3):"");
						sheet.addMergedRegion(new CellRangeAddress(5, 5, i*12+5, i*12+7));
						sheet.addMergedRegion(new CellRangeAddress(7, 7, i*12+5, i*12+7));
					}else{
						int num=(periods.size()%4==0&&periods.size()>0)?4:periods.size()%4;
						for(int col=0;col<num;col++){
							sheet.getRow(5).getCell(i*12+2+col).setCellValue("第"+(i*4+col+1)+"次");
							sheet.getRow(6).getCell(i*12+2+col).setCellValue("累积位移(mm)");
							sheet.getRow(7).getCell(i*12+2+col).setCellValue(periods.size()>0?periods.get(i*4+col):"");
							if(col==num-1){
								sheet.getRow(6).getCell(i*12+3+col).setCellValue("本次位移(mm)");
								sheet.getRow(6).getCell(i*12+4+col).setCellValue("本次位移速率(mm/d)");
								sheet.addMergedRegion(new CellRangeAddress(5, 5, i*12+2+col, i*12+4+col));
								sheet.addMergedRegion(new CellRangeAddress(7, 7, i*12+2+col, i*12+4+col));
								
							}
						}
						sheet.getRow(5).getCell(i*12+2+num+2).setCellValue("以下空白");
					}
				}
				
				//创建显示全部数据所需要的单元格和页尾的一行,默认20m的深度为表格高度，即会有40行数据
				int createRow=0;
				if(dataRowCount<40){
					createRow=41;
				}else{
					createRow=dataRowCount+1;
				}
				for (int i = 0; i < createRow; i++) {
					XSSFRow row=sheet.createRow(8+i);
					//以第6行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(templateSheet.getRow(8).getCell(2).getCellStyle());
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						if(i<dataRowCount){
							if((j-1)%12==0){
								//循环增加深度、、、测斜点号只需要在第一行设置值就行
								cell.setCellValue((i+1)*-0.5);
								if(i==0){
									sheet.getRow(8+i).getCell(j-1).setCellValue(codes.get(n));
								}
							}
						}
					}
				}
				
				for (int p = 0; p < pageCount; p++) {
					if (dataRowCount < 40) {
						sheet.getRow(dataRowCount + 8).getCell(12 * p + 1).setCellValue("以");
						sheet.getRow(dataRowCount + 8).getCell(12 * p + 2).setCellValue("下");
						sheet.getRow(dataRowCount + 8).getCell(12 * p + 3).setCellValue("空");
						sheet.getRow(dataRowCount + 8).getCell(12 * p + 4).setCellValue("白");
					}
					sheet.getRow(createRow + 7).getCell(p * 12).setCellValue("备注");
					sheet.getRow(createRow + 7).getCell(p * 12 + 2).setCellValue("1、“+”表示向基坑内位移,“-”表示向基坑外位移。");
					sheet.addMergedRegion(new CellRangeAddress(7 + createRow, 7 + createRow, p * 12, p * 12 + 1));
					sheet.addMergedRegion(new CellRangeAddress(7 + createRow, 7 + createRow, p * 12 + 2, p * 12 + 11));
					if (dataRowCount > 0) {
						sheet.addMergedRegion(new CellRangeAddress(8, 7 + createRow - 2, p * 12, p * 12));
					}
				}
				
				//根据每页的行数插入数据
				for (int i = 0; i < periods.size(); i++) {
					List<CX_Data> cxdataList = timeDataMap.get(periods.get(i));
					if(cxdataList!=null){
						int index=i%4;
						//判断是否最后一项或者是每页的最后一项
						if(index==3||i==periods.size()-1){
							for(int row=0;row<dataRowCount;row++){
								for(int j=0;j<cxdataList.size();j++){
									if(cxdataList.get(j).getDepth()==((row+1)*0.5)){
										sheet.getRow(8+row).getCell((i/4)*12+2+index).setCellValue(cxdataList.get(j).getAccumOffset());
										sheet.getRow(8+row).getCell((i/4)*12+3+index).setCellValue(cxdataList.get(j).getGapOffset());
										sheet.getRow(8+row).getCell((i/4)*12+4+index).setCellValue(cxdataList.get(j).getChangeRate());
										break;
									}
								}
							}
						}else{
							for(int row=0;row<dataRowCount;row++){
								for(int j=0;j<cxdataList.size();j++){
									if(cxdataList.get(j).getDepth()==((row+1)*0.5)){
										sheet.getRow(8+row).getCell((i/4)*12+2+index).setCellValue(cxdataList.get(j).getAccumOffset());
										break;
									}
								}
							}
						}
					}
				}
				//导出之前需要将模板的sheet删掉
				if(dataMap.size()!=0){
					workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
				}
				ByteArrayOutputStream out=new ByteArrayOutputStream();
				workbook.write(out);
				out.close();
				workbook.close();
				String fileName=codes.get(n)+"支护结构深层水平位移监测结果"+periods.get(periods.size()-1);
				excelOutPutter(out,fileName,project);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		return null;
	}
	
	private void creatLastHeader(String chartType,XSSFSheet sheet,int pageCount){
		//创建展示关系曲线图的一页
		for(int i=0;i<5;i++){
			for(int j=0;j<17;j++){
				sheet.setColumnWidth(pageCount*17+j, 1250);
				if(j==1||j==9){
					sheet.setColumnWidth(pageCount*17+j, 1250);
				}
				XSSFCell cell=(XSSFCell)
				sheet.getRow(i).createCell(pageCount*17+j);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(i<4){
					cell.setCellStyle(sheet.getRow(i).getCell(j).getCellStyle());
					if(j==11){cell.setCellStyle(sheet.getRow(i).getCell(2).getCellStyle());}
					cell.setCellValue(sheet.getRow(i).getCell(j).getStringCellValue()==null?"":sheet.getRow(i).getCell(j).getStringCellValue());
				}
			}
			sheet.addMergedRegion(new CellRangeAddress(i, i, pageCount*17, pageCount*17+1));
		}
		if(chartType.equals("WYS")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("顶部水平位移监测结果表曲线");
		}else if(chartType.equals("WYD")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("顶部竖向位移监测结果表曲线");
		}else if(chartType.equals("LZ")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("立柱竖向位移监测结果表曲线");
		}else if(chartType.equals("SM")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("周边建筑竖向位移监测结果表曲线");
		}else if(chartType.equals("SW")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("水位监测结果表曲线");
		}else if(chartType.equals("ZGD")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("周边管线竖向位移监测结果表曲线");
		}else if(chartType.equals("MT")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("锚索内力监测结果表曲线");
		}else if(chartType.equals("ZC")){
			sheet.getRow(0).getCell(pageCount*17+2).setCellValue("支撑内力监测结果表曲线");
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, pageCount*17+2, pageCount*17+16));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, pageCount*17+2, pageCount*17+8));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, pageCount*17+9, pageCount*17+10));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, pageCount*17+11, pageCount*17+16));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, pageCount*17+2, pageCount*17+8));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, pageCount*17+9, pageCount*17+10));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, pageCount*17+11, pageCount*17+16));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, pageCount*17+2, pageCount*17+16));
		
	}
	
	private void getWeekDateList(List<String> datelist,String startTime,boolean isDaily){
		Calendar c = Calendar.getInstance();  
		try {
			if(isDaily){
				c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
				Date anatherDay = c.getTime();
				datelist.add(new SimpleDateFormat("yyyy.MM.dd").format(anatherDay));
			}else{
				for (int i = 0; i < 7; i++) {
						c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
						c.add(Calendar.DAY_OF_MONTH, i);// +1天
						Date anatherDay = c.getTime();
						datelist.add(new SimpleDateFormat("yyyy.MM.dd").format(anatherDay));
					} 
				}
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private void excelOutPutter(ByteArrayOutputStream out,String fileName,Project project){
		try {
			byte[] b = out.toByteArray();
			File directory=new File(uploadFileSrc+"/dataReport/"+project.getProjectUuid());
			if(!(directory.exists())){
				directory.mkdirs();
			}
//			else{
//				deleteAllFilesOfDir(directory);
//				directory.mkdirs();
//			}
			String path=directory+"/"+fileName+".xlsm";
			File outputFile=new File(path);
			FileOutputStream  fop = new FileOutputStream(outputFile);
			fop.write(b);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		File directory = new File(zipFilePath);
		if (!(directory.exists())) {
			directory.mkdirs();
		} else {
			deleteAllFilesOfDir(directory);
			directory.mkdirs();
		}
		if (sourceFile.exists() == false) {
			System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// 创建ZIP实体，并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
					flag = true;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}

	

	
}
