package com.southgt.smosplat.organ.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.dao.IAccountDao;
import com.southgt.smosplat.organ.dao.IOrganDao;
import com.southgt.smosplat.organ.dao.IWorkerDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Role;
import com.southgt.smosplat.organ.entity.Worker;
import com.southgt.smosplat.organ.service.IRoleService;
import com.southgt.smosplat.organ.service.IWorkerService;

/**
 * 人员服务实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月2日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("workerService")
public class WorkerServiceImpl extends BaseServiceImpl<Worker> implements IWorkerService{
	
	@Resource
	IOrganDao organDao;
	
	@Resource
	IAccountDao accountDao;
	
	@Resource
	IRoleService roleService;

	@Resource(name="workerDao")
	@Override
	public void setDao(IBaseDao<Worker> dao) {
		super.setDao(dao);
	}
	
	@Override
	public Account validateAccount(String loginName, String password) {
		return accountDao.getAccount(loginName,password);
	}

	@Override
	public long getAccountsNumber(String loginName) {
		return accountDao.getAccountsNumber(loginName);
	}
	
	@Override
	public long getAccountsNumber(String accountUuid, String loginName) {
		return accountDao.getAccountsNumber(accountUuid,loginName);
	}

	@Override
	public void deleteWorkersByOrgan(String organUuid) {
		List<Worker> workers=getWorkersByOrgan(organUuid);
		for (Worker worker : workers) {
			if(worker.getAccount()!=null){
				accountDao.deleteEntity(worker.getAccount());
			}
			deleteEntity(worker);
		}
	}

	@Override
	public List<Worker> getWorkers(PageCondition options) {
		List<Worker> workers=((IWorkerDao)getDao()).getWorkers((options.getPageNumber()-1)*options.getPageSize(), options.getPageSize());
		return workers;
	}

	@Override
	public List<Worker> getWorkers(PageCondition options, String organUuid) {
		List<Worker> workers=((IWorkerDao)getDao()).getWorkers((options.getPageNumber()-1)*options.getPageSize(), options.getPageSize(),organUuid);
		return workers;
	}
	
	@Override
	public List<Worker> getWorkersByOrgan(String organUuid) {
		return ((IWorkerDao)getDao()).getWorkersByOrgan(organUuid);
	}

	@Override
	public Worker getWorkerByAccount(String accountUuid) {
		return ((IWorkerDao)getDao()).getWorkerByAccount(accountUuid);
	}
	
	@Override
	public void addWorker(Worker worker, Boolean hasAccount, Account account, String curOrganUuid, Account loginAccount) throws Exception {
		//增加人员，同时增加关联账号
		if(hasAccount){
			// 人员的账户登录名在整个系统中不能存在重复
			long number=accountDao.getAccountsNumber(account.getLoginName());
			if(number>0){
				throw new Exception("账户登录名已存在！");
			}
			//判断当前登录账号的级别，不同级别创建的人员的账号级别不一样
			byte roleMark=loginAccount.getRole().getMark();
			Role role=null;
			if(roleMark==0||roleMark==1){
				//在人员管理界面，超级管理员和机构管理员创建的账户对应的角色都是普通账户
				role=roleService.getRoleByMark((byte) 2);
			}else if(roleMark==2){
				//普通账户创建的账户对应的角色是访客账户
				role=roleService.getRoleByMark((byte) 3);
			}
			account.setRole(role);
			//设置人员的机构、账户的关联关系
			worker.setOrgan(organDao.getEntity(curOrganUuid));
			worker.setAccount(account);
			account.setWorker(worker);
			account.setCreatorUuid(loginAccount.getAccountUuid());
			accountDao.saveEntity(account);
			((IWorkerDao)getDao()).saveEntity(worker);
		}else{//仅增加人员，不增加账户
			worker.setOrgan(organDao.getEntity(curOrganUuid));
			((IWorkerDao)getDao()).saveEntity(worker);
		}
	}

	@Override
	public void saveAccount(Account account) {
		accountDao.saveEntity(account);
	}

	@Override
	public void updateAccount(Account account) {
		accountDao.updateEntity(account);
	}

	@Override
	public void deleteWorker(String workerUuid) throws Exception {
		//不能删除机构的联系人
		long number=organDao.getOrgansNumberByContact(workerUuid);
		if(number>0){
			throw new Exception("机构联系人不能删除！");
		}
		Worker worker=getDao().getEntity(workerUuid);
		getDao().deleteEntity(worker);
	}

	@Override
	public void updateWorker(Worker worker) {
		Worker worker1=getDao().getEntity(worker.getWorkerUuid());
		worker1.setWorkerName(worker.getWorkerName());
		worker1.setPhone(worker.getPhone());
		worker1.setEmail(worker.getEmail());
		worker1.setPaperID(worker.getPaperID());
		worker1.setCharacterId(worker.getCharacterId());
		worker1.setDuty(worker.getDuty());
		worker1.setEducation(worker.getEducation());
		worker1.setGraduationInfo(worker.getGraduationInfo());
		worker1.setIdNumber(worker.getIdNumber());
		worker1.setRegisterdPaperId(worker.getRegisterdPaperId());
		worker1.setRegisterdType(worker.getRegisterdType());
		worker1.setResume(worker.getResume());
		worker1.setSex(worker.getSex());
		worker1.setJobTitle(worker.getJobTitle());
		worker1.setTitleMajor(worker.getTitleMajor());
		if(worker.getWorkerType()!=null){
			worker1.setWorkerType(worker.getWorkerType());
		}
		worker1.setWorkYear(worker.getWorkYear());
		getDao().updateEntity(worker1);
	}

	@Override
	public void updateAccount(String accountUuid, String accountName, String loginName, Boolean resetPassword, String password) throws Exception {
		Account account=accountDao.getEntity(accountUuid);
		//账户登录名重复性检测
		long number=getAccountsNumber(accountUuid,loginName);
		if(number>0){
			throw new Exception("账户登录名已存在！");
		}
		account.setAccountName(accountName);
		account.setLoginName(loginName);
		if(resetPassword){
			account.setPassword(password);
		}
		accountDao.updateEntity(account);
	}

	@Override
	public long getWorkersNumberByOrgan(String organUuid) {
		return ((IWorkerDao)getDao()).getWorkersNumberByOrgan(organUuid);
	}

	@Override
	public Account getAccountById(String accountUuid) {
		return accountDao.getEntity(accountUuid);
	}


}
