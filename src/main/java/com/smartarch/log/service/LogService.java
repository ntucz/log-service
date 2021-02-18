package com.smartarch.log.service;

import com.smartarch.log.bean.LogMessage;
import com.smartarch.log.controller.ElasticController;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogService {
    public static final String LOG_SUCCESS = "log_success";
    
    public static final String LOG_ERROR = "log_error";
    
    //日志级别
    public static final String RANK_INFORM = "rank_inform";
    
    public static final String RANK_WARN = "rank_warn";
    
    public static final String RANK_ERROR = "rank_error";
    
    public static final String RANK_CRITIC = "rank_critic";
    
    private static LogService logService = new LogService();
    
    private static volatile boolean start = false;
    
    private LogService() {
    	
    }
    
    public long recordOperLog(LogMessage message) {
    	log.info("receive an operlog to be inserted:{}", message.getId());
    	startLogThread();
    	RecordLogThread.getInstance().addLog(message);
    	return message.getId();
    }

	private synchronized static void startLogThread() {
		if(!start) {
			RecordLogThread.getInstance().start();
			log.info("Start log thread successfully.");
		}
		start = true;
	}
    
	public static LogService getInstance() {
		return logService;
	}
    
}
