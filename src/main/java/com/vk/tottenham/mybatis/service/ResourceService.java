package com.vk.tottenham.mybatis.service;

import com.vk.tottenham.core.model.Resource;

public interface ResourceService {

    boolean exists(String resourceId);

    void save(Resource resource);

}
