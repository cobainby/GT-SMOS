package com.southgt.smosplat.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.southgt.smosplat.common.dao.IBaseDao;
import com.southgt.smosplat.common.service.IBaseService;

public abstract class BaseServiceImpl<T> implements IBaseService<T> {
	
	private IBaseDao<T> dao;

	public IBaseDao<T> getDao() {
		return dao;
	}

	@Resource//注入dao
	public void setDao(IBaseDao<T> dao) {
		this.dao = dao;
	}

	@Override
	public void saveEntity(T t){
		dao.saveEntity(t);
	}

	@Override
	public void updateEntity(T t) {
		dao.updateEntity(t);
	}

	@Override
	public void saveOrUpdateEntity(T t) {
		dao.saveOrUpdateEntity(t);
	}

	@Override
	public void deleteEntity(T t) {
		dao.deleteEntity(t);
	}

	@Override
	public void batchEntityByHQL(String hql, Object... objects) {
		dao.batchEntityByHQL(hql, objects);
	}

	@Override
	public T loadEntity(Integer id) {
		return dao.loadEntity(id);
	}

	@Override
	public T getEntity(Integer id) {
		return dao.getEntity(id);
	}
	
	@Override
	public T getEntity(String id) {
		return dao.getEntity(id);
	}

	@Override
	public List<T> findEntityByHQL(String hql, Object... objects) {
		return dao.findEntityByHQL(hql, objects);
	}

	@Override
	public List<T> findAllEntity() {
		return dao.findAllEntity();
	}
}
