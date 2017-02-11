package com.vk.tottenham.template;

import org.springframework.stereotype.Component;

@Component("playOffsTemplate")
public class PlayOffsTemplate extends TemplateBase {

    @Override
    public boolean isUpdateNeeded(PageContext context) {
        return context.getFixtures().getContent().size() > 6;
    }

    @Override
    public String buildContent(PageContext context) {
        // TODO Auto-generated method stub
        return "";
    }
}
