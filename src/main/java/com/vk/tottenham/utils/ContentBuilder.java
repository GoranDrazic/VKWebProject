package com.vk.tottenham.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component("contentBuilder")
public class ContentBuilder {
    public String buildPost(String title, String description, String content, String originalUrl, String site, Icon icon) {
        StringBuilder message = new StringBuilder();
        if (icon != null) {
            message.append(icon.getCode()).append(" ");
        }
        message.append(title).append("\n\n");
        message.append(description).append("\n\n");
        Document document = Jsoup.parse(content);
        Elements paragraphs = document.getElementsByTag("p");
        for (Element paragraph : paragraphs) {
                message.append(paragraph.text()).append("\n\n");
        }
        
        message.append("Original article: " + originalUrl).append("\n\n");
        message.append("(Источник: ").append(site).append(")");
        return message.toString();
    }
}
