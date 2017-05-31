package com.vk.tottenham.mybatis.service;

public interface ResourceService {

    boolean exists(String type, String source, String id);

    void save(String type, String source, String id);

}
