package com.southgt.smosplat.common.util;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

public class WebServiceUtil {
	public static String invokeService(String serviceURL,
			String serviceNamespace,
			String methodName,
			String[] paramsName,
			String[] paramsValue){
		String result=null;
		Service service=new Service();
		try {
			Call call=(Call) service.createCall();
			call.setTargetEndpointAddress(new URL(serviceURL));
			call.setUseSOAPAction(true);
			//同意返回字符串类型
			call.setReturnType(XMLType.SOAP_STRING);
			call.setOperationName(new QName(serviceNamespace, methodName));  
	        call.setSOAPActionURI(serviceNamespace+methodName); 
	        for(int i=0;i<paramsName.length;i++){
        		call.addParameter(new QName(serviceNamespace, paramsName[i]), XMLType.XSD_STRING, ParameterMode.IN);
	        }
	        //定义一个参数值数组，并调用服务
	        result = (String) call.invoke(paramsValue); 
	        return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
