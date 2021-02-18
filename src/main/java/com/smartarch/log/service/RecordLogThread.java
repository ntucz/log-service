package com.smartarch.log.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

import com.smartarch.log.bean.LogMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RecordLogThread {
	//队列最大长度
	private static final int MAX_COUNT = 100000;
	private static final int BULK_SIZE = 128;
	private static RecordLogThread instance = null;
	//入库队列
	private ArrayBlockingQueue<LogMessageWrapper> queue = new ArrayBlockingQueue<>(MAX_COUNT);
	
	private InsertLogHandler handler = null;
	
	private RecordLogThread() {
		handler = new InsertLogHandler();
	}
	
	public static synchronized RecordLogThread getInstance() {
		if(instance == null) {
			instance = new RecordLogThread();
		}
		return instance;
	}
	
	public void start() {
		Thread operateThread = new Thread(new WriteLogTask(),
				"RecordLogThread-operate");
		operateThread.setDaemon(true);
		operateThread.start();
	}
	
	public boolean addLog(LogMessage logMessage) {
		if(logMessage == null) {
			Log.error("logMessage is null");
			return false;
		}
		LogMessageWrapper logMessageWrapper = wrapLogMessage(logMessage);
		return addLog(logMessageWrapper);
	}
	
	private boolean addLog(LogMessageWrapper logMessageWrapper) {
		//队列满，立即返回false
		boolean isOffered = queue.offer(logMessageWrapper);
		if(!isOffered) {
			Log.error("The log operate queue is full!");
		}
		Thread.yield();
		return isOffered;
	}
	
	private LogMessageWrapper wrapLogMessage(LogMessage logMessage) {
		LogMessageWrapper logMessageWrapper = new LogMessageWrapper();
		logMessageWrapper.setEsDocumentId(UUID.randomUUID().toString());
		logMessageWrapper.setLogMessage(logMessage);
		return logMessageWrapper;
	}
	
	private List<LogMessageWrapper> handleRequest(List<LogMessageWrapper> list){
		if(list == null || list.isEmpty()) {
			return list;
		}
		return handler.insertLog(list);
	}
	
	private void handleFailedList(List<LogMessageWrapper> fails) {
		if(fails == null) {
			return;
		}
		
		fails.forEach(e->back2Queue(e));
	}
	
	private void back2Queue(LogMessageWrapper logMessageWrapper) {
		LogMessage logMessage = logMessageWrapper.getLogMessage();
		
		//尝试6次
		int retry = logMessage.getRetry();
		if(retry < 5) {
			logMessage.setRetry(retry+1);
			addLog(logMesssageWrapper);
		} else {
			log.warn("log:{} send failed and has exceeded 5 times. it will not send again.");
		}
	}
	
	private void sleep() {
		try {
			Thread.sleep(LogConst.DEFAULT_RECONNECTION_DELAY_TIME);
		} catch(InterruptedException e){
			log.info("sleep failed.", e);
		}
	}
	
	private class WriteLogTask implements Runnable {
		
		List<LogMessageWrapper> list = new ArrayList<>(BULK_SIZE);
		private volatile boolean running = true;
		
		public void run() {
			while(running) {
				try {
					if(!handler.available()) {
						sleep();
						continue;
					}
					LogMessageWrapper req = queue.take();
					if(req!=null) {
						list.add(req);
					}
					
					if(queue.size() == 0 || list.size() == BULK_SIZE) {
						List<LogMessageWrapper> fails = handleRequest(list);
						handlerFailedList(fails);
						list.clear();
								
					}
				} catch (Throwable e) {
					log.error("write log task fail.", e);
					list.clear();
				}
			}
		}
		
		void setRunning(boolean running) {
			this.running = running;
		}
	}
	
}
