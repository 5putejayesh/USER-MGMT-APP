package com.jayesh.usermgmt.props;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "usermgmt-api")
@Data
public class ResponseMessageProperties {

	private Map<String, String> messages=new HashMap<>();
	private Map<String, String> regmail=new HashMap<>();
	private Map<String, String> recovermail=new HashMap<>();
}
