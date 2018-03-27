package com.southgt.smosplat.project.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.service.IWYSCoordDataService;
import com.southgt.smosplat.data.service.IWYSService;
import com.southgt.smosplat.project.dao.ISurveyPointDao;
import com.southgt.smosplat.project.dao.ISurveyPoint_WYDDao;
import com.southgt.smosplat.project.dao.ISurveyPoint_WYSDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint;
import com.southgt.smosplat.project.entity.SurveyPoint_LZ;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISectionService;
import com.southgt.smosplat.project.service.ISurveyPoint_WYSService;
import com.southgt.smosplat.project.service.IWarningService;

@Service("sp_WYSService")
public class SurveyPoint_WYSServiceImpl extends BaseServiceImpl<SurveyPoint_WYS> implements ISurveyPoint_WYSService {

	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IWarningService warningService;
	
	@Resource
	ISectionService sectionService;
	
	@Resource
	IWYSCoordDataService wysCoordDataService;
	
	@Resource
	IWYSService wysService;
	
	@Resource(name="sp_WYSDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_WYS> dao) {
		super.setDao(dao);
	}
	
	@Override
	public List<SurveyPoint_WYS> addSP_WYS(Project project, SurveyPoint_WYS tempSP, int spCount,int beginNum) throws Exception {
		String codeChar=tempSP.getCodeChar();
		String monitorItemUuid=tempSP.getMonitorItem().getMonitorItemUuid();
		String warningUuid=tempSP.getWarning().getWarningUuid();
		String sectionUuid="";
		if(tempSP.getSection()!=null){
		 sectionUuid=tempSP.getSection().getSectionUuid();
		};
		Float originalTotalValue = tempSP.getOriginalTotalValue();

		List<SurveyPoint_WYS> addSps = new ArrayList<SurveyPoint_WYS>();
		//根据编号获取所有该编号的监测点
		List<SurveyPoint_WYS> existedSPs=((ISurveyPoint_WYSDao)getDao()).getExistedSurveyPoint_WYSsByCode(project.getProjectUuid(), monitorItemUuid, codeChar);
		if(existedSPs.size()==0){
			for (int i = 0; i < spCount; i++) {
				SurveyPoint_WYS sp=new SurveyPoint_WYS();
				int number=beginNum+i;
				sp.setCode(codeChar+number);
				sp.setCodeChar(codeChar);
				sp.setOriginalTotalValue(originalTotalValue);
				sp.setProject(project);
				sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
				sp.setWarning(warningService.getEntity(warningUuid));
				sp.setSection(sectionService.getEntity(sectionUuid));
				getDao().saveEntity(sp);
				addSps.add(sp);
			}
		}else{

			//获取已经存在的测点号数
			List<Integer> existedNumbers=new ArrayList<Integer>();
			for(int i=0;i<existedSPs.size();i++){
				existedNumbers.add(Integer.parseInt(existedSPs.get(i).getCode().replace(codeChar, "")));
			}
			//判断输入的连续序号数与已经存在的序号是否有冲突
			List<Integer> numbers=new ArrayList<Integer>();
			String tips="";
			for(int i=0;i<spCount;i++){
				int number=beginNum+i;
				if(existedNumbers.contains(number)){
					if(i<spCount-1){
						tips=tips+tempSP.getCodeChar()+number+"、";
					}else{
						tips=tips+tempSP.getCodeChar()+number;
					}
				}else{
					numbers.add(beginNum+i);
				}
			}
			//存在错误点号则组织返回错误的点号，否则逐一添加
			if(!tips.equals("")){
				throw new Exception("添加失败！已存在测点编号为:"+tips+"的测点,请重新确认");
			}else{
				for (int i = 0; i < numbers.size(); i++) {
					SurveyPoint_WYS sp=new SurveyPoint_WYS();
					sp.setCode(codeChar+numbers.get(i));
					sp.setCodeChar(codeChar);
					sp.setOriginalTotalValue(originalTotalValue);
					sp.setProject(project);
					sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
					sp.setWarning(warningService.getEntity(warningUuid));
					sp.setSection(sectionService.getEntity(sectionUuid));
					getDao().saveEntity(sp);
					addSps.add(sp);
				}
			}
		}
		return addSps;
	}

	@Override
	public List<SurveyPoint_WYS> getSP_WYSs(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_WYS> sps=((ISurveyPoint_WYSDao)getDao()).getSurveyPoint_WYSs(projectUuid,monitorItemUuid);
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
	public List<SurveyPoint_WYS> getSP_WYSs(String projectUuid) {
		List<SurveyPoint_WYS> sps=((ISurveyPoint_WYSDao)getDao()).getSurveyPoint_WYSs(projectUuid);
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
	public void updateSP_WYS(Project project, SurveyPoint_WYS surveyPoint) throws Exception {
		long num=((ISurveyPoint_WYSDao)getDao()).getWYSNumByCodeExceptSelf(project.getProjectUuid(), surveyPoint.getMonitorItem().getMonitorItemUuid(),
				 surveyPoint.getCode(), surveyPoint.getSurveyPointUuid());
		if(num>0){
			throw new Exception("存在相同测点编号的测点");
		}else{
			surveyPoint.setProject(project);
			getDao().updateEntity(surveyPoint);
		}
	}

	@Override
	public void deleteSP_WYS(String surveyPointUuid) {
		SurveyPoint_WYS sp=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????删除原始数据和解析数据
		wysCoordDataService.deleteWYSLevelDataBySurveyPoint(surveyPointUuid);
		wysService.deleteWYSLevelDataBySurveyPoint(surveyPointUuid);
		
		getDao().deleteEntity(sp);
	}


}
