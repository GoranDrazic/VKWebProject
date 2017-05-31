package com.vk.tottenham.utils;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.vk.tottenham.exception.VkException;

@Component("jsoupWrapper")
public class JsoupWrapper {
    public Document getDocument(String url) {
        try {
            return Jsoup.parse(new URL(url), 10000);
        } catch (Exception e) {
            throw new VkException("Error loading document from address: " + url, e);
        }
    }
}
