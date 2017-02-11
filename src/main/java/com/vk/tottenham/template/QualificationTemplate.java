package com.vk.tottenham.template;

import org.springframework.stereotype.Component;

@Component("qualificationTemplate")
public class QualificationTemplate extends TemplateBase {

    @Override
    public boolean isUpdateNeeded(PageContext context) {
        return false;
    }

    @Override
    public String buildContent(PageContext context) {
        // TODO Auto-generated method stub
        return "";
    }

}
