package com.vk.tottenham.instagram;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Component("instagramObjectMapper")
public class InstagramObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 3371579190859941011L;

    public InstagramObjectMapper() {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
}
