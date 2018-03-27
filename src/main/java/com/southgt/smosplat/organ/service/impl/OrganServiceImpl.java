package com.southgt.smosplat.organ.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.impl.BaseServiceImpl;
import com.southgt.smosplat.common.util.PageCondition;
import com.southgt.smosplat.organ.dao.IAccountDao;
import com.southgt.smosplat.organ.dao.IOrganDao;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Device;
import com.southgt.smosplat.organ.entity.Organ;
import com.southgt.smosplat.organ.entity.Role;
import com.southgt.smosplat.organ.entity.Worker;
import com.southgt.smosplat.organ.service.IAccountService;
import com.southgt.smosplat.organ.service.IDeviceService;
import com.southgt.smosplat.organ.service.IOrganService;
import com.southgt.smosplat.organ.service.IRoleService;
import com.southgt.smosplat.organ.service.IWorkerService;
import com.southgt.smosplat.project.service.IProjectService;

/**
 * 机构服务层实现类
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     mohaolin       v1.0.0        create</p>
 *
 */
@Service("organService")
public class OrganServiceImpl extends BaseServiceImpl<Organ> implements IOrganService {

	@Resource
	IWorkerService workerService;
	
	@Resource
	IAccountService accountService;
	
	@Resource
	IAccountDao accountDao;
	
	@Resource
	IDeviceService deviceService;
	
	@Resource
	IProjectService projectService;
	
	@Resource
	IRoleService roleService;
	
	@Resource(name="organDao")
	@Override
	public void setDao(IBaseDao<Organ> dao) {
		super.setDao(dao);
	}
	
	@Override
	public Map<String,Object> getOrgans(Account account, PageCondition options,HttpSession session) {
		Map<String,Object> data=new HashMap<String,Object>();
		//判断是否超级管理员
		boolean isSuperAdmin=account.getLoginName().equals("superadmin")?true:false;
		List<Organ> organs=new ArrayList<Organ>();
		Organ organ;
		//如果当前账号是超级管理员，则分页获取所有的机构
		if(isSuperAdmin){
			organs=((IOrganDao)getDao()).getOrgans((options.getPageNumber()-1)*options.getPageSize(), options.getPageSize());
		}else{
			//如果当前账号不是超级管理员，则获取账号对应的机构
			organ=account.getWorker().getOrgan();;
			organs.add(organ);
			//设置当前组织到session
			session.setAttribute("currentOrganUuid", organ.getOrganUuid());
		}
		//找出每个机构对应的联系人信息一并返回
		List<Worker> workers=new ArrayList<Worker>();
		for (int i = 0; i < organs.size(); i++) {
			Worker worker=workerService.getEntity(organs.get(i).getContactWorker());
			//账号不需要人员的关联关系，否则转成json会死循环
			worker.getAccount().setWorker(null);
			workers.add(worker);
		}
		//找出每个机构对应的监督员信息一并返回
//		List<Worker> supervisors=new ArrayList<Worker>();
//		for (int i = 0; i < organs.size(); i++) {
//			Worker supervisor=workerService.getEntity(organs.get(i).getSupervisor());
//			//账号不需要人员的关联关系，否则转成json会死循环
//			supervisor.getAccount().setWorker(null);
//			supervisors.add(supervisor);
//		}
		data.put("organs", organs);
		data.put("workers", workers);
//		data.put("supervisors", supervisors);
		return data;
	}

