package com.southgt.smosplat.project.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.southgt.smosplat.common.util.JsonUtil;
import com.southgt.smosplat.organ.service.IOrganService;
import com.southgt.smosplat.organ.service.IRoleService;
import com.southgt.smosplat.organ.service.IWorkerService;

@Controller
public class TestController {

	@Resource
	IRoleService roleService;
	
	@Resource
	IOrganService organService;
	
	@Resource
	IWorkerService workerService;

	@RequestMapping("/testService")
	@ResponseBody
	public String testService(HttpSession session){
		Map<String, Object> map=new HashMap<String,Object>();
		try {
			
			
			map.put("result", 0);
		} catch (Exception e) {
			map.put("result", -1);
			map.put("msg", e.getMessage());
		}
		String s=JsonUtil.beanToJson(map);
		return s;
	}
	
	@RequestMapping("/testDowload")
	public void testDowload(HttpServletResponse response){
		Map<String, Object> map=new HashMap<String,Object>();
		String s="sssssssss";
		byte[] data=s.getBytes();
		String fileName="data.txt";
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
	    response.addHeader("Content-Length", "" + data.length);  
	    response.setContentType("application/octet-stream;charset=UTF-8");  
	    try {
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
			outputStream.write(data);  
			outputStream.flush();  
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
}
