package com.southgt.smosplat.organ.service.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.organ.dao.IAccountDao;
import com.southgt.smosplat.organ.dao.IAccountProjectDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.AccountProject;
import com.southgt.smosplat.organ.service.IAccountProjectService;
import com.southgt.smosplat.project.dao.IProjectDao;
import com.southgt.smosplat.project.entity.Project;

/**
 * 账户和工程关联关系服务接口实现
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年7月4日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("accountProjectService")
public class AccountProjectServiceImpl extends BaseServiceImpl<AccountProject> implements IAccountProjectService {

	@Resource
	IAccountDao accountDao;
	
	@Resource
	IProjectDao projectDao;
	
	@Resource(name="accountProjectDao")
	@Override
	public void setDao(IBaseDao<AccountProject> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Project> getProjectsByAccount(String accountUuid) {
		List<AccountProject> entities=((IAccountProjectDao)getDao()).getEntitiesByAccount(accountUuid);
		List<Project> prjs=new ArrayList<Project>();
		for (AccountProject accountProject : entities) {
			Project p=accountProject.getProject();
			if(p.getAvailable()==0){
				prjs.add(p);
			}
		}
		return prjs;
	}

	@Override
	public void updateProjectForAccount(String accountUuid, String projectUuids) {
		//获得已有的可查看工程
		List<AccountProject> accountProjects=((IAccountProjectDao)getDao()).getProjectsByAccount(accountUuid);
		String[] projectUuidsArray=projectUuids.split(",");
		//根据已有的集合判断，如果已有集合没有该项目，先从已有的删掉
		for (int i = 0; i < accountProjects.size(); i++) {
			boolean finded=false;
			for (int j = 0; j < projectUuidsArray.length; j++) {
				if(accountProjects.get(i).getProject().getProjectUuid().equals(projectUuidsArray[j])){
					//找到后跳出循环
					finded=true;
					break;
				}
			}
			if(!finded){
				//没有找到，从已有中删除
				((IAccountProjectDao)getDao()).deleteEntity(accountProjects.get(i));
			}
		}
		//根据已有集合，如果已有的集合中没有，则增加记录
		for (int i = 0; i < projectUuidsArray.length; i++) {
			boolean finded=false;
			for (int j = 0; j < accountProjects.size(); j++) {
				//如果在已有的权限中找到，则跳过
				if(projectUuidsArray[i].equals(accountProjects.get(j).getProject().getProjectUuid())){
					finded=true;
					break;
				}
			}
			//没有找到，则增加记录
			if(!finded){
				Account account=accountDao.getEntity(accountUuid);
				Project project=projectDao.getEntity(projectUuidsArray[i]);
				AccountProject accountProject=new AccountProject(account, project);
				((IAccountProjectDao)getDao()).saveEntity(accountProject);
			}
		}
	}


}
