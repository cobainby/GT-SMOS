package com.southgt.smosplat.project.service.impl;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.ShapeTypes;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.charts.AxisCrossBetween;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.AxisTickMark;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ChartLegend;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.LineChartSeries;
import org.apache.poi.ss.usermodel.charts.ScatterChartData;
import org.apache.poi.ss.usermodel.charts.ScatterChartSeries;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.southgt.smosplat.project.service.IExportDataService;
import com.southgt.smosplat.project.service.IProjectMonitorItemService;
import com.southgt.smosplat.project.service.IReportService;

@Service("exportDataService")
public class ExportDataServiceImpl implements IExportDataService {

	@Resource
	IReportService reportService;
	
	@Resource
	IProjectMonitorItemService projectMonitorItemService;
	
	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportWYSDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "WYS");
			dataMap=(Map<String, Object>) map.get("WYS");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<>();
			
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<WYS_CoordData> coordDataList = (List<WYS_CoordData>) dMap.get("data");
				for (int i = 0; i < coordDataList.size(); i++) {
					if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
						periods.add(coordDataList.get(i).getSurveyTime());
					}
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
			
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}
			XSSFSheet sheet=workbook.createSheet("围护墙(边坡)顶部水平位移");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			if(pageCount==0){pageCount=1;};
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "围护墙(边坡)顶部水平位移监测结果表", "围护墙(边坡)顶部水平位移监测", "初始读数(m)", "上次累计(mm)",1,spCodeCharStr);
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
				
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<WYS_CoordData> coordDataList = (List<WYS_CoordData>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(coordDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<coordDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填每页页首的测点编号、初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalE")==null? "":dMap.get("originalE").toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumEOffset());
								}
							}
						}
						if(coordDataList.get(j).getSurveyTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(coordDataList.get(j).getGapEOffset());
							sheet.getRow(9+i).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumEOffset());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(coordDataList.get(j).getChangeRate());
							break;
						}
					}
				}
			}
			//先移动行数据
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "围护墙(边坡)顶部水平位移监测结果表", "围护墙(边坡)顶部水平位移监测", "初始读数(m)", "上次累计(mm)",1,spCodeCharStr);
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			
			//每隔行数增加表头,表头有
//			for(i=0;i<){}
			//创建最后一页
			//导出之前需要将模板的sheet删掉
			if(periods.size()!=0){
//				creatLineChart( "WYS", sheet, pageCount, periods, codes, dataMap);
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportWYDDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "WYD");
			dataMap=(Map<String, Object>) map.get("WYD");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<String>();
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<WYD_LevelData> wydDataList = (List<WYD_LevelData>) dMap.get("data");
				for (int i = 0; i < wydDataList.size(); i++) {
					if (!(periods.contains(wydDataList.get(i).getSurveyTime()))) {
						periods.add(wydDataList.get(i).getSurveyTime());
					}
					if (!(spCodeChar.contains(wydDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(wydDataList.get(i).getSurveyPoint().getCodeChar());
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
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给测点编号排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}

			XSSFSheet sheet=workbook.createSheet("围护墙(边坡)顶部竖向位移");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			//无数据的时候不能导出空白页，要有数据头
			if(pageCount==0){pageCount=1;}
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "围护墙(边坡)顶部竖向位移监测结果表", "围护墙(边坡)顶部竖向位移监测", "初始读数(m)", "上次累计(mm)",2,spCodeCharStr);
			
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<WYD_LevelData> wydDataList = (List<WYD_LevelData>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(wydDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<wydDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填每页页首的测点编号、初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(wydDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if(wydDataList.get(j).getSurveyTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(wydDataList.get(j).getGapHOffset());
							sheet.getRow(9+i).getCell(columnNum+1).setCellValue(wydDataList.get(j).getAccumHOffset());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(wydDataList.get(j).getGapHChangeRate());
							
							break;
						}
					}
				}
			}
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "围护墙(边坡)顶部竖向位移监测结果表", "围护墙(边坡)顶部竖向位移监测", "初始读数(m)", "上次累计(mm)",2,spCodeCharStr);
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			
			//导出之前需要将模板的sheet删掉
			if(periods.size()!=0){
				//创建展示折线图的最后一页并画出折线图
//				creatLineChart("WYD", sheet, pageCount, periods, codes, dataMap);
			}
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportLZDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "LZ");
			dataMap=(Map<String, Object>) map.get("LZ");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<String>();
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<LZ_Data> lzDataList = (List<LZ_Data>) dMap.get("data");
				for (int i = 0; i < lzDataList.size(); i++) {
					if (!(periods.contains(lzDataList.get(i).getSurveyTime()))) {
						periods.add(lzDataList.get(i).getSurveyTime());
					}
					if (!(spCodeChar.contains(lzDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(lzDataList.get(i).getSurveyPoint().getCodeChar());
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
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给周期排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}
			
			XSSFSheet sheet=workbook.createSheet("立柱竖向位移");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			//无数据的时候不能导出空白页，要有数据头
			if(pageCount==0){pageCount=1;}
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "立柱竖向位移监测结果表", "立柱竖向位移监测", "初始读数(m)", "上次累计(mm)",3,spCodeCharStr);
			
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<LZ_Data> lzDataList = (List<LZ_Data>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(lzDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<lzDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填每页页首的测点编号、初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(lzDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if(lzDataList.get(j).getSurveyTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(lzDataList.get(j).getGapHOffset());
							sheet.getRow(9+i).getCell(columnNum+1).setCellValue(lzDataList.get(j).getAccumHOffset());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(lzDataList.get(j).getGapHOffsetChangeRate());
							
							break;
						}
					}
				}
			}
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "立柱竖向位移监测结果表", "立柱竖向位移监测", "初始读数(m)", "上次累计(mm)",3,spCodeCharStr);
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			if(periods.size()!=0){
				//创建展示折线图的最后一页并画出折线图
//				creatLineChart("LZ", sheet, pageCount, periods, codes, dataMap);
			}
			//导出之前需要将模板的sheet删掉
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportSMDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "SM");
			dataMap=(Map<String, Object>) map.get("SM");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<String>();
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<SM_Data> smDataList = (List<SM_Data>) dMap.get("data");
				for (int i = 0; i < smDataList.size(); i++) {
					if (!(periods.contains(smDataList.get(i).getSurveyTime()))) {
						periods.add(smDataList.get(i).getSurveyTime());
					}
					if (!(spCodeChar.contains(smDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(smDataList.get(i).getSurveyPoint().getCodeChar());
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
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给周期排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}
			
			XSSFSheet sheet=workbook.createSheet("周边建筑物竖向位移");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			//无数据的时候不能导出空白页，要有数据头
			if(pageCount==0){pageCount=1;}
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "周边建筑物竖向位移监测结果表", "周边建筑物竖向位移监测", "初始读数(m)", "上次累计(mm)",4,spCodeCharStr);
			
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<SM_Data> smDataList = (List<SM_Data>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(smDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<smDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填每页页首的测点编号、初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(smDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if(smDataList.get(j).getSurveyTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(smDataList.get(j).getGapHOffset());
							sheet.getRow(9+i).getCell(columnNum+1).setCellValue(smDataList.get(j).getAccumHOffset());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(smDataList.get(j).getGapHOffsetChangeRate());
							
							break;
						}
					}
				}
			}
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "周边建筑物竖向位移监测结果表", "周边建筑物竖向位移监测", "初始读数(m)", "上次累计(mm)",4,spCodeCharStr);
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			if(periods.size()!=0){
				//创建展示折线图的最后一页并画出折线图
//				creatLineChart("SM", sheet, pageCount, periods, codes, dataMap);
			}
			//导出之前需要将模板的sheet删掉
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportZGDDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "ZGD");
			dataMap=(Map<String, Object>) map.get("ZGD");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<String>();
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<ZGD_Data> zgdDataList = (List<ZGD_Data>) dMap.get("data");
				for (int i = 0; i < zgdDataList.size(); i++) {
					if (!(periods.contains(zgdDataList.get(i).getSurveyTime()))) {
						periods.add(zgdDataList.get(i).getSurveyTime());
					}
					if (!(spCodeChar.contains(zgdDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(zgdDataList.get(i).getSurveyPoint().getCodeChar());
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
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给周期排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}
			XSSFSheet sheet=workbook.createSheet("周边管线竖向位移");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			//无数据的时候不能导出空白页，要有数据头
			if(pageCount==0){pageCount=1;}
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "周边管线竖向位移监测结果表", "周边管线竖向位移监测", "初始读数(m)", "上次累计(mm)",5,spCodeCharStr);
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<ZGD_Data> zgdDataList = (List<ZGD_Data>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(zgdDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<zgdDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填每页页首的测点编号、初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(zgdDataList.get(j-1).getAccumHOffset());
								}
							}
						}
						if(zgdDataList.get(j).getSurveyTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(zgdDataList.get(j).getGapHOffset());
							sheet.getRow(9+i).getCell(columnNum+1).setCellValue(zgdDataList.get(j).getAccumHOffset());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(zgdDataList.get(j).getGapHChangeRate());
							
							break;
						}
					}
				}
			}
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "周边管线竖向位移监测结果表", "周边管线竖向位移监测", "初始读数(m)", "上次累计(mm)",5,spCodeCharStr);
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			if(periods.size()!=0){
				//创建展示折线图的最后一页并画出折线图
//				creatLineChart("ZGD", sheet, pageCount, periods, codes, dataMap);
			}
			//导出之前需要将模板的sheet删掉
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportSWDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "SW");
			dataMap=(Map<String, Object>) map.get("SW");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			List<String> spCodeChar=new ArrayList<String>();
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<SW_Data> swDataList = (List<SW_Data>) dMap.get("data");
				for (int i = 0; i < swDataList.size(); i++) {
					if (!(periods.contains(swDataList.get(i).getCollectTime()))) {
						periods.add(swDataList.get(i).getCollectTime());
					}
					if (!(spCodeChar.contains(swDataList.get(i).getSurveyPoint().getCodeChar()))) {
						spCodeChar.add(swDataList.get(i).getSurveyPoint().getCodeChar());
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
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给周期排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}
			XSSFSheet sheet=workbook.createSheet("地下水位");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			//无数据的时候不能导出空白页，要有数据头
			if(pageCount==0){pageCount=1;}
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "地下水位监测结果表", "地下水位移监测", "初始读数(m)", "上次累计(mm)",6,spCodeCharStr);
			
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
				
			
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<SW_Data> swDataList = (List<SW_Data>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(swDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<swDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						//先填每页页首的测点编号、初始读数或上次累计值
						pFlag+=1;
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(swDataList.get(j-1).getAccumOffset());
									System.out.println((p/5*17+1)+"时间："+swDataList.get(j-1).getCollectTime().toString()+"累计值："+swDataList.get(j-1).getAccumOffset());
								}
							}
						}
						if(swDataList.get(j).getCollectTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(swDataList.get(j).getGapOffset());
							sheet.getRow(9+i).getCell(columnNum+1).setCellValue(swDataList.get(j).getAccumOffset());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(swDataList.get(j).getGapChangeRate());
							break;
						}
					}
				}
			}
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "地下水位监测结果表", "地下水位监测", "初始读数(m)", "上次累计(mm)",6,spCodeCharStr);
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			if(periods.size()!=0){
				//创建展示折线图的最后一页并画出折线图
//				creatLineChart("SW", sheet, pageCount, periods, codes, dataMap);
			}
			//导出之前需要将模板的sheet删掉
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportMTDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "MT");
			dataMap=(Map<String, Object>) map.get("MT");
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<MT_Data> mtDataList = (List<MT_Data>) dMap.get("data");
				for (int i = 0; i < mtDataList.size(); i++) {
					if (!(periods.contains(mtDataList.get(i).getCollectTime()))) {
						periods.add(mtDataList.get(i).getCollectTime());
					}
				}
			}
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给周期排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}
			
			XSSFSheet sheet=workbook.createSheet("锚杆内力");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			//无数据的时候不能导出空白页，要有数据头
			if(pageCount==0){pageCount=1;}
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "锚杆内力监测结果表", "锚杆内力监测", "初始读数(kN)", "初始读数(kN)",7,"");
			
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue());
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					int ji=j%17;
					if(ji==3||ji==6||ji==9||ji==12||ji==15){
						sheet.addMergedRegion(new CellRangeAddress(9+i, 9+i, j-1, j));
					}
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<MT_Data> mtDataList = (List<MT_Data>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(mtDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<mtDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填每页页首的测点编号、初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
								}
							}
						}
						if(mtDataList.get(j).getCollectTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(mtDataList.get(j).getCalValue());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(mtDataList.get(j).getGapChangeRate());
							
							break;
						}
					}
				}
			}
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "锚杆内力监测结果表", "锚杆内力监测", "初始读数(kN)", "初始读数(kN)",7,"");
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			if(periods.size()!=0){
//				creatLineChart("MT", sheet, pageCount, periods, codes, dataMap);
			}
			//导出之前需要将模板的sheet删掉
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportZCDataToExcel(String templatePath,String pjName,String pjAddress,String deviceName,Project project,String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "ZC");
			dataMap=(Map<String, Object>) map.get("ZC");
