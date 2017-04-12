package com.vk.tottenham.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vk.tottenham.vk.model.PhotoDescription;

@Component("pageBuilder")
public class PageBuilder {
    private static final String OFFICIAL_SITE = "http://www.tottenhamhotspur.com/";
    @Autowired
    private PhotoDownloader photoDownloader;

    public String buildPost(String title, String description, String content, String originalUrl, String site, String groupId, boolean isTestMode) {
        StringBuilder message = new StringBuilder();
        message.append("===").append(description).append("===").append("\n\n");
        Element body = Jsoup.parse(content).getElementsByTag("body").get(0);
        appendTag(message, body, isTestMode, groupId);
        return message.toString();
    }

    private void appendTag(StringBuilder message, Node element, boolean isTestMode, String groupId) {
        if (element instanceof Element && "strong".equals(((Element)element).tagName())) {
            if ("em".equals(((Element)element.parent()).tagName())) {
                message.append("<center><gray>").append(((Element)element).text()).append(" Чтобы увеличить, нажмите на фото</gray></center>\n");
            } else if ("p".equals(((Element)element.parent()).tagName())) {
                message.append("'''").append(((Element)element).text()).append("'''\n");
            }
        } else if (element instanceof Element && "img".equals(((Element)element).tagName())) {
            PhotoDescription photo = photoDownloader.downloadPhoto(OFFICIAL_SITE + element.attr("src"), isTestMode);
            int height = 440 * photo.getHeight() / photo.getWidth();
            message.append("<center>[[photo-").append(groupId).append("_")
                    .append(photo.getPhotoId()).append("|440x").append(height)
                    .append("px| ]]</center>\n");
        } else if (element.childNodes().size() > 0) {
            for (Node child : element.childNodes()) {
                appendTag(message, child, isTestMode, groupId);
            }
        } else if (element instanceof TextNode) {
            String text = ((TextNode) element).getWholeText();
            if (text.trim().replace("\n", "").length() > 0) {
                message.append(text);
            }
        }
        
        if (element instanceof Element && "p".equals(((Element)element).tagName())) {
            message.append("\n\n");
        }
    }
}