	@Override
	public void addOrgan(Organ organ, Account account, Worker worker) throws Exception {
		//判断有没有相同名称的机构
		long organsNum=((IOrganDao)getDao()).getOrgansNumber(organ.getOrganName());
		if(organsNum>0){
			throw new Exception("已存在相同名称的部门!");
		}
		//判断有没有存在相同的账号了，账号登录名不能重复
		long accountNum=workerService.getAccountsNumber(account.getLoginName());
		if(accountNum>0){
			throw new Exception("机构联系人登录名已存在!");
		}
		//判断是否已监督员账户已存在
//		long supervisorNum=workerService.getAccountsNumber(supervisorAccountName);
//		if(supervisorNum>0){
//			throw new Exception("监督员登录名已存在!");
//		}
		//因为创建机构时机构下还没有人员，所以不用判断人员重复
		//保存机构
		getDao().saveEntity(organ);
		//设置关联关系
		worker.setAccount(account);
		account.setWorker(worker);
		worker.setOrgan(organ);
		//因为关联关系由worker管理，所以先要保存账号再保存人员
		//保存账号
		//超级管理员创建机构时创建的账号就是机构管理员角色
		Role role=roleService.getRoleByMark((byte) 1);
		account.setRole(role);
		workerService.saveAccount(account);
		//保存人员
		workerService.saveEntity(worker);
		//设置更新机构的联系人
		organ.setContactWorker(worker.getWorkerUuid());
//		//保存监督员账户
//		Worker supervisorWorker=new Worker();
//		supervisorWorker.setWorkerName(supervisorName);
//		supervisorWorker.setPhone(supervisorPhone);
//		supervisorWorker.setEmail(supervisorEmail);
//		supervisorWorker.setOrgan(organ);
//		
//		Account supervisorAccount=new Account();
//		supervisorAccount.setAccountName(supervisorAccountName);
//		supervisorAccount.setLoginName(supervisorLoginName);
//		supervisorAccount.setPassword(supervisorPassword);
//		Role supervisorRole=roleService.getRoleByMark((byte) 4);
//		supervisorAccount.setRole(supervisorRole);
//		
//		supervisorWorker.setAccount(supervisorAccount);
//		supervisorAccount.setWorker(supervisorWorker);
//		
//		workerService.saveAccount(supervisorAccount);
//		workerService.saveEntity(supervisorWorker);
//		
//		//更新机构的监督员
//		organ.setSupervisor(supervisorWorker.getWorkerUuid());
		getDao().updateEntity(organ);
	}

	@Override
	public void deleteOrgan(String organUuid) throws Exception {
		//删除机构需要检查机构下有没有相关信息，有就不允许删除
		//人员：如果有除了机构管理员以外的人，那么不允许删除机构
		//机构管理员是删除不了的，所以判断数量是否大于1就可以
		long workerNum=workerService.getWorkersNumberByOrgan(organUuid);
		if(workerNum>1){
			throw new Exception("机构下存在非机构管理员以外的人员，不能删除机构！");
		}
		//设备：如果有设备（不管自动还是手动的），不允许删除机构
		long deviceNum=deviceService.getDeviceNumberByOrgan(organUuid);
		if(deviceNum>0){
			throw new Exception("机构存在设备，不能删除机构！");
		}
		//工程：如果有工程，不允许删除
		long projectNum=projectService.getProjectNumberByOrgan(organUuid);
		if(projectNum>0){
			throw new Exception("机构下存在工程，不能删除机构！");
		}
		//机构下的数据都没有了，才可以删除机构
		//删除机构
		getDao().deleteEntity(getDao().getEntity(organUuid));
	}

	@Override
	public void updateOrgan(Organ organ1, String workerName, String phone,String loginName, String accountName, String supervisorName, String supervisorPhone) throws Exception{
		//机构重复性检测
		long organNum=((IOrganDao)getDao()).getOrgansNumberExceptSelf(organ1.getOrganName(),organ1.getOrganUuid());
		if(organNum>0){
			throw new Exception("机构名已存在！");
		}
		//查询出机构信息
		Organ organ=getDao().getEntity(organ1.getOrganUuid());
		Worker worker=workerService.getEntity(organ.getContactWorker());
		Account account=worker.getAccount();
		//判断登录名是否可用
		//如果相等，说明不修改登录名
		if(!(account.getLoginName().equals(loginName))){
			//重复性检测
			long number=accountDao.getAccountsNumber(account.getAccountUuid(), loginName);
			if(number>0){
				throw new Exception("登录名已存在！");
			}
		}
		organ.setOrganName(organ1.getOrganName());
		organ.setAdress(organ1.getAdress());
		organ.setDetactNumber(organ1.getDetactNumber());
		organ.setEmail(organ1.getEmail());
		organ.setEstablishedTime(organ1.getEstablishedTime());
		organ.setMeteringNumber(organ1.getMeteringNumber());
		organ.setOrganCode(organ1.getOrganCode());
		organ.setPostcode(organ1.getPostcode());
		organ.setRepresentative(organ1.getRepresentative());
		organ.setTechDirector(organ1.getTechDirector());
		organ.setTelephone(organ1.getTelephone());
		getDao().updateEntity(organ);
		worker.setWorkerName(workerName);
		worker.setPhone(phone);
//		worker.setEmail(email);
		workerService.updateEntity(worker);
		account.setLoginName(loginName);
		account.setAccountName(accountName);
		accountService.updateEntity(account);
		//更新监督员信息
//		Worker supervisor=workerService.getEntity(organ.getSupervisor());
//		supervisor.setWorkerName(supervisorName);
//		supervisor.setPhone(supervisorPhone);
//		workerService.updateEntity(supervisor);
	}

}
