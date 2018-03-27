package com.southgt.smosplat.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.service.IZCService;
import com.southgt.smosplat.project.dao.ISurveyPoint_ZCDao;
import com.southgt.smosplat.project.entity.SurveyPoint_ZC;
import com.southgt.smosplat.project.entity.Warning;
import com.southgt.smosplat.project.service.IStressService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZCService;
import com.southgt.smosplat.project.service.IWarningService;

@Service("sp_ZCService")
public class SurveyPoint_ZCServiceImpl extends BaseServiceImpl<SurveyPoint_ZC> implements ISurveyPoint_ZCService {

	@Resource(name="sp_ZCDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_ZC> dao) {
		super.setDao(dao);
	}

	@Resource
	IWarningService warningService;
	
	@Resource
	IZCService zcService;
	
	@Resource
	IStressService stressService;
	
	@Override
	public List<SurveyPoint_ZC> getSP_ZCs(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_ZC> sp_ZCList=((ISurveyPoint_ZCDao)getDao()).getSurveyPoint_ZCs(projectUuid, monitorItemUuid);
		return sp_ZCList;
	
	}

	@Override
	public void addSP_zc(SurveyPoint_ZC sp_ZC) throws Exception {
		Warning w=warningService.getEntity(sp_ZC.getWarning().getWarningUuid());
		sp_ZC.setWarning(w);
		long num=((ISurveyPoint_ZCDao)getDao()).getSP_zcNumByCode(sp_ZC.getProject().getProjectUuid(), sp_ZC.getMonitorItem().getMonitorItemUuid(), sp_ZC.getCode());
		if(num>0){
			throw new Exception("已存在相同编号的轴力");
		}else{
			 ((ISurveyPoint_ZCDao)getDao()).saveEntity(sp_ZC);
		}
	}

	@Override
	public void updateSP_zc(SurveyPoint_ZC sp_ZC) throws Exception {
		long num=((ISurveyPoint_ZCDao)getDao()).getSP_zcNumByCodeExceptSelf(sp_ZC.getProject().getProjectUuid(), sp_ZC.getMonitorItem().getMonitorItemUuid(), sp_ZC.getCode(), sp_ZC.getSurveyPointUuid());
		if(num>0){
			throw new Exception("已存在相同编号的轴力");
		}else{
			 ((ISurveyPoint_ZCDao)getDao()).updateEntity(sp_ZC);
		}
	}

	@Override
	public void deleteSP_zc(String  surveyPointUuid) {
		SurveyPoint_ZC zc=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????
		zcService.deleteZCDataBySurveyPoint(surveyPointUuid);
		stressService.deleteStressBySurveyPoint(surveyPointUuid);
		
		getDao().deleteEntity(zc);
	
		
	}
}