//			if(dataMap==null){
//				return null;
//			}
			//取出所有时间以生成所有列、取出所有监测点编号
			List<Date> periods=new ArrayList<Date>();
			List<String> codes=new ArrayList<String>();
			for (Map.Entry<String, Object> m : dataMap.entrySet()) {
				codes.add(m.getKey());
				Map<String, Object> dMap = (Map<String, Object>) m.getValue();
				List<ZC_Data> zcDataList = (List<ZC_Data>) dMap.get("data");
				for (int i = 0; i < zcDataList.size(); i++) {
					if (!(periods.contains(zcDataList.get(i).getCollectTime()))) {
						periods.add(zcDataList.get(i).getCollectTime());
					}
				}
			}
			//给时间排序
			periods.sort(new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					int compareCode = d1.compareTo(d2);
					return compareCode;
				}
			});
			//给周期排序
			codes.sort(new Comparator<String>() {
				@Override
				public int compare(String c1, String c2) {
					int compareCode = c1.compareTo(c2);
					return compareCode;
				}
			});
			
		
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}

			XSSFSheet sheet=workbook.createSheet("支撑内力");
			//一共多少个周期
			int periodCount=periods.size();
			//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
			//总页数
			int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
			//无数据的时候不能导出空白页，要有数据头
			if(pageCount==0){pageCount=1;}
			//总列数
			int columnCount=pageCount*17;
			//处理第一行
			createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
					periods, "支撑内力监测结果表", "支撑内力监测", "初始读数(kN)", "初始读数(kN)",8,"");
			
			//处理每一页的底部
			sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(9).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue());
				
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
			}
			int dataRowCount=dataMap.size();
			//底部往下移动对应的行数，为数据腾出位置
			//如果直接createRow会报错
			//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
			for (int i = 0; i < dataRowCount; i++) {
				//移动后行索引会发生变化
				sheet.shiftRows(9+i, 9+i, 1,false,false);
			}
			//创建显示全部数据所需要的单元格
			for (int i = 0; i < dataRowCount; i++) {
				XSSFRow row=sheet.createRow(9+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(textCellStyle);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					int ji=j%17;
					if(ji==3||ji==6||ji==9||ji==12||ji==15){
						sheet.addMergedRegion(new CellRangeAddress(9+i, 9+i, j-1, j));
					}
				}
			}
			//插入数据
			for (int i = 0; i < codes.size(); i++) {
				Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
				List<ZC_Data> zcDataList = (List<ZC_Data>) dMap.get("data");
				//如果没有数据也要添加监测点编号到表头
				if(zcDataList.size()<=0){
					for(int p=0;p<pageCount;p++){
						sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
					}
				}
				//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
				int pFlag=0;
				for(int j=0 ;j<zcDataList.size();j++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						//先填每页页首的测点编号、初始读数或上次累计值
						if(p%5==0){
							if(p==0){
								sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
								sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
							}else{
								sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
								if(j!=0){
									sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
								}
							}
						}
						if(zcDataList.get(j).getCollectTime().equals(periods.get(p))){
							int pageNum=p/5+1;
							int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
							sheet.getRow(9+i).getCell(columnNum).setCellValue(zcDataList.get(j).getGapOffset());
//							sheet.getRow(9+i).getCell(columnNum+1).setCellValue(zcDataList.get(j).getAccumOffset());
							sheet.getRow(9+i).getCell(columnNum+2).setCellValue(zcDataList.get(j).getGapChangeRate());
							
							break;
						}
					}
				}
			}
			//先移动行数据
			int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
			if(times>1){
				for(int i=0;i<times-1;i++){
					sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
					sheet.setRowBreak(33+i*34+i+1);
					createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , deviceName, specification,
							periods, "支撑内力监测结果表", "支撑内力监测", "初始读数(kN)", "初始读数(kN)",8,"");
				}
			}
			createBlankRow( codes, sheet, times, columnCount,templateSheet);
			if(periods.size()!=0){
				//创建展示折线图的最后一页并画出折线图
//				creatLineChart("ZC", sheet, pageCount, periods, codes, dataMap);
			}
			//导出之前需要将模板的sheet删掉
			workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportCXDataToExcel(String templatePath, String pjName, String pjAddress, String deviceName,Project project, String specification, String startTime, String endTime) {
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, List<CX_Data>> dataMap=new HashMap<String,List<CX_Data>>();
		try {
			//获取数据
			reportService.timeReport(map, project, startTime, endTime, "CX");
			dataMap=(Map<String, List<CX_Data>>) map.get("CX");
//			if(dataMap==null){
//				return null;
//			}
			
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			
			//找到模板sheet
			XSSFSheet templateSheet=null;
			Iterator<Sheet> sheetIterator=workbook.sheetIterator();
			while(sheetIterator.hasNext()){
				XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
				if(tempSheet.getSheetName().equals("template")){
					templateSheet=tempSheet;
				}
			}
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
			//建立多个sheet
			for(int n=0;n<codes.size();n++){
				List<CX_Data> cxDataList = dataMap.get(codes.get(n));
				Map<Date, List<CX_Data>> timeDataMap =new HashMap<Date, List<CX_Data>>();
				List<Date> periods=new ArrayList<Date>();
				for (int i = 0; i < cxDataList.size(); i++) {
					if (!(periods.contains(cxDataList.get(i).getCollectTime()))) {
						periods.add(cxDataList.get(i).getCollectTime());
						timeDataMap.put(cxDataList.get(i).getCollectTime(), new ArrayList<CX_Data>());
					}
					((List<CX_Data>)timeDataMap.get(cxDataList.get(i).getCollectTime())).add(cxDataList.get(i));
				}
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				
				XSSFSheet sheet=workbook.createSheet(codes.get(n));
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
						cell.setCellValue("附件3");
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
						sheet.getRow(7).getCell(i*12+2).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+0)):"");
						sheet.getRow(7).getCell(i*12+3).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+1)):"");
						sheet.getRow(7).getCell(i*12+4).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+2)):"");
						sheet.getRow(7).getCell(i*12+5).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+3)):"");
						sheet.addMergedRegion(new CellRangeAddress(5, 5, i*12+5, i*12+7));
						sheet.addMergedRegion(new CellRangeAddress(7, 7, i*12+5, i*12+7));
					}else{
						int num=(periods.size()%4==0&&periods.size()>0)?4:periods.size()%4;
						for(int col=0;col<num;col++){
							sheet.getRow(5).getCell(i*12+2+col).setCellValue("第"+(i*4+col+1)+"次");
							sheet.getRow(6).getCell(i*12+2+col).setCellValue("累积位移(mm)");
							sheet.getRow(7).getCell(i*12+2+col).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+col)):"");
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

				//深度/0.5即为数据总行数,*2后必然是整数
				int dataRowCount=0;
				if(periods.size()>0&&timeDataMap.get(periods.get(0)).size()>0){
					dataRowCount=(int) ((timeDataMap.get(periods.get(0)).get(0).getSurveyPoint().getDeep())*2);
				}
				
				//创建显示全部数据所需要的单元格和页尾的一行
				for (int i = 0; i < dataRowCount+1; i++) {
					XSSFRow row=sheet.createRow(8+i);
					//以第6行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(templateSheet.getRow(8).getCell(2).getCellStyle());
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						if((j-1)%12==0){
							//循环增加深度、、、测斜点号只需要在第一行设置值就行
							cell.setCellValue((i+1)*-0.5);
							if(i==0){
								sheet.getRow(8+i).getCell(j-1).setCellValue(codes.get(n));
							}
						}
					}
				}
				//
				for (int i = 0; i < pageCount; i++) {
					sheet.getRow(dataRowCount+8).getCell(i*12).setCellValue("备注");
					sheet.getRow(dataRowCount+8).getCell(i*12+2).setCellValue("1、“+”表示向基坑内位移,“-”表示向基坑外位移。");
					sheet.addMergedRegion(new CellRangeAddress(8+dataRowCount, 8+dataRowCount, i*12,i*12+1));
					sheet.addMergedRegion(new CellRangeAddress(8+dataRowCount, 8+dataRowCount, i*12+2, i*12+11));
					if(dataRowCount>0){
						sheet.addMergedRegion(new CellRangeAddress(8, 8+dataRowCount-1, i*12, i*12));
					}
				}
				//记录真实数据的最大条数，不然画图会出现报错
				int dataSize=0;
				//根据每页的行数插入数据
				for (int i = 0; i < periods.size(); i++) {
					List<CX_Data> cxdataList = timeDataMap.get(periods.get(i));
					if(cxdataList.size()>dataSize){
						dataSize=cxdataList.size();
					}
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
				creatScatterChart( sheet, pageCount, dataRowCount, periods, dataSize, timeDataMap);
			}

			//导出之前需要将模板的sheet删掉
			if(dataMap.size()!=0){
				workbook.removeSheetAt(workbook.getSheetIndex(templateSheet));
			}
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	

	@SuppressWarnings("unchecked")
	private void creatLineChart(String chartType,XSSFSheet sheet,int pageCount,List<Date> periods,List<String> codes,Map<String, Object> dataMap){
		//创建展示关系曲线图的一页
		for(int i=0;i<5;i++){
			for(int j=0;j<17;j++){
				sheet.setColumnWidth(pageCount*17+j, 1334);
				if(j==1||j==9){
					sheet.setColumnWidth(pageCount*17+j, 1320);
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
		//画折线图
		Drawing drawing = sheet.createDrawingPatriarch();  
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, pageCount*17, 5, pageCount*17+16, 33);  
		Chart chart = drawing.createChart(anchor);  
		//创建图形注释的位置  
		ChartLegend legend = chart.getOrCreateLegend();  
		legend.setPosition(LegendPosition.TOP);  
		//创建绘图的类型   LineCahrtData 折线图  
		LineChartData chartData = chart.getChartDataFactory().createLineChartData();  
		//设置横坐标  
		ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);  
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);  
		//设置纵坐标  
		ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);  
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		leftAxis.setMajorTickMark(AxisTickMark.OUT);
//		leftAxis.setOrientation();
//		leftAxis.setPosition(Position.After);
		leftAxis.isVisible();
		//横坐标数组获取
		List<String> timeList=new ArrayList<>();
		for(int i=0;i<periods.size();i++){
			timeList.add(new SimpleDateFormat("yyyy_MM_dd").format(periods.get(i)));
		}
		String[] timeArr = (String[])timeList.toArray(new String[timeList.size()]);
		//添加各系列点
		for(int num=0;num<dataMap.size();num++){
			//设置Y轴数组，要根据时间数组一一对应，注意当中间Y轴数据出现空值的处理______这里设置了跟时间数组大小相同的数组，对应时间点有值时填值
			Double[] elements =new Double[periods.size()];
			//获取监测点数据MAP
			Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(num));
			if(chartType.equals("WYS")){
				List<WYS_CoordData> dataList = (List<WYS_CoordData>) dMap.get("data");
				//dataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<dataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(dataList.get(i).getSurveyTime().equals(periods.get(p))){
							elements[p]=dataList.get(i).getAccumEOffset();
							break;
						}
					}
				}
			}else if(chartType.equals("WYD")){
				List<WYD_LevelData> dataList = (List<WYD_LevelData>) dMap.get("data");
				//dataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<dataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(dataList.get(i).getSurveyTime().equals(periods.get(p))){
							elements[p]=dataList.get(i).getAccumHOffset();
							break;
						}
					}
				}
			}else if(chartType.equals("LZ")){
				List<LZ_Data> dataList = (List<LZ_Data>) dMap.get("data");
				//dataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<dataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(dataList.get(i).getSurveyTime().equals(periods.get(p))){
							elements[p]=dataList.get(i).getAccumHOffset();
							break;
						}
					}
				}
			}else if(chartType.equals("SM")){
				List<SM_Data> dataList = (List<SM_Data>) dMap.get("data");
				//dataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<dataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(dataList.get(i).getSurveyTime().equals(periods.get(p))){
							elements[p]=dataList.get(i).getAccumHOffset();
							break;
						}
					}
				}
			}else if(chartType.equals("SW")){
				List<SW_Data> dataList = (List<SW_Data>) dMap.get("data");
				//dataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<dataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(dataList.get(i).getCollectTime().equals(periods.get(p))){
							elements[p]=dataList.get(i).getAccumOffset();
							break;
						}
					}
				}
			}else if(chartType.equals("ZGD")){
				List<ZGD_Data> dataList = (List<ZGD_Data>) dMap.get("data");
				//dataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<dataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(dataList.get(i).getSurveyTime().equals(periods.get(p))){
							elements[p]=dataList.get(i).getAccumHOffset();
							break;
						}
					}
				}
			}else if(chartType.equals("MT")){
				List<MT_Data> mtDataList = (List<MT_Data>) dMap.get("data");
				//mtDataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<mtDataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(mtDataList.get(i).getCollectTime().equals(periods.get(p))){
							elements[p]=mtDataList.get(i).getCalValue();
							break;
						}
					}
				}
			}else if(chartType.equals("ZC")){
				List<ZC_Data> dataList = (List<ZC_Data>) dMap.get("data");
				//mtDataList是按时间排序的，这里设置了pFlag可以使得遍历过的时间不再重复
				int pFlag=0;
				for(int i=0;i<dataList.size();i++){
					for(int p=pFlag;p<periods.size();p++){
						pFlag+=1;
						if(dataList.get(i).getCollectTime().equals(periods.get(p))){
							elements[p]=dataList.get(i).getCalValue();
							break;
						}
					}
				}
			}
			
			//为chart的系列绑定数据源
			ChartDataSource<String> xAxis = DataSources.fromArray(timeArr);  
			ChartDataSource<Number> dataAxis = DataSources.fromArray(elements);
			LineChartSeries chartSeries = chartData.addSeries(xAxis, dataAxis);
			chartSeries.setTitle(codes.get(num));
		}
		//开始绘制折线图  
		chart.plot(chartData, bottomAxis, leftAxis);
	}
	
	@SuppressWarnings("unused")
	private void creatScatterChartFromSheet(Sheet sheet,int pageCount,int dataRowCount,List<Date> periods,int dataSize){
		for(int num=0;num<pageCount;num++){
			//创建绘图区域  
			Drawing drawing = sheet.createDrawingPatriarch();  
			ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, num*12+8,5, num*12+12,8+dataRowCount);  
			Chart chart = drawing.createChart(anchor);  
			//创建图形注释的位置  
			ChartLegend legend = chart.getOrCreateLegend();  
			legend.setPosition(LegendPosition.TOP);  
			//创建绘图的类型   LineCahrtData 折线图  
			ScatterChartData chartData = chart.getChartDataFactory().createScatterChartData();  
			//设置横坐标  
			ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);  
			bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
