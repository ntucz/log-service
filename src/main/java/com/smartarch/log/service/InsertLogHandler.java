package com.smartarch.log.service;

import java.util.List;

import com.smartarch.log.bean.LogMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class InsertLogHandler {
	public List<LogMessage> insertLog(List<LogMessage> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean available() {
		// es health check
		return true;
	}

}
