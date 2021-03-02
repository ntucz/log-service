package com.smartarch.log.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import com.smartarch.log.bean.LogMessage;
import com.smartarch.log.dao.ElasticRepository;

@Service
public class ElasticServiceImpl implements IElasticService {

    @Autowired
    private ElasticsearchRestTemplate esTemplate;
    @Autowired
    private ElasticRepository elasticRepository;

    private Pageable pageable = PageRequest.of(0,10);

    @Override
    public Boolean createIndex() {
		/*
		 * IndexOperations indexOperations =
		 * esTemplate.indexOps(IndexCoordinates.of(indexName)); Map<String, Object>
		 * indexSettings = new HashMap<String, Object>(); indexSettings.put("indexName",
		 * indexName); indexSettings.put("shards", indexName);
		 * indexSettings.put("replicas", 1);
		 */
    	if(!indexExists()) {
    		IndexOperations indexOperations = esTemplate.indexOps(LogMessage.class);
    		indexOperations.createMapping(LogMessage.class);
    		return indexOperations.create();
    	}
    	return true;
    }
    
    public Boolean indexExists() {
        IndexOperations indexOperations = esTemplate.indexOps(LogMessage.class);
        return indexOperations.exists();
    }

    @Override
    public Boolean deleteIndex(String index) {
    	IndexOperations indexOperations = esTemplate.indexOps(LogMessage.class);
        return indexOperations.delete();
    }

    @Override
    public void save(LogMessage docBean) {
        elasticRepository.save(docBean);
    }

    @Override
    public void saveAll(List<LogMessage> list) {
        elasticRepository.saveAll(list);
    }

    @Override
    public Iterator<LogMessage> findAll() {
        return elasticRepository.findAll().iterator();
    }

    @Override
    public Page<LogMessage> findByContent(String content) {
        return elasticRepository.findByContent(content,pageable);
    }

	@Override
	public Page<LogMessage> query(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}