//			bottomAxis.isSetMinimum();
//			bottomAxis.setVisible(true);
			
			//设置纵坐标  
			ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
//			leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
			leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
//			leftAxis.isSetMinimum();
//			leftAxis.setVisible(true);
			//leftAxis.setAxisLineVisible(true);
//			leftAxis.setMajorTickMark(AxisTickMark.IN);
			//每页系列数小于等于4
			for(int i=0;i<4;i++){
				if((num*4+i)<periods.size()){
					//从Excel中获取数据  
					ChartDataSource<Number> xAxis = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(8, 7+dataSize, num*12+1, num*12+1));  
					ChartDataSource<Number> dataAxis = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(8, 7+dataSize, num*12+2+i, num*12+2+i));
					//将获取到的数据填充到折线图  
					ScatterChartSeries chartSeries = chartData.addSerie(dataAxis, xAxis);
					//给每条这项创建名字  
					chartSeries.setTitle(new SimpleDateFormat("yyyy_MM_dd").format(periods.get(num*4+i))); 
				}else{
					break;
				}
				
			}
			//开始绘制折线图  
			chart.plot(chartData, bottomAxis, leftAxis);
		}
	}
	
	private void creatScatterChart(Sheet sheet,int pageCount,int dataRowCount,List<Date> periods,int dataSize,Map<Date, List<CX_Data>> timeDataMap){
		for(int num=0;num<pageCount;num++){
			//创建绘图区域  
			Drawing drawing = sheet.createDrawingPatriarch();  
			ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, num*12+8,5, num*12+12,8+dataRowCount);  
			Chart chart = drawing.createChart(anchor);  
			//创建图形注释的位置  
			ChartLegend legend = chart.getOrCreateLegend();  
			legend.setPosition(LegendPosition.TOP);  
			//创建绘图的类型   LineCahrtData 折线图  
			ScatterChartData chartData = chart.getChartDataFactory().createScatterChartData();  
			//设置横坐标  
			ChartAxis bottomAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.BOTTOM); 
			bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
			//设置纵坐标  
			ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.RIGHT);
			leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
			//数据
			for(int i=0;i<4;i++){
				if((num*4+i)<periods.size()){
					List<CX_Data> cxdataList = timeDataMap.get(periods.get(num*4+i));
					Double[] deeps =new Double[cxdataList.size()];
					Double[] elements =new Double[cxdataList.size()];
					for(int j=0;j<cxdataList.size();j++){
						deeps[j]=(j*-0.50)-0.50;
						for(int p=0;p<cxdataList.size();p++){
							if(cxdataList.get(p).getDepth()==j*0.5+0.5){
								elements[j]=cxdataList.get(p).getAccumOffset();
								break;
							}
						}
					}
					//从Excel中获取数据  
					ChartDataSource<Number> xAxis = DataSources.fromArray(deeps);
					ChartDataSource<Number> dataAxis = DataSources.fromArray(elements);
					//当最后一页是单独一条数据的时候，多画一条同样的线可避免Excel画图出现的bug
					if(periods.size()%4==1&&(num*4+i)==periods.size()-1){
						ChartDataSource<Number> xAxis2 = DataSources.fromArray(deeps);
						ChartDataSource<Number> dataAxis2 = DataSources.fromArray(elements);
						ScatterChartSeries chartSeries2 = chartData.addSerie(dataAxis2, xAxis2);
						chartSeries2.setTitle(" "); 
					}
					//将获取到的数据填充到折线图  
					ScatterChartSeries chartSeries = chartData.addSerie(dataAxis, xAxis);
					//给每条这项创建名字  
					chartSeries.setTitle(new SimpleDateFormat("yyyy_MM_dd").format(periods.get(num*4+i)));
					
				}else{
					break;
				}
			}
			//开始绘制折线图  
			chart.plot(chartData, bottomAxis, leftAxis);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] exportAllSurveyDataToExcel(String templatePath,String pjName, String pjAddress, Project project,ProjectReport pr,
			 String startTime, String endTime) {
		//获得工程的监测项
		List<ProjectMonitorItem> projectMonitorItems = projectMonitorItemService.getMonitorItemsByProject(project);
		
		Map<String, Object> map=new HashMap<String,Object>();
		Map<String, Object> dataMap=new HashMap<String,Object>();
		boolean isDailyReport=startTime.split(" ")[0].equals(endTime.split(" ")[0]);
		try {
			XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(templatePath)));
			//设置字体
			XSSFFont font=workbook.createFont();
			font.setFontHeight((short) (9*20));//字体大小
			font.setColor(XSSFFont.COLOR_NORMAL);//字体颜色
			font.setFontName("宋体");//字体
			
			//创建单元格样式
			//文本样式
			XSSFCellStyle textCellStyle=workbook.createCellStyle();
			textCellStyle.setAlignment(HorizontalAlignment.CENTER);
			textCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			textCellStyle.setBorderLeft(BorderStyle.THIN);//设置边框
			textCellStyle.setBorderTop(BorderStyle.THIN);
			textCellStyle.setBorderRight(BorderStyle.THIN);
			textCellStyle.setBorderBottom(BorderStyle.THIN);
			textCellStyle.setWrapText(true);
			textCellStyle.setFont(font);
			int fulu=1;
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 1)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "WYS");
				dataMap=(Map<String, Object>) map.get("WYS");
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				List<String> spCodeChar=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<WYS_CoordData> coordDataList = (List<WYS_CoordData>) dMap.get("data");
					for (int i = 0; i < coordDataList.size(); i++) {
						if (!(periods.contains(coordDataList.get(i).getSurveyTime()))) {
							periods.add(coordDataList.get(i).getSurveyTime());
						}
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
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给编号排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("spTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("WYS");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				if(pageCount==0){
					pageCount=1;
				}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p78(), pr.getP1p79(),
						periods, "围护墙(边坡)顶部水平位移监测结果表", "围护墙(边坡)顶部水平位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
					
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					}
				}
				//插入数据
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<WYS_CoordData> coordDataList = (List<WYS_CoordData>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(coordDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<coordDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							pFlag+=1;
							//先填每页页首的测点编号、初始读数或上次累计值
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalE")==null? "":dMap.get("originalE").toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(coordDataList.get(j-1).getAccumEOffset());
									}
								}
							}
							if(coordDataList.get(j).getSurveyTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(coordDataList.get(j).getGapEOffset());
								sheet.getRow(9+i).getCell(columnNum+1).setCellValue(coordDataList.get(j).getAccumEOffset());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(coordDataList.get(j).getChangeRate());
								break;
							}
						}
					}
				}
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p78(), pr.getP1p79(),
								periods, "围护墙(边坡)顶部水平位移监测结果表", "围护墙(边坡)顶部水平位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				//创建最后一页,当时间不是同一天时
				if(periods.size()>0&&!isDailyReport){
				creatLineChart( "WYS", sheet, pageCount, periods, codes, dataMap);
				}
			}
			//支护结构深层水平位移
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 4)){
				fulu=fulu+1;
				Map<String, List<CX_Data>> cxdataMap=new HashMap<String,List<CX_Data>>();
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "CX");
				cxdataMap=(Map<String, List<CX_Data>>) map.get("CX");
				//找到模板sheet
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("cxTemplate")){
						templateSheet=tempSheet;
					}
				}
				//取出所有时间以生成所有列、取出所有监测点编号
				
				List<String> codes=new ArrayList<String>();
				for (Map.Entry<String, List<CX_Data>> m : cxdataMap.entrySet()) {
					codes.add(m.getKey());
				}
				//给编号排序、便于按测斜编号建立sheet
				 String codeChar="";
				if(codes.size()>0&&cxdataMap.get(codes.get(0)).size()>0){
					codeChar=codeChar+cxdataMap.get(codes.get(0)).get(0).getSurveyPoint().getCodeChar();
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
				//建立多个sheet
				for(int n=0;n<codes.size();n++){
					List<CX_Data> cxDataList = cxdataMap.get(codes.get(n));
					Map<Date, List<CX_Data>> timeDataMap =new HashMap<Date, List<CX_Data>>();
					List<Date> periods=new ArrayList<Date>();
					for (int i = 0; i < cxDataList.size(); i++) {
						if (!(periods.contains(cxDataList.get(i).getCollectTime()))) {
							periods.add(cxDataList.get(i).getCollectTime());
							timeDataMap.put(cxDataList.get(i).getCollectTime(), new ArrayList<CX_Data>());
						}
						((List<CX_Data>)timeDataMap.get(cxDataList.get(i).getCollectTime())).add(cxDataList.get(i));
					}
					//给时间排序
					periods.sort(new Comparator<Date>() {
						@Override
						public int compare(Date d1, Date d2) {
							int compareCode = d1.compareTo(d2);
							return compareCode;
						}
					});
					
					XSSFSheet sheet=workbook.createSheet(codes.get(n));
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
						sheet.addMergedRegion(new CellRangeAddress(1, 1, i*12+2, i*12+7));
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
							cell.setCellValue(pr.getP1p90());
						}else{
							cell.setCellValue(templateSheet.getRow(2).getCell(index).getStringCellValue());
						}
					}
					for (int i = 0; i < pageCount; i++) {
						sheet.addMergedRegion(new CellRangeAddress(2, 2, i*12, i*12+1));
						sheet.addMergedRegion(new CellRangeAddress(2, 2, i*12+2, i*12+7));
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
							cell.setCellValue(pr.getP1p91());
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
//						cell2.setCellValue(templateSheet.getRow(7).getCell(index).getStringCellValue());
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
							sheet.getRow(7).getCell(i*12+2).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+0)):"");
							sheet.getRow(7).getCell(i*12+3).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+1)):"");
							sheet.getRow(7).getCell(i*12+4).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+2)):"");
							sheet.getRow(7).getCell(i*12+5).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+3)):"");
							sheet.addMergedRegion(new CellRangeAddress(5, 5, i*12+5, i*12+7));
							sheet.addMergedRegion(new CellRangeAddress(7, 7, i*12+5, i*12+7));
						}else{
							int num=(periods.size()%4==0&&periods.size()>0)?4:periods.size()%4;
							for(int col=0;col<num;col++){
								sheet.getRow(5).getCell(i*12+2+col).setCellValue("第"+(i*4+col+1)+"次");
								sheet.getRow(6).getCell(i*12+2+col).setCellValue("累积位移(mm)");
								sheet.getRow(7).getCell(i*12+2+col).setCellValue(periods.size()>0?new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*4+col)):"");
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

					//深度/0.5即为数据总行数,*2后必然是整数
					int dataRowCount=0;
					if(periods.size()>0&&timeDataMap.get(periods.get(0)).size()>0){
						dataRowCount=(int) ((timeDataMap.get(periods.get(0)).get(0).getSurveyPoint().getDeep())*2);
					}
					
					//创建显示全部数据所需要的单元格和页尾的一行
					for (int i = 0; i < dataRowCount+1; i++) {
						XSSFRow row=sheet.createRow(8+i);
						//以第6行数据的高度为准
						row.setHeight(sheet.getRow(5).getHeight());
						for (int j = 0; j < columnCount; j++) {
							XSSFCell cell=row.createCell(j);
							cell.setCellStyle(templateSheet.getRow(8).getCell(2).getCellStyle());
							cell.setCellType(XSSFCell.CELL_TYPE_STRING);
							if((j-1)%12==0){
								//循环增加深度、、、测斜点号只需要在第一行设置值就行
								cell.setCellValue((i+1)*-0.5);
								if(i==0){
									sheet.getRow(8+i).getCell(j-1).setCellValue(codes.get(n));
								}
							}
						}
					}
					//
					for (int i = 0; i < pageCount; i++) {
						sheet.getRow(dataRowCount+8).getCell(i*12).setCellValue("备注");
						sheet.getRow(dataRowCount+8).getCell(i*12+2).setCellValue("1、“+”表示向基坑内位移,“-”表示向基坑外位移。");
						sheet.addMergedRegion(new CellRangeAddress(8+dataRowCount, 8+dataRowCount, i*12,i*12+1));
						sheet.addMergedRegion(new CellRangeAddress(8+dataRowCount, 8+dataRowCount, i*12+2, i*12+11));
						if(dataRowCount>0){
							sheet.addMergedRegion(new CellRangeAddress(8, 8+dataRowCount-1, i*12, i*12));
						}
					}
					//记录真实数据的最大条数，不然画图会出现报错
					int dataSize=0;
					//根据每页的行数插入数据
					for (int i = 0; i < periods.size(); i++) {
						List<CX_Data> cxdataList = timeDataMap.get(periods.get(i));
						if(cxdataList.size()>dataSize){
							dataSize=cxdataList.size();
						}
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
					creatScatterChart( sheet, pageCount, dataRowCount, periods, dataSize, timeDataMap);
				}

			}
			//围护墙(边坡)顶部竖向位移
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 5)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "WYD");
				dataMap=(Map<String, Object>) map.get("WYD");
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				List<String> spCodeChar=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<WYD_LevelData> wydDataList = (List<WYD_LevelData>) dMap.get("data");
					for (int i = 0; i < wydDataList.size(); i++) {
						if (!(periods.contains(wydDataList.get(i).getSurveyTime()))) {
							periods.add(wydDataList.get(i).getSurveyTime());
						}
						if (!(spCodeChar.contains(wydDataList.get(i).getSurveyPoint().getCodeChar()))) {
							spCodeChar.add(wydDataList.get(i).getSurveyPoint().getCodeChar());
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
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给测点编号排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("spTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("WYD");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				//无数据的时候不能导出空白页，要有数据头
				if(pageCount==0){pageCount=1;}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p80(), pr.getP1p81(),
						periods, "围护墙(边坡)顶部竖向位移监测结果表", "围护墙(边坡)顶部竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
				
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					}
				}
				//插入数据
//				int pageCount=(periods.size())%5==0?(periods.size())/5:(periods.size())/5+1;
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<WYD_LevelData> wydDataList = (List<WYD_LevelData>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(wydDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<wydDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							pFlag+=1;
							//先填每页页首的测点编号、初始读数或上次累计值
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(wydDataList.get(j-1).getAccumHOffset());
									}
								}
							}
							if(wydDataList.get(j).getSurveyTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(wydDataList.get(j).getGapHOffset());
								sheet.getRow(9+i).getCell(columnNum+1).setCellValue(wydDataList.get(j).getAccumHOffset());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(wydDataList.get(j).getGapHChangeRate());
								
								break;
							}
						}
					}
				}
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p80(), pr.getP1p81(),
								periods, "围护墙(边坡)顶部竖向位移监测结果表", "围护墙(边坡)顶部竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				//创建展示折线图的最后一页并画出折线图
				if(periods.size()>0&&!isDailyReport){
				creatLineChart("WYD", sheet, pageCount, periods, codes, dataMap);
				}
				//导出之前需要将模板的sheet删掉
				
			}
			
			//立柱竖向位移
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 6)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "LZ");
				dataMap=(Map<String, Object>) map.get("LZ");
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				List<String> spCodeChar=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<LZ_Data> lzDataList = (List<LZ_Data>) dMap.get("data");
					for (int i = 0; i < lzDataList.size(); i++) {
						if (!(periods.contains(lzDataList.get(i).getSurveyTime()))) {
							periods.add(lzDataList.get(i).getSurveyTime());
						}
						if (!(spCodeChar.contains(lzDataList.get(i).getSurveyPoint().getCodeChar()))) {
							spCodeChar.add(lzDataList.get(i).getSurveyPoint().getCodeChar());
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
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给周期排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("spTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("LZ");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				//无数据的时候不能导出空白页，要有数据头
				if(pageCount==0){pageCount=1;}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p92(), pr.getP1p93(),
						periods, "立柱竖向位移监测结果表", "立柱竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
				
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
					
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					}
				}
				//插入数据
//				int pageCount=(periods.size())%5==0?(periods.size())/5:(periods.size())/5+1;
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<LZ_Data> lzDataList = (List<LZ_Data>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(lzDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<lzDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							pFlag+=1;
							//先填每页页首的测点编号、初始读数或上次累计值
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(lzDataList.get(j-1).getAccumHOffset());
									}
								}
							}
							if(lzDataList.get(j).getSurveyTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(lzDataList.get(j).getGapHOffset());
								sheet.getRow(9+i).getCell(columnNum+1).setCellValue(lzDataList.get(j).getAccumHOffset());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(lzDataList.get(j).getGapHOffsetChangeRate());
								
								break;
							}
						}
					}
				}
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p92(), pr.getP1p93(),
								periods, "立柱竖向位移监测结果表", "立柱竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				//创建展示折线图的最后一页并画出折线图
				if(periods.size()>0&&!isDailyReport){
				creatLineChart("LZ", sheet, pageCount, periods, codes, dataMap);
				}
			}
			//周边建筑物竖向位移
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 8)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "SM");
				dataMap=(Map<String, Object>) map.get("SM");
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				List<String> spCodeChar=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<SM_Data> smDataList = (List<SM_Data>) dMap.get("data");
					for (int i = 0; i < smDataList.size(); i++) {
						if (!(periods.contains(smDataList.get(i).getSurveyTime()))) {
							periods.add(smDataList.get(i).getSurveyTime());
						}
						if (!(spCodeChar.contains(smDataList.get(i).getSurveyPoint().getCodeChar()))) {
							spCodeChar.add(smDataList.get(i).getSurveyPoint().getCodeChar());
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
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给周期排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("spTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("SM");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				//无数据的时候不能导出空白页，要有数据头
				if(pageCount==0){pageCount=1;}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p84(), pr.getP1p85(),
						periods, "周边建筑物竖向位移监测结果表", "周边建筑物竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
				
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
					
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					}
				}
				//插入数据
//				int pageCount=(periods.size())%5==0?(periods.size())/5:(periods.size())/5+1;
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<SM_Data> smDataList = (List<SM_Data>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(smDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<smDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							pFlag+=1;
							//先填每页页首的测点编号、初始读数或上次累计值
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(smDataList.get(j-1).getAccumHOffset());
									}
								}
							}
							if(smDataList.get(j).getSurveyTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(smDataList.get(j).getGapHOffset());
								sheet.getRow(9+i).getCell(columnNum+1).setCellValue(smDataList.get(j).getAccumHOffset());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(smDataList.get(j).getGapHOffsetChangeRate());
								
								break;
							}
						}
					}
				}
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p84(), pr.getP1p85(),
								periods, "周边建筑物竖向位移监测结果表", "周边建筑物竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				//创建展示折线图的最后一页并画出折线图
				if(periods.size()>0&&!isDailyReport){
					creatLineChart("SM", sheet, pageCount, periods, codes, dataMap);
				}
				
			}

			//周边管线竖向位移
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 10)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "ZGD");
				dataMap=(Map<String, Object>) map.get("ZGD");
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				List<String> spCodeChar=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<ZGD_Data> zgdDataList = (List<ZGD_Data>) dMap.get("data");
					for (int i = 0; i < zgdDataList.size(); i++) {
						if (!(periods.contains(zgdDataList.get(i).getSurveyTime()))) {
							periods.add(zgdDataList.get(i).getSurveyTime());
						}
						if (!(spCodeChar.contains(zgdDataList.get(i).getSurveyPoint().getCodeChar()))) {
							spCodeChar.add(zgdDataList.get(i).getSurveyPoint().getCodeChar());
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
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给周期排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("spTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("ZGD");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				//无数据的时候不能导出空白页，要有数据头
				if(pageCount==0){pageCount=1;}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p86(), pr.getP1p87(),
						periods, "周边管线竖向位移监测结果表", "周边管线竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
					
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					}
				}
				//插入数据
//				int pageCount=(periods.size())%5==0?(periods.size())/5:(periods.size())/5+1;
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<ZGD_Data> zgdDataList = (List<ZGD_Data>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(zgdDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<zgdDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							pFlag+=1;
							//先填每页页首的测点编号、初始读数或上次累计值
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalH")==null? "":String.format("%.2f", dMap.get("originalH")).toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(zgdDataList.get(j-1).getAccumHOffset());
									}
								}
							}
							if(zgdDataList.get(j).getSurveyTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(zgdDataList.get(j).getGapHOffset());
								sheet.getRow(9+i).getCell(columnNum+1).setCellValue(zgdDataList.get(j).getAccumHOffset());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(zgdDataList.get(j).getGapHChangeRate());
								
								break;
							}
						}
					}
				}
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p86(), pr.getP1p87(),
								periods, "周边管线竖向位移监测结果表", "周边管线竖向位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				//创建展示折线图的最后一页并画出折线图
				if(periods.size()>0&&!isDailyReport){
					creatLineChart("ZGD", sheet, pageCount, periods, codes, dataMap);
				}
			}
			//地下水位
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 12)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "SW");
				dataMap=(Map<String, Object>) map.get("SW");
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				List<String> spCodeChar=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<SW_Data> swDataList = (List<SW_Data>) dMap.get("data");
					for (int i = 0; i < swDataList.size(); i++) {
						if (!(periods.contains(swDataList.get(i).getCollectTime()))) {
							periods.add(swDataList.get(i).getCollectTime());
						}
						if (!(spCodeChar.contains(swDataList.get(i).getSurveyPoint().getCodeChar()))) {
							spCodeChar.add(swDataList.get(i).getSurveyPoint().getCodeChar());
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
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给周期排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("spTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("SW");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				//无数据的时候不能导出空白页，要有数据头
				if(pageCount==0){pageCount=1;}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p88(), pr.getP1p89(),
						periods, "地下水位监测结果表", "地下水位移监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
				
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue().replaceAll("codeChar", spCodeCharStr));
					
				
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
					
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					}
				}
				//插入数据
