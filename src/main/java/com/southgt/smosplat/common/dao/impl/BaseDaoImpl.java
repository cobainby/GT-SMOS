package com.southgt.smosplat.common.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;

import com.southgt.smosplat.common.dao.IBaseDao;


public abstract class BaseDaoImpl<T> implements IBaseDao<T> {
	
	/**
	 * 注入sessionFactory
	 */
	@Resource
	private SessionFactory sessionFactory;
	
	/**
	 * 获得真实的子类的类型
	 */
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		clazz=(Class<T>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * 获得当前session
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	

	//
	// =================写操作====================
	//
	
	@Override
	public void saveEntity(T t) {
		getSession().save(t);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T mergeEntity(T t) {
		return (T) getSession().merge(t);
	}
	
	@Override
	public void evictEntity(T t) {
		getSession().evict(t);
	}

	@Override
	public void updateEntity(T t) {
		getSession().update(t);
	}

	@Override
	public void saveOrUpdateEntity(T t) {
		getSession().saveOrUpdate(t);
	}

	@Override
	public void deleteEntity(T t) {
		getSession().delete(t);
	}
	
	/**
	 * 根据hql批量处理更新
	 */
	@Override
	public void batchEntityByHQL(String hql, Object... objects) {
		Query query=getSession().createQuery(hql);
		//绑定参数
		for (int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		query.executeUpdate();
	}
	
	//
	// =================读操作====================
	//
	
	@Override
	@SuppressWarnings("unchecked")
	public T loadEntity(Integer id) {
		return (T) getSession().load(clazz, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getEntity(Integer id) {
		return (T) getSession().get(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getEntity(String id) {
		return (T) getSession().get(clazz, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findEntityByHQL(String hql, Object... objects) {
		Query query=getSession().createQuery(hql);
		//绑定参数
		for (int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllEntity() {
		return getSession().createCriteria(clazz).setCacheable(true).list();
	}
	
	
	//
	// =================getter setter====================
	//
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
