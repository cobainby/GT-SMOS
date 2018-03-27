package com.southgt.smosplat.project.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.data.service.IZGDService;
import com.southgt.smosplat.project.dao.ISurveyPoint_ZGDDao;
import com.southgt.smosplat.project.entity.Project;
import com.southgt.smosplat.project.entity.SurveyPoint_WYS;
import com.southgt.smosplat.project.entity.SurveyPoint_ZGD;
import com.southgt.smosplat.project.service.IMonitorItemService;
import com.southgt.smosplat.project.service.ISurveyPoint_ZGDService;
import com.southgt.smosplat.project.service.IWarningService;

@Service("sp_ZGDService")
public class SurveyPoint_ZGDServiceImpl extends BaseServiceImpl<SurveyPoint_ZGD> implements ISurveyPoint_ZGDService {

	@Resource
	IMonitorItemService monitorItemService;
	
	@Resource
	IWarningService warningService;
	
	@Resource
	IZGDService zgdService;
	
	@Resource(name="sp_ZGDDao")
	@Override
	public void setDao(IBaseDao<SurveyPoint_ZGD> dao) {
		super.setDao(dao);
	}
	
	@Override
	public List<SurveyPoint_ZGD> addSP_ZGD(Project project, SurveyPoint_ZGD tempSP, int spCount,int beginNum)throws Exception {
		String codeChar=tempSP.getCodeChar();
		String monitorItemUuid=tempSP.getMonitorItem().getMonitorItemUuid();
		String warningUuid=tempSP.getWarning().getWarningUuid();
	
		Float originalTotalValue = tempSP.getOriginalTotalValue();

		List<SurveyPoint_ZGD> addSps = new ArrayList<SurveyPoint_ZGD>();
		//根据编号获取所有该编号的监测点
		List<SurveyPoint_ZGD> existedSPs=((ISurveyPoint_ZGDDao)getDao()).getExistedSurveyPoint_ZGDsByCode(project.getProjectUuid(),monitorItemUuid,codeChar);
		if(existedSPs.size()==0){
			for (int i = 0; i < spCount; i++) {
				SurveyPoint_ZGD sp=new SurveyPoint_ZGD();
				int number=beginNum+i;
				sp.setCode(codeChar+number);
				sp.setCodeChar(codeChar);
				sp.setOriginalTotalValue(originalTotalValue);
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
					SurveyPoint_ZGD sp=new SurveyPoint_ZGD();
					sp.setCode(codeChar+numbers.get(i));
					sp.setCodeChar(codeChar);
					sp.setOriginalTotalValue(originalTotalValue);
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
	public List<SurveyPoint_ZGD> getSP_ZGDs(String projectUuid, String monitorItemUuid) {
		List<SurveyPoint_ZGD> sps=((ISurveyPoint_ZGDDao)getDao()).getSurveyPoint_ZGDs(projectUuid,monitorItemUuid);
		//需要按照测点编号进行排序
		sps.sort(new Comparator<SurveyPoint_ZGD>() {
			@Override
			public int compare(SurveyPoint_ZGD sp1, SurveyPoint_ZGD sp2) {  
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
	public List<SurveyPoint_ZGD> getSP_ZGDs(String projectUuid) {
		List<SurveyPoint_ZGD> sps=((ISurveyPoint_ZGDDao)getDao()).getSurveyPoint_ZGDs(projectUuid);
		//需要按照测点编号进行排序
		sps.sort(new Comparator<SurveyPoint_ZGD>() {
			@Override
			public int compare(SurveyPoint_ZGD sp1, SurveyPoint_ZGD sp2) {  
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
	public void updateSP_ZGD(Project project, SurveyPoint_ZGD surveyPoint) throws Exception {
		long num=((ISurveyPoint_ZGDDao)getDao()).getZGDNumByCodeExceptSelf(project.getProjectUuid(), surveyPoint.getMonitorItem().getMonitorItemUuid(),
				surveyPoint.getCode(), surveyPoint.getSurveyPointUuid());
		if(num>0){
			throw new Exception("已存在相同测点编号的测点");
		}
		surveyPoint.setProject(project);
		getDao().updateEntity(surveyPoint);
	}

	@Override
	public void deleteSP_ZGD(String surveyPointUuid) {
		SurveyPoint_ZGD sp=getDao().getEntity(surveyPointUuid);
		//如果有数据，则先要删除数据????????????????
		zgdService.deleteZGDDataBySurveyPoint(surveyPointUuid);
		
		getDao().deleteEntity(sp);
	}


}
