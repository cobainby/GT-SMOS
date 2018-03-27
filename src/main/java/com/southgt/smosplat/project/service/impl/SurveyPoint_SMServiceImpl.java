package com.southgt.smosplat.project.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.service.ISMService;
import com.southgt.smosplat.project.dao.ISurveyPoint_SMDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_SM;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_SMService;
import com.southgt.smosplat.project.service.IWarningService;
@Service("sp_SMService")
public class SurveyPoint_SMServiceImpl extends BaseServiceImpl<SurveyPoint_SM> implements ISurveyPoint_SMService{

	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IWarningService warningService;
	
	@Resource
	ISMService smService;
	
	@Resource(name="sp_SMDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_SM> dao) {
		super.setDao(dao);
	}
	@Override
	public List<SurveyPoint_SM> getSP_SMs(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_SM> sps=((ISurveyPoint_SMDao)getDao()).getSurveyPoint_SMs(projectUuid,monitorItemUuid);
		return sps;
	}
	
	@Override
	public List<SurveyPoint_SM> getSP_SMs(String projectUuid) {
		List<SurveyPoint_SM> sps=((ISurveyPoint_SMDao)getDao()).getSurveyPoint_SMs(projectUuid);
		return sps;
	}
	
	@Override
	public List<SurveyPoint_SM> addSP_SM(Project project, SurveyPoint_SM tempSP, int spCount, int beginNum)
			throws Exception {
		String codeChar=tempSP.getCodeChar();
		String monitorItemUuid=tempSP.getMonitorItem().getMonitorItemUuid();
		String warningUuid=tempSP.getWarning().getWarningUuid();
		
		Float originalTotalValue = tempSP.getOriginalTotalValue();

		List<SurveyPoint_SM> addSps = new ArrayList<SurveyPoint_SM>();
		//根据编号获取所有该编号的监测点		
		List<SurveyPoint_SM> existedSPs=((ISurveyPoint_SMDao)getDao()).getExistedSurveyPoint_SMsByCode(project.getProjectUuid(),monitorItemUuid,codeChar);
		
		if(existedSPs.size()==0){
			for (int i = 0; i < spCount; i++) {
				int number=beginNum+i;
				SurveyPoint_SM sp=new SurveyPoint_SM();
				sp.setCode(codeChar+number);
				sp.setCodeChar(codeChar);
				sp.setOriginalTotalValue(originalTotalValue);
				sp.setDeviceType(tempSP.getDeviceType());
				
				sp.setProject(project);
				sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
				sp.setWarning(warningService.getEntity(warningUuid));
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
					SurveyPoint_SM sp=new SurveyPoint_SM();
					sp.setCode(codeChar+numbers.get(i));
					sp.setCodeChar(codeChar);
					sp.setOriginalTotalValue(originalTotalValue);
					sp.setDeviceType(tempSP.getDeviceType());
					
					sp.setProject(project);
					sp.setMonitorItem(monitorItemService.getEntity(monitorItemUuid));
					sp.setWarning(warningService.getEntity(warningUuid));
					getDao().saveEntity(sp);
					addSps.add(sp);
				}
			}
		}
		return addSps;
	}
	@Override
	public void updateSP_SM(Project project, SurveyPoint_SM surveyPoint) throws Exception {
		long num=((ISurveyPoint_SMDao)getDao()).getSMNumByCodeExceptSelf(project.getProjectUuid(), surveyPoint.getMonitorItem().getMonitorItemUuid(), surveyPoint.getCodeChar(), surveyPoint.getCode(), surveyPoint.getSurveyPointUuid());
		if(num>0){
			throw new Exception("存在相同编号的测点");
		}
		surveyPoint.setProject(project);
		getDao().updateEntity(surveyPoint);
		
	}
	@Override
	public void deleteSP_SM(String surveyPointUuid) {
		SurveyPoint_SM sp=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????
		smService.deleteSMDataBySP(surveyPointUuid);
		getDao().deleteEntity(sp);
		
	}

}
