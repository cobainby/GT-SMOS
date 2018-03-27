package com.southgt.smosplat.organ.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 
 * 资质管理控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 姚家俊
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年3月1日     姚家俊       v1.0.0        create</p>
 *
 */
@Controller  
public class QualifyController {
	/**
	 * 
	 * 加载资质管理页面 
	 * @date  2017年3月1日 下午3:31:34
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  姚家俊
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年3月1日     姚家俊      v1.0          create</p>
	 *
	 */
	@RequestMapping("/qualify")
	public String toQualify(){
		return "organ/view/qualify";
	}
	
}
