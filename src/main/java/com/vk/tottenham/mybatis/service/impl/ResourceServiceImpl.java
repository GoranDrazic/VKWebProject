package com.vk.tottenham.mybatis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vk.tottenham.core.model.Resource;
import com.vk.tottenham.mybatis.mappers.ResourceMapper;
import com.vk.tottenham.mybatis.service.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public boolean exists(String type, String source, String id) {
        return resourceMapper.findById(concat(type, source, id)) != null;
    }

    @Override
    public void save(String type, String source, String id) {
        resourceMapper.save(new Resource(concat(type, source, id)));
    }

    private String concat(String type, String source, String id) {
        return type + ":" + source + ":" + id;
    }
}
