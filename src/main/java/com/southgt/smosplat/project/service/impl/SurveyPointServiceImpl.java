package com.southgt.smosplat.project.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.project.dao.ISurveyPointDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.service.IMcuService;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISectionService;
import com.southgt.smosplat.project.service.ISurveyPointService;
import com.southgt.smosplat.project.service.IWarningService;

/**
 * 监测点服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月31日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("surveyPointService")
public class SurveyPointServiceImpl extends BaseServiceImpl<SurveyPoint_WYS> implements ISurveyPointService {
	
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IWarningService warningService;
	
	@Resource
	ISectionService sectionService;
	
	@Resource
	IMcuService mcuService;

	@Resource(name="surveyPointDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_WYS> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SurveyPoint_WYS> addSurveyPoint(Project project, String monitorItemUuid, String codeChar, int spCount,
			String warningUuid, String sectionUuid, Float originalTotalValue) {
		List<SurveyPoint_WYS> addSps=new ArrayList<SurveyPoint_WYS>();
		//根据编号获取所有该编号的监测点
		List<SurveyPoint_WYS> existedSPs=((ISurveyPointDao)getDao()).getExistedSurveyPointsByCode(project.getProjectUuid(),monitorItemUuid,codeChar);
		if(existedSPs.size()==0){
			for (int i = 0; i < spCount; i++) {
				SurveyPoint_WYS sp=new SurveyPoint_WYS();
				sp.setCode(codeChar+(i+1));
				sp.setCodeChar(codeChar);
				sp.setProject(project);
				sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
				sp.setWarning(warningService.getEntity(warningUuid));
				sp.setSection(sectionService.getEntity(sectionUuid));
				sp.setOriginalTotalValue(originalTotalValue);
				getDao().saveEntity(sp);
				addSps.add(sp);
			}
		}else{
			//测点是按序号添加的，如果中间有删除掉一些点，需要补回来
			//首先需要根据序号进行排序，因为有可能会删除掉又新增， 这样createTime的顺序就乱了，不能以createTime为准
			existedSPs.sort(new Comparator<SurveyPoint_WYS>() {
				@Override
				public int compare(SurveyPoint_WYS o1, SurveyPoint_WYS o2) {
					String codeChar=o1.getCodeChar();
					int number1=Integer.parseInt(o1.getCode().replace(codeChar, ""));
					int number2=Integer.parseInt(o2.getCode().replace(codeChar, ""));
					if(number1<=number2){
						return -1;
					}else{
						return 1;
					}
				}
			});
			
			//需要补回来的序号
			List<Integer> numbers=new ArrayList<Integer>();
			//当前最大序号
			int maxNumber=Integer.parseInt(existedSPs.get(existedSPs.size()-1).getCode().replace(codeChar, ""));
			for (int i = 0; i < maxNumber; i++) {
				//编号是从1开始的，0不用比较
				if(i==0){
					continue;
				}
				//找找看有没有序号
				boolean finded=false;
				for (int j = 0; j < existedSPs.size(); j++) {
					SurveyPoint_WYS sp=existedSPs.get(j);
					//编号中的序号
					int spNumber=Integer.parseInt(sp.getCode().replace(codeChar, ""));
					if(spNumber==i){
						finded=true;
						break;
					}
				}
				if(!finded){
					numbers.add(i);
				}
			}
			//先补点，再在后面增加点
			for (int i = 0; i < spCount; i++) {
				if(i<numbers.size()){
					//补点
					SurveyPoint_WYS sp=new SurveyPoint_WYS();
					sp.setCode(codeChar+numbers.get(i));
					sp.setCodeChar(codeChar);
					sp.setProject(project);
					sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
					sp.setWarning(warningService.getEntity(warningUuid));
					sp.setSection(sectionService.getEntity(sectionUuid));
					sp.setOriginalTotalValue(originalTotalValue);
					getDao().saveEntity(sp);
					addSps.add(sp);
				}else{
					//增加点
					maxNumber++;
					SurveyPoint_WYS sp=new SurveyPoint_WYS();
					sp.setCode(codeChar+maxNumber);
					sp.setCodeChar(codeChar);
					sp.setProject(project);
					sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
					sp.setWarning(warningService.getEntity(warningUuid));
					sp.setSection(sectionService.getEntity(sectionUuid));
					sp.setOriginalTotalValue(originalTotalValue);
					getDao().saveEntity(sp);
					addSps.add(sp);
				}
			}
		}
		return addSps;
	}

	@Override
	public List<SurveyPoint_WYS> addSurveyPoint(Project project, SurveyPoint_WYS tempSP, int spCount) {
		String codeChar=tempSP.getCodeChar();
		String monitorItemUuid=tempSP.getMonitorItem().getMonitorItemUuid();
		String warningUuid=tempSP.getWarning().getWarningUuid();
		String sectionUuid="";
		String mcuUuid="";
		if(tempSP.getSection()!=null){
		 sectionUuid=tempSP.getSection().getSectionUuid();
		};
//		if(tempSP.getMcu()!=null){
//			 mcuUuid=tempSP.getMcu().getMcuUuid();
//			}
		
		Float originalTotalValue = tempSP.getOriginalTotalValue();

		List<SurveyPoint_WYS> addSps = new ArrayList<SurveyPoint_WYS>();
		//根据编号获取所有该编号的监测点
		List<SurveyPoint_WYS> existedSPs=((ISurveyPointDao)getDao()).getExistedSurveyPointsByCode(project.getProjectUuid(),monitorItemUuid,codeChar);
		if(existedSPs.size()==0){
			for (int i = 0; i < spCount; i++) {
				SurveyPoint_WYS sp=new SurveyPoint_WYS();
				sp.setCode(codeChar+(i+1));
				sp.setCodeChar(codeChar);
				sp.setOriginalTotalValue(originalTotalValue);
//				sp.setDeep(tempSP.getDeep());
//				sp.setConstant(tempSP.getConstant());
//				sp.setOriginalModule(tempSP.getOriginalModule());
//				sp.setCc(tempSP.getCc());
//				sp.setDeviceCode(tempSP.getDeviceCode());
//				sp.setMonitorElementCode(tempSP.getMonitorElementCode());
//				sp.setFrequency(tempSP.getFrequency());
//				sp.setDeviceType(tempSP.getDeviceType());
//				sp.setDeviceCode1(tempSP.getDeviceCode1());
//				sp.setFrequency1(tempSP.getFrequency1());
//				sp.setCc1(tempSP.getCc1());
//				sp.setDeviceCode2(tempSP.getDeviceCode2());
//				sp.setFrequency2(tempSP.getFrequency2());
//				sp.setCc2(tempSP.getCc2());
//				sp.setDeviceCode3(tempSP.getDeviceCode3());
//				sp.setFrequency3(tempSP.getFrequency3());
//				sp.setCc3(tempSP.getCc3());
//				sp.setDeviceCode4(tempSP.getDeviceCode4());
//				sp.setFrequency4(tempSP.getFrequency4());
//				sp.setCc4(tempSP.getCc4());
//				sp.setDeviceCode5(tempSP.getDeviceCode5());
//				sp.setFrequency5(tempSP.getFrequency5());
//				sp.setCc5(tempSP.getCc5());
//				sp.setDeviceCode6(tempSP.getDeviceCode6());
//				sp.setFrequency6(tempSP.getFrequency6());
//				sp.setCc6(tempSP.getCc6());
//				sp.setSteelArea(tempSP.getSteelArea());
//				sp.setSectionArea(tempSP.getSectionArea());
//				sp.setEc(tempSP.getEc());
//				sp.setEs(tempSP.getEs());
//				sp.setChannelNum(tempSP.getChannelNum());
//				sp.setModuleNum(tempSP.getModuleNum());
//				
//				sp.setMcu(mcuService.getEntity(mcuUuid));
				sp.setProject(project);
				sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
				sp.setWarning(warningService.getEntity(warningUuid));
				sp.setSection(sectionService.getEntity(sectionUuid));
				getDao().saveEntity(sp);
				addSps.add(sp);
			}
		}else{
			//测点是按序号添加的，如果中间有删除掉一些点，需要补回来
			//首先需要根据序号进行排序，因为有可能会删除掉又新增， 这样createTime的顺序就乱了，不能以createTime为准
			existedSPs.sort(new Comparator<SurveyPoint_WYS>() {
				@Override
				public int compare(SurveyPoint_WYS o1, SurveyPoint_WYS o2) {
					String codeChar=o1.getCodeChar();
					int number1=Integer.parseInt(o1.getCode().replace(codeChar, ""));
					int number2=Integer.parseInt(o2.getCode().replace(codeChar, ""));
					if(number1<=number2){
						return -1;
					}else{
						return 1;
					}
				}
			});
			
			//需要补回来的序号
			List<Integer> numbers=new ArrayList<Integer>();
			//当前最大序号
			int maxNumber=Integer.parseInt(existedSPs.get(existedSPs.size()-1).getCode().replace(codeChar, ""));
			for (int i = 0; i < maxNumber; i++) {
				//编号是从1开始的，0不用比较
				if(i==0){
					continue;
				}
				//找找看有没有序号
				boolean finded=false;
				for (int j = 0; j < existedSPs.size(); j++) {
					SurveyPoint_WYS sp=existedSPs.get(j);
					//编号中的序号
					int spNumber=Integer.parseInt(sp.getCode().replace(codeChar, ""));
					if(spNumber==i){
						finded=true;
						break;
					}
				}
				if(!finded){
					numbers.add(i);
				}
			}
			//先补点，再在后面增加点
			for (int i = 0; i < spCount; i++) {
				if(i<numbers.size()){
					//补点
					SurveyPoint_WYS sp=new SurveyPoint_WYS();
					sp.setCode(codeChar+numbers.get(i));
					sp.setCodeChar(codeChar);
//					sp.setDeep(tempSP.getDeep());
//					sp.setConstant(tempSP.getConstant());
//					sp.setOriginalModule(tempSP.getOriginalModule());
//					sp.setCc(tempSP.getCc());
//					sp.setDeviceCode(tempSP.getDeviceCode());
//					sp.setMonitorElementCode(tempSP.getMonitorElementCode());
//					sp.setFrequency(tempSP.getFrequency());
//					sp.setDeviceType(tempSP.getDeviceType());
//					sp.setDeviceCode1(tempSP.getDeviceCode1());
//					sp.setFrequency1(tempSP.getFrequency1());
//					sp.setCc1(tempSP.getCc1());
//					sp.setDeviceCode2(tempSP.getDeviceCode2());
//					sp.setFrequency2(tempSP.getFrequency2());
//					sp.setCc2(tempSP.getCc2());
//					sp.setDeviceCode3(tempSP.getDeviceCode3());
//					sp.setFrequency3(tempSP.getFrequency3());
//					sp.setCc3(tempSP.getCc3());
//					sp.setDeviceCode4(tempSP.getDeviceCode4());
//					sp.setFrequency4(tempSP.getFrequency4());
//					sp.setCc4(tempSP.getCc4());
//					sp.setDeviceCode5(tempSP.getDeviceCode5());
//					sp.setFrequency5(tempSP.getFrequency5());
//					sp.setCc5(tempSP.getCc5());
//					sp.setDeviceCode6(tempSP.getDeviceCode6());
//					sp.setFrequency6(tempSP.getFrequency6());
//					sp.setCc6(tempSP.getCc6());
//					sp.setSteelArea(tempSP.getSteelArea());
//					sp.setSectionArea(tempSP.getSectionArea());
//					sp.setEc(tempSP.getEc());
//					sp.setEs(tempSP.getEs());
//					sp.setChannelNum(tempSP.getChannelNum());
//					sp.setModuleNum(tempSP.getModuleNum());
//					
//					sp.setMcu(mcuService.getEntity(mcuUuid));
					sp.setProject(project);
					sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
					sp.setWarning(warningService.getEntity(warningUuid));
					sp.setSection(sectionService.getEntity(sectionUuid));
					sp.setOriginalTotalValue(originalTotalValue);
					getDao().saveEntity(sp);
					addSps.add(sp);
				}else{
					//增加点
					maxNumber++;
					SurveyPoint_WYS sp=new SurveyPoint_WYS();
					sp.setCode(codeChar+maxNumber);
					sp.setCodeChar(codeChar);
//					sp.setDeep(tempSP.getDeep());
//					sp.setConstant(tempSP.getConstant());
//					sp.setOriginalModule(tempSP.getOriginalModule());
//					sp.setCc(tempSP.getCc());
//					sp.setDeviceCode(tempSP.getDeviceCode());
//					sp.setMonitorElementCode(tempSP.getMonitorElementCode());
//					sp.setFrequency(tempSP.getFrequency());
//					sp.setDeviceType(tempSP.getDeviceType());
//					sp.setDeviceCode1(tempSP.getDeviceCode1());
//					sp.setFrequency1(tempSP.getFrequency1());
//					sp.setCc1(tempSP.getCc1());
//					sp.setDeviceCode2(tempSP.getDeviceCode2());
//					sp.setFrequency2(tempSP.getFrequency2());
//					sp.setCc2(tempSP.getCc2());
//					sp.setDeviceCode3(tempSP.getDeviceCode3());
//					sp.setFrequency3(tempSP.getFrequency3());
//					sp.setCc3(tempSP.getCc3());
//					sp.setDeviceCode4(tempSP.getDeviceCode4());
//					sp.setFrequency4(tempSP.getFrequency4());
//					sp.setCc4(tempSP.getCc4());
//					sp.setDeviceCode5(tempSP.getDeviceCode5());
//					sp.setFrequency5(tempSP.getFrequency5());
//					sp.setCc5(tempSP.getCc5());
//					sp.setDeviceCode6(tempSP.getDeviceCode6());
//					sp.setFrequency6(tempSP.getFrequency6());
//					sp.setCc6(tempSP.getCc6());
//					sp.setSteelArea(tempSP.getSteelArea());
//					sp.setSectionArea(tempSP.getSectionArea());
//					sp.setEc(tempSP.getEc());
//					sp.setEs(tempSP.getEs());
//					sp.setChannelNum(tempSP.getChannelNum());
//					sp.setModuleNum(tempSP.getModuleNum());
//					
//					sp.setMcu(mcuService.getEntity(mcuUuid));
					sp.setProject(project);
					sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
					sp.setWarning(warningService.getEntity(warningUuid));
					sp.setSection(sectionService.getEntity(sectionUuid));
					sp.setOriginalTotalValue(originalTotalValue);
					getDao().saveEntity(sp);
					addSps.add(sp);
				}
			}
		}
		return addSps;
	}
	
	@Override
	public List<SurveyPoint_WYS> getSurveyPoints(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_WYS> sps=((ISurveyPointDao)getDao()).getSurveyPoints(projectUuid,monitorItemUuid);
		//需要按照测点编号进行排序
		sps.sort(new Comparator<SurveyPoint_WYS>() {
			@Override
			public int compare(SurveyPoint_WYS sp1, SurveyPoint_WYS sp2) {  
                int compareCode = sp1.getCodeChar().compareTo(sp2.getCodeChar());  
                if (compareCode == 0) {
                	int number1=Integer.parseInt(sp1.getCode().replace(sp1.getCodeChar(), ""));
    				int number2=Integer.parseInt(sp2.getCode().replace(sp2.getCodeChar(), ""));
                    return (number1 == number2 ? 0 : (number1 > number2 ? 1 : -1));  
                }  
                return compareCode;  
            }
		});
		return sps;
	}

	@Override
	public void updateSurveyPoint(Project project, SurveyPoint_WYS surveyPoint) {
		surveyPoint.setProject(project);
		getDao().updateEntity(surveyPoint);
	}

	@Override
	public void deleteSurveyPoint(String surveyPointUuid) {
		SurveyPoint_WYS sp=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????
		
		getDao().deleteEntity(sp);
	}

	@Override
	public long getSPNumbersByWarning(String warningUuid) {
		return ((ISurveyPointDao)getDao()).getSPNumbersByWarning(warningUuid);
	}

	@Override
	public long getSPNumbersBySection(String sectionUuid) {
		return ((ISurveyPointDao)getDao()).getSPNumbersBySection(sectionUuid);
	}


}
