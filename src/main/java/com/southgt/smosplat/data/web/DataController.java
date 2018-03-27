package com.southgt.smosplat.data.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据查看前端控制器
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年4月8日     mohaolin       v1.0.0        create</p>
 *
 */
/**
 * TODO(这里用一句话描述这个类的作用)
 * @version v1.0.0
 * Copyright (C) 2017 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author 吴达
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2017年7月5日     吴达       v1.0.0        create</p>
 *
 */
@Controller
public class DataController {
	
	/**
	 * 引导页
	 * @date  2017年4月8日 上午10:47:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataIndex")
	public String dataIndex(){
		return "data/view/index";
	}
	
	/**
	 * 数据整体情况
	 * @date  2017年4月8日 上午10:47:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataOverview")
	public String dataOverview(){
		return "data/view/overview";
	}
	/**
	 * 查看监测方案
	 * @date  2017年8月10日 下午2:25:56
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月10日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/proPlan")
	public String proPlan(){
		return "data/view/projectPlan";
	}
	/**
	 * 查看工程图
	 * @date  2017年8月10日 下午2:27:26
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月10日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/proPic")
	public String proPic(){
		return "data/view/projectPic";
	}
	/**
	 * 查看工程完成情况
	 * @date  2017年9月20日 下午4:53:39
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月20日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/proStatus")
	public String proStatus(){
		return "data/view/proStatus";
	}
	/**
	 * 围护墙(边坡)顶部水平位移
	 * @date  2017年4月8日 上午10:47:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataWYS")
	public String dataWYS(){
		return "data/view/wys";
	}
	/**
	 * 立柱竖向位移
	 * @date  2017年4月8日 上午10:47:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataLZ")
	public String dataLZ(){
		return "data/view/lz";
	}
	/**
	 * 周边管线竖向位移
	 * @date  2017年4月8日 上午10:47:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataZGD")
	public String dataZGD(){
		return "data/view/zgd";
	}
	/**
	 * 地下水位
	 * @date  2017年4月8日 上午10:47:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataSW")
	public String dataSW(){
		return "data/view/sw";
	}
	/**
	 * 锚杆内力
	 * @date  2017年4月8日 上午10:47:00
	 * @return String
	 * @return
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  mohaolin
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年4月8日     mohaolin      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataMT")
	public String dataMT(){
		return "data/view/mt";
	}
	/**
	 * 支护结构深层水平位移
	 * @date  2017年5月5日 上午9:21:17
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月5日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataCX")
	public String dataCX(){
		return "data/view/cx";
	}
	/**
	 * 围护墙(边坡)顶部竖向位移
	 * @date  2017年5月5日 上午9:39:34
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月5日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataWYD")
	public String dataWYD(){
		return "data/view/wyd";
	}
	/**
	 *支撑内力
	 * @date  2017年5月5日 上午9:40:02
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年5月5日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataZC")
	public String dataZC(){
		return "data/view/zc";
	}
	/**
	 *监测概况
	 * @date  2017年6月13日 下午18:15
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年6月13日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/overview")
	public String overview(){
		return "data/view/overview";
	}
	/**
	 * 周边建筑物竖向位移
	 * @date  2017年7月5日 下午4:33:27
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年7月5日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dataSM")
	public String dataSM(){
		return "data/view/sm";
	}
	/**
	 * 日报导出
	 * @date  2017年9月22日 上午9:01:08
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年9月22日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/dateExport")
	public String dateExport(){
		return "data/view/dateExport";
	}
	/**
	 * 报表导出
	 * @date  2017年8月10日 下午2:28:47
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月10日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/exPortData")
	public String exPortData(){
		return "data/view/exPortData";
	}
	/**
	 * 工程项目的基本信息查看
	 * @date  2017年8月14日 下午2:23:02
	 * 
	 * @return
	 * String
	 * @throws null
	 * 
	 * @version v1.0
	 * @author  白杨
	 * <p>Modification History:</p>
	 * <p>Date         Author      Version     Description</p>
	 * <p> -----------------------------------------------------------------</p>
	 * <p>2017年8月14日     白杨      v1.0          create</p>
	 *
	 */
	@RequestMapping("/basicInfo")
	public String basicInfo(){
		return "data/view/basicInfo";
	}
}
