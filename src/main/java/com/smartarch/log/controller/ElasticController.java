package com.smartarch.log.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartarch.log.api.IElasticService;
import com.smartarch.log.bean.LogMessage;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/elastic")
public class ElasticController {

  @Autowired
  private IElasticService elasticService;
  
  @GetMapping("/init")
  public void init(){
      elasticService.createIndex();
      List<LogMessage> list =new ArrayList<>();
      list.add(new LogMessage(1L,"XX0193","XX8064","xxxxxx",1));
      list.add(new LogMessage(2L,"XX0210","XX7475","xxxxxxxxxx",1));
      list.add(new LogMessage(3L,"XX0257","XX8097","xxxxxxxxxxxxxxxxxx",1));
      elasticService.saveAll(list);

  }

  @GetMapping("/all")
  public Iterator<LogMessage> all(){
      return elasticService.findAll();
  }

}