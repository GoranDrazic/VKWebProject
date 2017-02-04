package com.vk.tottenham.core.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("vkContext")
public class VKContext {
    @Autowired
    @Qualifier("vkConfig")
    private VKConfig vkConfig;

    @Autowired
    @Qualifier("testVkConfig")
    private VKConfig testVkConfig;

    public String getGroupId(boolean isTestMode) {
        return isTestMode ? testVkConfig.getGroupId() :  vkConfig.getGroupId();
    }

    public String getAlbumId(boolean isTestMode) {
        return isTestMode ? testVkConfig.getAlbumId() :  vkConfig.getAlbumId();
    }

    public String getChatId(boolean isTestMode) {
        return isTestMode ? testVkConfig.getChatId() :  vkConfig.getChatId();
    }
}
