package com.smartarch.log.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartarch.log.api.IConst;
import com.smartarch.log.api.IElasticService;
import com.smartarch.log.bean.LogMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/logs")
public class ElasticController {

  @Autowired
  private IElasticService elasticService;
  
  @GetMapping("/init")
  public void init(){
      elasticService.createIndex();
      List<LogMessage> list =new ArrayList<>();
      list.add(new LogMessage(1L, IConst.LOG_TYPE_OPER, "user_service", IConst.LOG_RANK_NOTICE, "admin", new Date(), "testesdfsfsdfsf"));
      list.add(new LogMessage(2L, IConst.LOG_TYPE_OPER, "user_service", IConst.LOG_RANK_WARN, "ccc", new Date(), "teste1211sdfsfsdfsf"));
      list.add(new LogMessage(3L, IConst.LOG_TYPE_SYS, "user_service", IConst.LOG_RANK_ERROR, "zzz", new Date(), "testes22222dfsfsdfsf"));
      elasticService.saveAll(list);

  }

	/*
	 * @GetMapping("/all") public Iterator<LogMessage> all(){ return
	 * elasticService.findAll(); }
	 */

}