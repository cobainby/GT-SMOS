package com.southgt.smosplat.project.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.service.ICXService;
import com.southgt.smosplat.data.service.IQXService;
import com.southgt.smosplat.project.dao.ISurveyPoint_QXDao;
import com.southgt.smosplat.project.dao.ISurveyPoint_QXDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_QX;
import com.southgt.smosplat.project.entity.SurveyPoint_QX;
import com.southgt.smosplat.project.entity.SurveyPoint_QX;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_QXService;
import com.southgt.smosplat.project.service.IWarningService;
@Service("sp_QXService")
public class SurveyPoint_QXServiceImpl extends BaseServiceImpl<SurveyPoint_QX> implements ISurveyPoint_QXService{
	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IWarningService warningService;
	
	@Resource
	IQXService qxService;
	
	@Resource(name="sp_QXDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_QX> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SurveyPoint_QX> addSP_QX(Project project, SurveyPoint_QX tempSP, int spCount, int beginNum)
			throws Exception {
		String codeChar=tempSP.getCodeChar();
		String monitorItemUuid=tempSP.getMonitorItem().getMonitorItemUuid();
		String warningUuid=tempSP.getWarning().getWarningUuid();
		List<SurveyPoint_QX> addSps = new ArrayList<SurveyPoint_QX>();
		//根据编号获取所有该编号的监测点
		List<SurveyPoint_QX> existedSPs=((ISurveyPoint_QXDao)getDao()).getExistedSurveyPoint_QXsByCode(project.getProjectUuid(),monitorItemUuid,codeChar);
		if(existedSPs.size()==0){
			for (int i = 0; i < spCount; i++) {
				int number=beginNum+i;
				SurveyPoint_QX sp=new SurveyPoint_QX();
				sp.setCode(codeChar+number);
				sp.setCodeChar(codeChar);
				sp.setDeep(tempSP.getDeep());
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
					SurveyPoint_QX sp=new SurveyPoint_QX();
					sp.setCode(codeChar+numbers.get(i));
					sp.setCodeChar(codeChar);
					sp.setDeep(tempSP.getDeep());
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
	public List<SurveyPoint_QX> getSP_QXs(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_QX> sps=((ISurveyPoint_QXDao)getDao()).getSurveyPoint_QXs(projectUuid,monitorItemUuid);
		//需要按照测点编号进行排序
		sps.sort(new Comparator<SurveyPoint_QX>() {
			@Override
			public int compare(SurveyPoint_QX sp1, SurveyPoint_QX sp2) {  
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
	public void updateSurveyPoint_QX(Project project, SurveyPoint_QX surveyPoint) throws Exception {
		long num=((ISurveyPoint_QXDao)getDao()).getQXNumByCodeExceptSelf(project.getProjectUuid(), surveyPoint.getMonitorItem().getMonitorItemUuid(), surveyPoint.getCodeChar(), surveyPoint.getCode(), surveyPoint.getSurveyPointUuid());
		if(num>0){
			throw new Exception("已存在相同编号的监测点");
		}
		surveyPoint.setProject(project);
		getDao().updateEntity(surveyPoint);
		
	}

	@Override
	public void deleteSurveyPoint_QX(String surveyPointUuid) {
		SurveyPoint_QX sp=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????
		qxService.deleteQXDataBysurveyPoint(surveyPointUuid);
		getDao().deleteEntity(sp);
		
	}

}
