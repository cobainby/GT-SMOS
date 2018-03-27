package com.southgt.smosplat.common.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.organ.entity.Account;
import com.southgt.smosplat.organ.entity.Worker;
import com.southgt.smosplat.organ.service.IOrganService;

/**
 * 
 * 公共跳转逻辑控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年2月22日     mohaolin       v1.0.0        create</p>
 *
 */
@Controller
public class CommonController {
	
	@Resource
	IOrganService organService;
	/**
	 * 跳转到登录页
	 * @date  2016年12月8日 下午5:22:08
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年12月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/")
	public String toLogin(){
		return "common/view/login";
	}
	/**
	 * 返回没有找到结果页面
	 * @date  2016年12月12日 上午11:30:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2016年12月12日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/toFileNoFound")
	public String toFileNoFound(){
		return "admin/common/view/fileNotFound";
	}
	
	/**
	 * 
	 * 跳转到首页 
	 * @date  2017年2月22日 下午4:18:20
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年2月22日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/index")
	public String toIndex(){
		return "common/view/index";
	}
	/**
	 * 获得当前登录用户信息
	 * @date  2017年3月6日 上午9:22:31
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月6日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/getCurrentAccount")
	@ResponseBody
	public String getCurrentAccount(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String, Object>();
		Account account=(Account) request.getSession().getAttribute("account");
		map.put("result", 0);
		map.put("account", account);
		Worker worker=account.getWorker();
		//加上所属机构的uuid
		map.put("organUuid",worker.getOrgan().getOrganUuid());
		//绕过循环序列化的问题
		account.setWorker(null);
		String s=JsonUtil.beanToJson(map);
		account.setWorker(worker);
		return s;
	}
	
}
