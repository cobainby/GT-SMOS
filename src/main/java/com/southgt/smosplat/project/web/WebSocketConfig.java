package com.southgt.smosplat.project.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.southgt.smosplat.common.util.ApplicationUtil;

/**
 * 项目采集双向通讯配置类
 * @version v1.0.0
 * Copyright (C) 2016 广州南方高铁测绘技术有限公司 All rights reserved.
 * @author mohaolin
 * <p>Modification History:</p>
 * <p> Date         Author      Version     Description</p>
 * <p>-----------------------------------------------------------------</p>
 * <p>2016年9月5日     mohaolin       v1.0.0        create</p>
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/**
	 * 应用初始化的时候注入这个实体
	 */
	@Autowired
	private SimpMessagingTemplate template;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry arg0) {
		//添加一个服务端连接点，并支持sockjs
		arg0.addEndpoint("/collect").withSockJS();
		//注入SimpMessagingTemplate后将其保存到ApplicationUtil之中供发送消息使用
		if(ApplicationUtil.getSimpMessagingTemplate()==null){
			ApplicationUtil.setSimpMessagingTemplate(template);
		}
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/collect");
		registry.setApplicationDestinationPrefixes("/collect");
	}
	
	
}
