package com.smartarch.log.api;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;

import com.smartarch.platform.log.bean.LogMessage;

public interface IElasticService {

    Boolean createIndex();

    Boolean deleteIndex(String index);

    void save(LogMessage docBean);

    void saveAll(List<LogMessage> list);

    Iterator<LogMessage> findAll();

    Page<LogMessage> findByContent(String content);

    Page<LogMessage> query(String key);
}
