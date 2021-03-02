package com.smartarch.log.service;

import java.util.List;

import com.smartarch.log.api.IElasticService;
import com.smartarch.log.api.SpringContextHolder;
import com.smartarch.log.bean.LogMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class InsertLogHandler {
	public void insertLog(List<LogMessage> list) {
		IElasticService esService = SpringContextHolder.getBean(IElasticService.class);
		if(esService!=null) {
			if(esService.createIndex()) {
				esService.saveAll(list);
			}
		}
	}

	public boolean available() {
		// es health check
		return true;
	}

}