//				int pageCount=(periods.size())%5==0?(periods.size())/5:(periods.size())/5+1;
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<SW_Data> swDataList = (List<SW_Data>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(swDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<swDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							//先填每页页首的测点编号、初始读数或上次累计值
							pFlag+=1;
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(swDataList.get(j-1).getAccumOffset());
										System.out.println((p/5*17+1)+"时间："+swDataList.get(j-1).getCollectTime().toString()+"累计值："+swDataList.get(j-1).getAccumOffset());
									}
								}
							}
							if(swDataList.get(j).getCollectTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(swDataList.get(j).getGapOffset());
								sheet.getRow(9+i).getCell(columnNum+1).setCellValue(swDataList.get(j).getAccumOffset());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(swDataList.get(j).getGapChangeRate());
								break;
							}
						}
					}
				}
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p88(), pr.getP1p89(),
								periods, "地下水位监测结果表", "地下水位监测", "初始读数(m)", "上次累计(mm)",fulu,spCodeCharStr);
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				//创建展示折线图的最后一页并画出折线图
				if(periods.size()>0&&!isDailyReport){
					creatLineChart("SW", sheet, pageCount, periods, codes, dataMap);
				}
			}
			//支撑内力
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 15)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "ZC");
				dataMap=(Map<String, Object>) map.get("ZC");
