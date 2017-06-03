package com.vk.tottenham.footballapi;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("footballApiObjectMapper")
public class FootballApiObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = -4759447640825527814L;

    public FootballApiObjectMapper() {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
}
