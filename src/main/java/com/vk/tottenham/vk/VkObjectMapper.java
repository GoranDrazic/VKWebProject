package com.vk.tottenham.vk;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Component("vkObjectMapper")
public class VkObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = -4759447640825527814L;

    public VkObjectMapper() {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
    }
}