//				if(dataMap==null){
//					return null;
//				}
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<ZC_Data> zcDataList = (List<ZC_Data>) dMap.get("data");
					for (int i = 0; i < zcDataList.size(); i++) {
						if (!(periods.contains(zcDataList.get(i).getCollectTime()))) {
							periods.add(zcDataList.get(i).getCollectTime());
						}
					}
				}
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给周期排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("mtTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("支撑内力");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				//无数据的时候不能导出空白页，要有数据头
				if(pageCount==0){pageCount=1;}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p94(), pr.getP1p95(),
						periods, "支撑内力监测结果表", "支撑内力监测", "初始读数(kN)", "初始读数(kN)",fulu,"");
				
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue());
					
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						int ji=j%17;
						if(ji==3||ji==6||ji==9||ji==12||ji==15){
							sheet.addMergedRegion(new CellRangeAddress(9+i, 9+i, j-1, j));
						}
					}
				}
				//插入数据
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<ZC_Data> zcDataList = (List<ZC_Data>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(zcDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<zcDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							pFlag+=1;
							//先填每页页首的测点编号、初始读数或上次累计值
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
									}
								}
							}
							if(zcDataList.get(j).getCollectTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(zcDataList.get(j).getGapOffset());
//								sheet.getRow(9+i).getCell(columnNum+1).setCellValue(zcDataList.get(j).getAccumOffset());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(zcDataList.get(j).getGapChangeRate());
								
								break;
							}
						}
					}
				}
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p94(), pr.getP1p95(),
								periods, "支撑内力监测结果表", "支撑内力监测", "初始读数(kN)", "初始读数(kN)",fulu,"");
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				//创建展示折线图的最后一页并画出折线图
				if(periods.size()>0&&!isDailyReport){
					creatLineChart("ZC", sheet, pageCount, periods, codes, dataMap);
				}
			}
			//锚杆内力
			if(hasMonitorItemByNumber(projectMonitorItems, (byte) 18)){
				fulu=fulu+1;
				//获取数据
				reportService.timeReport(map, project, startTime, endTime, "MT");
				dataMap=(Map<String, Object>) map.get("MT");
				//取出所有时间以生成所有列、取出所有监测点编号
				List<Date> periods=new ArrayList<Date>();
				List<String> codes=new ArrayList<String>();
				for (Map.Entry<String, Object> m : dataMap.entrySet()) {
					codes.add(m.getKey());
					Map<String, Object> dMap = (Map<String, Object>) m.getValue();
					List<MT_Data> mtDataList = (List<MT_Data>) dMap.get("data");
					for (int i = 0; i < mtDataList.size(); i++) {
						if (!(periods.contains(mtDataList.get(i).getCollectTime()))) {
							periods.add(mtDataList.get(i).getCollectTime());
						}
					}
				}
				//给时间排序
				periods.sort(new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						int compareCode = d1.compareTo(d2);
						return compareCode;
					}
				});
				//给周期排序
				codes.sort(new Comparator<String>() {
					@Override
					public int compare(String c1, String c2) {
						int compareCode = c1.compareTo(c2);
						return compareCode;
					}
				});
				XSSFSheet templateSheet=null;
				Iterator<Sheet> sheetIterator=workbook.sheetIterator();
				while(sheetIterator.hasNext()){
					XSSFSheet tempSheet=(XSSFSheet) sheetIterator.next();
					if(tempSheet.getSheetName().equals("mtTemplate")){
						templateSheet=tempSheet;
					}
				}
				XSSFSheet sheet=workbook.createSheet("锚杆内力");
				//一共多少个周期
				int periodCount=periods.size();
				//每一页显示5个周期，一个周期占用3列，加上前面2列，如果填满的话是一页17列
				//总页数
				int pageCount=periodCount%5==0?periodCount/5:periodCount/5+1;
				//无数据的时候不能导出空白页，要有数据头
				if(pageCount==0){pageCount=1;}
				//总列数
				int columnCount=pageCount*17;
				//处理第一行
				createSheetHead(0,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p82(), pr.getP1p83(),
						periods, "锚杆内力监测结果表", "锚杆内力监测", "初始读数(kN)", "初始读数(kN)",fulu,"");
				
				//处理每一页的底部
				sheet.createRow(9).setHeight(templateSheet.getRow(9).getHeight());
				
				for (int i = 0; i < columnCount; i++) {
					//17列为一页，取余数，即第一页对应的列所在的索引
					int index=i%17;
					XSSFCell cell=sheet.getRow(9).createCell(i);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
					cell.setCellValue(templateSheet.getRow(9).getCell(index).getStringCellValue());
				}
				for (int i = 0; i < pageCount; i++) {
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17, i*17+1));
					sheet.addMergedRegion(new CellRangeAddress(9, 9, i*17+2, i*17+2+14));
				}
				int dataRowCount=dataMap.size();
				//底部往下移动对应的行数，为数据腾出位置
				//如果直接createRow会报错
				//必须要一行一行移动，否则第三个参数n大于要移动的数据的行数就会出问题
				for (int i = 0; i < dataRowCount; i++) {
					//移动后行索引会发生变化
					sheet.shiftRows(9+i, 9+i, 1,false,false);
				}
				//创建显示全部数据所需要的单元格
				for (int i = 0; i < dataRowCount; i++) {
					XSSFRow row=sheet.createRow(9+i);
					//以第5行数据的高度为准
					row.setHeight(sheet.getRow(5).getHeight());
					for (int j = 0; j < columnCount; j++) {
						XSSFCell cell=row.createCell(j);
						cell.setCellStyle(textCellStyle);
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						int ji=j%17;
						if(ji==3||ji==6||ji==9||ji==12||ji==15){
							sheet.addMergedRegion(new CellRangeAddress(9+i, 9+i, j-1, j));
						}
					}
				}
				//插入数据
				for (int i = 0; i < codes.size(); i++) {
					Map<String, Object> dMap = (Map<String, Object>)dataMap.get(codes.get(i));
					List<MT_Data> mtDataList = (List<MT_Data>) dMap.get("data");
					//如果没有数据也要添加监测点编号到表头
					if(mtDataList.size()<=0){
						for(int p=0;p<pageCount;p++){
							sheet.getRow(9+i).getCell(p*17).setCellValue(codes.get(i));
						}
					}
					//将所有数据与周期的时间匹配，相同则插入，flag使遍历过的周期不再重复判断
					int pFlag=0;
					for(int j=0 ;j<mtDataList.size();j++){
						for(int p=pFlag;p<periods.size();p++){
							pFlag+=1;
							//先填每页页首的测点编号、初始读数或上次累计值
							if(p%5==0){
								if(p==0){
									sheet.getRow(9+i).getCell(0).setCellValue(codes.get(i));
									sheet.getRow(9+i).getCell(1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
								}else{
									sheet.getRow(9+i).getCell(p/5*17).setCellValue(codes.get(i));
									if(j!=0){
										sheet.getRow(9+i).getCell(p/5*17+1).setCellValue(dMap.get("originalCalVal")==null? "":dMap.get("originalCalVal").toString());
									}
								}
							}
							if(mtDataList.get(j).getCollectTime().equals(periods.get(p))){
								int pageNum=p/5+1;
								int columnNum=(pageNum-1)*17+((p+1)%5==0?14:((p+1)%5-1)*3+2);
								sheet.getRow(9+i).getCell(columnNum).setCellValue(mtDataList.get(j).getCalValue());
								sheet.getRow(9+i).getCell(columnNum+2).setCellValue(mtDataList.get(j).getGapChangeRate());
								
								break;
							}
						}
					}
				}
				//先移动行数据
				//先移动行数据
				int times=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
				if(times>1){
					for(int i=0;i<times-1;i++){
						sheet.shiftRows(33+i*34+i, 35+i*35+codes.size()-24*i+1, 11,true,false);
						sheet.setRowBreak(33+i*34+i+1);
						createSheetHead(33+i*34+i,sheet, templateSheet, pageCount, columnCount, pjName, pjAddress , pr.getP1p82(), pr.getP1p83(),
								periods, "锚杆内力监测结果表", "锚杆内力监测", "初始读数(kN)", "初始读数(kN)",fulu,"");
					}
				}
				createBlankRow( codes, sheet, times, columnCount,templateSheet);
				if(periods.size()>0&&!isDailyReport){
					creatLineChart("MT", sheet, pageCount, periods, codes, dataMap);
				}
				
			}
			//找到模板sheet
			workbook.removeSheetAt(workbook.getSheetIndex(workbook.getSheet("spTemplate")));
			workbook.removeSheetAt(workbook.getSheetIndex(workbook.getSheet("cxTemplate")));
			workbook.removeSheetAt(workbook.getSheetIndex(workbook.getSheet("stressTemplate")));
			workbook.removeSheetAt(workbook.getSheetIndex(workbook.getSheet("mtTemplate")));

			ByteArrayOutputStream out=new ByteArrayOutputStream();
			workbook.write(out);
			out.close();
			workbook.close();
			return out.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

	private boolean hasMonitorItemByNumber(List<ProjectMonitorItem> projectMonitorItems,byte number){
		for (ProjectMonitorItem projectMonitorItem : projectMonitorItems) {
			if(projectMonitorItem.getMonitorItem().getNumber()==number){
				return true;
			}
		}
		return false;
	}
	
	private void createSheetHead(int beginRowsIndex,XSSFSheet sheet,XSSFSheet templateSheet,int pageCount,int columnCount,String pjName,String pjAddress
			,String deviceName,String specification,List<Date> periods,String header,String spType,String duShu,String leiJi,int fulu,String spCodeCharStr){
		//判断是否是插入表头的行
		int beginRowIndex=beginRowsIndex;
		//备注的那行
		if(beginRowsIndex>0){
			sheet.createRow(beginRowsIndex).setHeight(templateSheet.getRow(9).getHeight());
			for (int i = 0; i < columnCount; i++) {
				//17列为一页，取余数，即第一页对应的列所在的索引
				int index=i%17;
				XSSFCell cell=sheet.getRow(beginRowsIndex).createCell(i);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(templateSheet.getRow(9).getCell(index).getCellStyle());
				cell.setCellValue((templateSheet.getRow(9).getCell(index).getStringCellValue()).replace("codeChar", spCodeCharStr));
				
			}
			for (int i = 0; i < pageCount; i++) {
				sheet.addMergedRegion(new CellRangeAddress(beginRowsIndex, beginRowsIndex, i*17, i*17+1));
				sheet.addMergedRegion(new CellRangeAddress(beginRowsIndex, beginRowsIndex, i*17+2, i*17+2+14));
			}
			beginRowIndex=beginRowsIndex+2;
		}
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
				sheet.getRow(2).getCell(i*17+12).setCellValue(spType);
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
			
//			String[] times=dataMap.get(type).get(0).getTimes();
			sheet.getRow(beginRowIndex+6).getCell(i*17+2).setCellValue((i*5)>=periods.size()?"": new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*5)));
			sheet.getRow(beginRowIndex+6).getCell(i*17+5).setCellValue((i*5+1)>=periods.size()?"":new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*5+1)));
			sheet.getRow(beginRowIndex+6).getCell(i*17+8).setCellValue((i*5+2)>=periods.size()?"":new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*5+2)));
			sheet.getRow(beginRowIndex+6).getCell(i*17+11).setCellValue((i*5+3)>=periods.size()?"":new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*5+3)));
			sheet.getRow(beginRowIndex+6).getCell(i*17+14).setCellValue((i*5+4)>=periods.size()?"":new SimpleDateFormat("yyyy.MM.dd").format(periods.get(i*5+4)));
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
	}

	private void createBlankRow(List<String> codes,XSSFSheet sheet,int times,int columnCount,XSSFSheet templateSheet){
		if(codes.size()%24>0){
			//33+(times-1)*34+codes.size()%24 
			sheet.shiftRows(33+(times-2)*35+codes.size()%24+11, 33+(times-2)*35+codes.size()%24+11,24-codes.size()%24,false,false);
			for (int i = 0; i < 24-codes.size()%24; i++) {
				int bRow=33+(times-2)*35+codes.size()%24+11;
				XSSFRow row=sheet.createRow(bRow+i);
				//以第5行数据的高度为准
				row.setHeight(sheet.getRow(5).getHeight());
				for (int j = 0; j < columnCount; j++) {
					XSSFCell cell=row.createCell(j);
					cell.setCellStyle(sheet.getRow(5).getCell(1).getCellStyle());
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(i==0){
						if(j%17==0){
							cell.setCellValue("以");
						}
						if(j%17==1){
							cell.setCellValue("下");
						}
						if(j%17==2){
							cell.setCellValue("空");
						}
						if(j%17==3){
							cell.setCellValue("白");
						}
					}
				}
			}
		}
		int size=codes.size()%24==0?codes.size()/24:codes.size()/24+1;
		for(int i=0;i<size;i++){
			sheet.getRow(33+35*i).setHeight(templateSheet.getRow(9).getHeight());
		}
	}
	
	
	
	
}
