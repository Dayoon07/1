package com.e.d.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfig {
	
	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();  // 기본 ObjectMapper 빈 등록
    }

}
